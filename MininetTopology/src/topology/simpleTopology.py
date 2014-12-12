#!/usr/bin/python
'''
Created on Oct 5, 2014

@author: georgios.savvidis


'''

from __future__ import print_function    
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.net import Mininet
from mininet.topo import Topo
from mininet.util import dumpNodeConnections
from mininet.node import Node, Host

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

    CLI( net )
    
    #net.stop()
    
#def compileJavaClasses(net):
    #randomHost = net.get('c1.1')
    #resultClient = randomHost.cmd('javac network/client/ClientMain.java')
    #resultServer = randomHost.cmd('javac network/server/ServerMain.java')
    #print( resultClient )
    #print resultServer
    
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
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+1) + ' > logs/' + node.name + '.txt &')

    print( 'Starting servers in switched network 2...' )
    for i in range(2):
        node = net.get('n2.%s' % (i+1))
        print( 'Starting server %s...' % node.name )
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+5) + ' > logs/' + node.name + '.txt &')

    print( 'Starting servers in switched network 3...' )
    for i in range(4):
        node = net.get('n3.%s' % (i+1))
        print( 'Starting server %s...' % node.name )
        node.cmd('java -jar network/server/CsdDBServer.jar ' + str(i+7) + ' > logs/' + node.name + '.txt &')
        
    # Starting clients...

#     print( 'Starting client %s...' % c1_1.name )
# #    setServerIP( n1_1.IP() )
#     result = c1_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workloadc >logs/' + c1_1.name + '.txt &')
#     print( 'Client result %s' % result )
#    
#     print( 'Starting client %s...' % c2_1.name )    
# #    setServerIP( n2_2.IP() )
#     result = c2_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workloadc >logs/' + c2_1.name + '.txt &')
#     print( 'Client result %s' % result )
#     
#     print( 'Starting client %s...' % c3_1.name )
# #    setServerIP( n3_3.IP() )
#     result = c3_1.cmd( pathToYcsb + '/bin/ycsb load elasticsearch -s -P ' + pathToYcsb + '/workloads/workloadc >logs/' + c3_1.name + '.txt')
#     print( 'Client result %s' % result )
    
def setServerIP(serverIP):
    propertiesFile = 'properties/server_ip.txt'

    with open(propertiesFile, 'w') as f:
        print(serverIP, file=f)
    f.close()
    
def getYcsbPath():
    propertiesFile = 'properties/ycsb_path.txt'

    with open(propertiesFile) as f:
        ycsbPath = f.readline()
    f.close()
    
    return ycsbPath

#def CompileUpdateClasses(net):
    
#    print 'Compiling update Server, and client sniffer classes'
#    node= net.get('n1.1')
#    resultServer= node.cmd('javac /home/jawad/ik2200/multicastServer/src/multicastServer/MulticastServer.java')
#    print resultServer
#    resultclient= node.cmd ('javac /home/jawad/ik2200/multicastServer/src/clientbroadcastsniffer/Snifferclient.java')
#    print resultclient

def runupdateinterface(net):
    
    print ('Client Sniffer running on n3.1, n3.2, n3.3, n3.4')

    for i in range(4):
       nodeClient =net.get('n3.%s' % (i+1))
       print ('Running Snifferclient %s ' % nodeClient.name)
       nodeClient.cmd ('java /home/jawad/ik2200/multicastServer/src/clientbroadcastsniffer/Snifferclient' + ' > logs/' + nodeClient.name + '.txt')

    

    nodeserver= net.get('n1.1')

    print ('running master broadcast node on %s..' % nodeserver.name)
    nodeserver.cmd ('java /home/jawad/ik2200/multicastServer/src/multicastServer/MulticastServer')

 

if __name__ == '__main__':
# Tell mininet to print useful information
    setLogLevel('info')
    run()
