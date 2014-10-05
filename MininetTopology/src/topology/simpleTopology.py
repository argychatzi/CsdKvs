#!/usr/bin/python
'''
Created on Oct 5, 2014

@author: georgios.savvidis


'''

from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.topo import Topo
from mininet.util import dumpNodeConnections


class SingleSwitchNetwork():
    
    def __init__(self, topo, n=3, address='192.168.0.0', mask='24', firstNodeIp='1', netId=1, **opts):
        
        # Initialize topology and default options
        switch = topo.addSwitch('s%s.1' % netId)
        # Python's range(N) generates 0..N-1
        for i in range(n):
            hostAddress = address[:-1] + '%s' % (i + firstNodeIp)
            host = topo.addHost('h%s.%s' % (netId, (i + 1)), ip = hostAddress + '/' + mask)
            topo.addLink(host, switch)
          
class SimpleTopology(Topo):
    ''' The created topology looks like this:
    
        h1.1     h1.2        h2.1     h2.2       h3.1     h3.2
            \   /               \   /               \   / 
            s1.1 -------------- s2.1 -------------- s3.1
            /  \                                    /  \
        h1.3    h1.4                            h3.3    h3.4
    '''
    
    
    def __init__(self, **opts):
        # Initialize topology and default options
        Topo.__init__(self, **opts)
        SingleSwitchNetwork(self, n=4, address='192.168.0.0', firstNodeIp=1, netId=1)
        SingleSwitchNetwork(self, n=2, address='192.168.0.0', firstNodeIp=5, netId=2)
        SingleSwitchNetwork(self, n=4, address='192.168.0.0', firstNodeIp=7, netId=3)

        s1 = 's1.1'
        s2 = 's2.1'
        s3 = 's3.1'
        #s4 = self.addSwitch('s4.1') #for redundancy purposes

        self.addLink(s1, s2)
        self.addLink(s2, s3)
        #self.addLink(s1, s4)
        #self.addLink(s3, s4)
   

#Test SimpleTopology
def simpleTest():
    "Create and test a simple network"
    topo = SimpleTopology()
    net = Mininet(topo)
    net.start()
    print "Dumping host connections"
    dumpNodeConnections(net.hosts)
    
    hosts = net.hosts
    for i in range(len(hosts)):
        print "%s IP: %s" % (hosts[i], hosts[i].IP())
    
    print "Testing network connectivity"
    net.pingAll()

    #CLI( net )
    
    net.stop()

if __name__ == '__main__':
# Tell mininet to print useful information
    setLogLevel('info')
    simpleTest()