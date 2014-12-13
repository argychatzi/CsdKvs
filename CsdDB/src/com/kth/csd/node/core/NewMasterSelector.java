package com.kth.csd.node.core;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Logger;

public class NewMasterSelector {

	private static final String TAG = NewMasterSelector.class.getCanonicalName();
	private static HashMap<String, Double> delayCostMap; 
	private static double nodeDelayCost;
	private static String masterWithMinimumDelay = null;
	private static double minValue = Integer.MAX_VALUE;
	
	public static void putNodeWithCorrespondingDelay(HashMap<String, Double> ycsbclientsRttMapFromSlave, IoSession session){		
		nodeDelayCost = calculateCostForNode(ycsbclientsRttMapFromSlave);
		
		ConnectionMetaData connectionMetaData = new ConnectionMetaData(session);
		ApplicationContext.updateNodeWithDelayCostMap(connectionMetaData.getHost(), nodeDelayCost);
		Logger.d(TAG, "putNodeWithCorrespondingDelay: Slave "+ connectionMetaData.getHost() + "has delay cost "+ nodeDelayCost);
	}
	
	//This method should be called passing the delayCostMap argument with
	//ApplicationContext.getNodeWithDelayCostMap()	
	public static void selectNewMasterAndBrodcastAPossibleUpdate(HashMap<String, Double> delayCostMap){
		String newMasterIp = selectNewMaster(delayCostMap);
		
		if (ApplicationContext.getMasterExternalConnection().getHost() != newMasterIp){
			ConnectionMetaData internal = new ConnectionMetaData(newMasterIp,  Constants.DEFAULT_INTERNAL_PORT);
			ConnectionMetaData external = new ConnectionMetaData(newMasterIp, Constants.DEFAULT_EXTERNAL_PORT);
			ApplicationContext.assignNewMaster(internal, external);
		}
	}
	
	// delay cost calculator for single node
	public static double calculateCostForNode(HashMap<String, Double> ycsbclientRttMap){
		double nodeDelayCost = 0;
		HashMap<String, Double> tempWriteMap = new HashMap<String, Double>();
		tempWriteMap= ApplicationContext.getKeyValueStore().getycsbClientWritePerSecStatisticsMapWithEma();
		
		for (String key: tempWriteMap.keySet()){
			double delayfromycsbClient= tempWriteMap.get(key)* ycsbclientRttMap.get(key);
			
			nodeDelayCost += delayfromycsbClient;
		}
		return nodeDelayCost;
	}
	
	public static String selectNewMaster(HashMap<String, Double> mapOfNodeandDelay){	
		for (String key: mapOfNodeandDelay.keySet() ) {
			double value = mapOfNodeandDelay.get(key);
			if (value < minValue){
				minValue = value;
				masterWithMinimumDelay = key;
			}
		}
		return masterWithMinimumDelay;	
	}
}
