#!/usr/bin/python
'''
Created on Oct 5, 2014

@author: georgios.savvidis


'''

from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.topo import Topo
from mininet.util import dumpNodeConnections
from mininet.node import Node, Host

#class CustomHost(Host):
#	def __init__(self, name, inNamespace=True, **params):
#		Host.__init__(self, name, inNamespace=inNamespace, **params)

class SingleSwitchNetwork():
    
    def __init__(self, topo, n=3, address='192.168.0.0', mask='24', firstNodeIp='1', netId=1, **opts):
        
        # Initialize topology and default options
        switch = topo.addSwitch('s%s.1' % netId)
        
        #Add a client
        clientAddress = address[:-1] + '%s' % firstNodeIp
        client = topo.addHost('c%s.1' % netId, ip = clientAddress)
        topo.addLink(client, switch)

        # Create n nodes
        for i in range(n):
            nodeAddress = address[:-1] + '%s' % (i + 1 + firstNodeIp)
            node = topo.addHost('n%s.%s' % (netId, (i + 1)), ip = nodeAddress + '/' + mask)
            topo.addLink(node, switch)

          
class CsdTopology(Topo):
    ''' The created topology looks like this:
    
            n1.1     n1.2        n2.1     n2.2       n3.1     n3.2
                \   /               \   /               \   / 
       c1.1 --- s1.1 -------------- s2.1 -------------- s3.1 --- c3.1
                /  \                 /                  /  \
            n1.3    n1.4           c2.1             n3.3    n3.4
    '''
    
    
    def __init__(self, **opts):
        # Initialize topology and default options
        Topo.__init__(self, **opts)
        SingleSwitchNetwork(self, n=4, address='192.168.0.0', firstNodeIp=1, netId=1)
        SingleSwitchNetwork(self, n=2, address='192.168.0.0', firstNodeIp=6, netId=2)
        SingleSwitchNetwork(self, n=4, address='192.168.0.0', firstNodeIp=9, netId=3)

        s1 = 's1.1'
        s2 = 's2.1'
        s3 = 's3.1'
        #s4 = self.addSwitch('s4.1') #for redundancy purposes

        self.addLink(s1, s2)
        self.addLink(s2, s3)
        #self.addLink(s1, s4)
        #self.addLink(s3, s4)
   

#Test SimpleTopology
def run():
    "Create and test a simple network"
    topo = CsdTopology()
    net = Mininet(topo)
    net.start()
    print 'Dumping host connections'
    dumpNodeConnections(net.hosts)
    
    hosts = net.hosts
    for i in range(len(hosts)):
        print "%s IP: %s" % (hosts[i], hosts[i].IP())

    print "Executing Java program..."
    net.get('c1.1').cmd('javac network/HelloWorld.java')
    result = net.get('c1.1').cmd('java network/HelloWorld')
    print result
    
    #print "Testing network connectivity"
    #net.pingAll()

    #CLI( net )
    
    net.stop()

if __name__ == '__main__':
# Tell mininet to print useful information
    #print "test"
    setLogLevel('info')
    run()