package com.kth.csd.node.core;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.Constants;

public class AssignNewMaster {

	private static HashMap<String, Double> delayCostMap; 
	private static double nodeDelayCost;
	private static String masterWithMinimumDelay = null;
	private static double minValue = Integer.MAX_VALUE;
	
	public static void assignNewMaster(HashMap<String, Double> ycsbclientsRttMapFromSlave, IoSession session){		
		nodeDelayCost = delayCostCalculatorOfNode(ycsbclientsRttMapFromSlave);
		delayCostMap.put(getSessionIp(session), nodeDelayCost);
		ApplicationContext.setMasterInternalConnection(new ConnectionMetaData(newMasterIP(delayCostMap), Constants.DEFAULT_INTERNAL_PORT));
		ApplicationContext.setMasterExternalConnection(new ConnectionMetaData(newMasterIP(delayCostMap), Constants.DEFAULT_EXTERNAL_PORT));
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
	public static String newMasterIP(HashMap<String, Double> mapOfNodeandDelay){	
		for (String key: mapOfNodeandDelay.keySet() ) {
			double value = mapOfNodeandDelay.get(key);
			if (value < minValue){
				minValue = value;
				masterWithMinimumDelay = key;
			}
		}
		return masterWithMinimumDelay;	
	}
	public static String getSessionIp (IoSession session){
		InetSocketAddress socketAddress = (InetSocketAddress) session.getRemoteAddress();
		InetAddress inetAddress = socketAddress.getAddress();
		String sessionIp = inetAddress.getHostAddress();
			return sessionIp;
	}
}
