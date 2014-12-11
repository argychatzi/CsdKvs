package com.kth.csd.networking.interfaces.internal;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.ExecutionResultCommunicator;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsResultMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class ClientInternalInputInterface extends IoHandlerAdapter implements IoHandler, ExecutionResultCommunicator{
	
	private HashMap<KvsOperation, IoSession> mSessionVault;
	private HashMap<String, Double> ycsbclientsRttMapFromSlave = new HashMap<String, Double>();
	
	//TODO Jawad, Mihret you should move those and place them in the application Context, also you should name them smt that makes sense
	//for example for "nodeAndPortMap" all I can say is that it describes a pair of node and a port. What is really holding is 
	//all the connections ever opened in the internal channel for the purpose of exchanging StatisticsResult type of data. Isn't 
	//that however smt that concerns the application at a global level, and thus it should be "living" in the application Context?
	public static HashMap<String, Double> nodeWithDelayCostMap = new HashMap<String, Double>();
	public static HashMap<String, Integer> nodeAndPortMap = new HashMap<String, Integer>();
	
	
	public ClientInternalInputInterface() {
		mSessionVault = new HashMap<KvsOperation, IoSession>();
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		switch(response.getType()){
		
			case STATISTICS_RES:{
				
				ycsbclientsRttMapFromSlave = ((StatisticsResultMessage)response).getRttStatistics();
				nodeWithDelayCostMap.put(getSessionIp(session),delayCostCalculatorOfNode(ycsbclientsRttMapFromSlave));
				nodeAndPortMap.put(getSessionIp(session), getPort(session));
				//Logger.d(getNewMasterIp(), "new master");
				break;
			}
		}
	}
	
	public static int getPort(IoSession session){
		SocketAddress socket = session.getLocalAddress();
		int port = 0;
		try {
			 port = Integer.parseInt(socket.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return port;	
	}
	
	// getter for Nodes Ip and port number map
	public static HashMap<String, Integer> getNodeAndPortMap(){	
		return nodeAndPortMap;
	}

	public static String getSessionIp (IoSession session){
		InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
		InetAddress inetAddress = socketAddress.getAddress();
		String sessionIp = inetAddress.getHostAddress();
			return sessionIp;
	}
	
	// delay cost calculator for single node
	public static double delayCostCalculatorOfNode(HashMap<String, Double> ycsbclientsRttMap){
		double nodeDelayCost = 0;
		HashMap<String, Double> tempWriteMap = new HashMap<String, Double>();
		tempWriteMap= ApplicationContext.getKeyValueStore().getycsbClientWritePerSecStatisticsMapWithEma();
		
		for (String key: tempWriteMap.keySet()){
			double delayfromycsbClient= tempWriteMap.get(key)* ycsbclientsRttMap.get(key);
			
			nodeDelayCost += delayfromycsbClient;
		}
	return nodeDelayCost;
	}
	
	// getter for server nodes delay cost map 
	public static HashMap<String, Double> getNodeWithDelayCostMap(){
		return nodeWithDelayCostMap;
	}
	
	//TODO Javad, Mihret: this is a very crucial block of code and it should be a different class. Along with
	//getNewMasterPort()
	// get node Ip, for new master 
	public static String getNewMasterIp(){
	
		HashMap<String, Double> nodeWithDelayCostMap = getNodeWithDelayCostMap();	
		String minKey = null;
		double minValue = Integer.MAX_VALUE;
		for (String key: nodeWithDelayCostMap.keySet() ) {
			double value = nodeWithDelayCostMap.get(key);
			if (value < minValue){
				minValue = value;
				minKey = key;
			}
		}
		return minKey;	
	}
	
	// get port for new master
	public static int getNewMasterPort(String nodeIpWithMindelay){
		int port = 0;
		HashMap<String, Integer> tempHashMap = new HashMap<String, Integer>();
		tempHashMap = getNodeAndPortMap();
		for (String key: tempHashMap.keySet()){
			if(key.equals(nodeIpWithMindelay)){
				port = tempHashMap.get(key);
				break;
			}
			
		}
		return port;
	}
	
	@Override
	public void excutionFinished(KeyValueEntry entry) {
		// TODO Auto-generated method stub
	}
}