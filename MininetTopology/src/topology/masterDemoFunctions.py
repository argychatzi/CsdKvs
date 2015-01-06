#!/usr/bin/python

'''
This is used to show demo: Master Moved and Master Handover + Data replication on slave nodes
'''

from __future__ import print_function    
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.topo import Topo
from mininet.util import dumpNodeConnections
from mininet.node import Node, Host
import time

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

        self.addLink(s1, s2)
        self.addLink(s2, s3)
   

def run():
    "Create and test a simple network"
    topo = CsdTopology()
    net = Mininet(topo)
    net.start()
    print( 'Dumping host connections' )
    dumpNodeConnections(net.hosts)
    
    hosts = net.hosts
    for i in range(len(hosts)):
        print( '%s IP: %s' % (hosts[i], hosts[i].IP()) )

    #print "Compaling Java classes..."
    #compileJavaClasses(net)

    print( 'Deploying servers and clients...' )
    runNetworkClasses(net)

    #print "Testing network connectivity"
    #net.pingAll()

    #CLI( net )
    
    net.stop()
        
def runNetworkClasses(net):
    pathToYcsb = getYcsbPath()

    n1_1 = net.get('n1.1')
    n2_2 = net.get('n2.2')
    n3_3 = net.get('n3.3')
    
    c1_1 = net.get('c1.1')
    c2_1 = net.get('c2.1')
    c3_1 = net.get('c3.1')
    
    # Starting servers...
    print( 'Starting servers in switched network 1...' )
    for i in range(4):
        node = net.get('n1.%s' % (i+1))
        print( 'Starting server %s...' % node.name )
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+1) + '&> logs/' + node.name + '.txt &')

    print( 'Starting servers in switched network 2...' )
    for i in range(2):
        node = net.get('n2.%s' % (i+1))
        print( 'Starting server %s...' % node.name )
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+5) + ' &> logs/' + node.name + '.txt &')

    print( 'Starting servers in switched network 3...' )
    for i in range(4):
        node = net.get('n3.%s' % (i+1))
        print( 'Starting server %s...' % node.name )
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+7) + ' &> logs/' + node.name + '.txt &')
        
    time.sleep(30)        
    # Starting clients...

    print( 'Starting client %s...' % c1_1.name )
    result = c1_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c1_1.name + '.txt &')
    print( 'Client result %s' % result )

    time.sleep(10)        
  
    print( 'Starting client %s...' % c2_1.name )    
    result = c2_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c2_1.name + '.txt &')
    print( 'Client result %s' % result )
    
    time.sleep(10)
    print( 'Starting client %s...' % c3_1.name )
    result = c3_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c3_1.name + '.txt')
    print( 'Client result %s' % result )
    
    time.sleep(10)
    print ('running the workload on clients')
    result = c1_1.cmd( pathToYcsb + '/bin/ycsb run elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c1_1.name + '.txt &')
    time.sleep(10)
    result = c2_1.cmd( pathToYcsb + '/bin/ycsb run elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c2_1.name + '.txt &')
    time.sleep(10)
    result = c3_1.cmd( pathToYcsb + '/bin/ycsb run elasticsearch -s -P ' + pathToYcsb + '/workloads/workload_csd &> logs/' + c3_1.name + '.txt')
    time.sleep(10)
def getYcsbPath():
    propertiesFile = 'properties/ycsb_path.txt'

    with open(propertiesFile) as f:
        ycsbPath = f.readline()
    f.close()
    
    return ycsbPath

if __name__ == '__main__':
# Tell mininet to print useful information
    setLogLevel('info')
    run()
