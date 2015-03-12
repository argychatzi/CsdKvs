//package com.kth.csd.node.core;
//
//import java.util.HashMap;
//import com.kth.csd.networking.ConnectionMetaData;
//import com.kth.csd.networking.messages.MasterMovedMessage;
//import com.kth.csd.node.Constants;
//import com.kth.csd.utils.Logger;
//
//public class NewMasterSelector {
//
//	private static final String TAG = NewMasterSelector.class.getCanonicalName();
//	//private static HashMap<String, Double> delayCostMap; 
//	private static String masterWithMinimumDelay = null;
//	private static double minValue = Integer.MAX_VALUE;
//	
//	public static void putNodeWithCorrespondingDelay(HashMap<String, Double> ycsbclientsRttMapFromSlave, String slaveIp){		
//		double nodeDelayCost = calculateCostForNode(ycsbclientsRttMapFromSlave);
//		
//		ApplicationContext.updateNodeWithDelayCostMap(slaveIp, nodeDelayCost);
//		Logger.d(TAG, "putNodeWithCorrespondingDelay: Slave "+ slaveIp + " has delay cost "+ nodeDelayCost);
//		selectNewMasterAndBrodcastAPossibleUpdate(ycsbclientsRttMapFromSlave);
//	}
//	
//	//This method should be called passing the delayCostMap argument with
//	//ApplicationContext.getNodeWithDelayCostMap()	
//	public static void selectNewMasterAndBrodcastAPossibleUpdate(HashMap<String, Double> delayCostMap){
//		String newMasterIp = selectNewMaster(delayCostMap);
//		Logger.d(TAG, "selectNewMasterAndBrodcastAPossibleUpdate: currentMaster: " +ApplicationContext.getMasterExternalConnection().getHost() + "newMaster " + newMasterIp);
//		if (!ApplicationContext.getMasterExternalConnection().getHost().equals(newMasterIp)){
//			Logger.d(TAG, "New master elected, switching ips");
//			ConnectionMetaData internal = new ConnectionMetaData(newMasterIp,  Constants.DEFAULT_INTERNAL_PORT);
//			ConnectionMetaData external = new ConnectionMetaData(newMasterIp, Constants.DEFAULT_EXTERNAL_PORT);
//			ApplicationContext.assignNewMaster(internal, external);
//		}
//	}
//	// delay cost calculator for single node
//	public static double calculateCostForNode(HashMap<String, Double> ycsbclientRttMap){
//		double nodeDelayCost = 0;
//		HashMap<String, Double> stateWriteMap = ApplicationContext.getmYcsbClientsStatisticsMapPerSecondWithEma();
//		
//		for (String key: stateWriteMap.keySet()){
//			double delayfromycsbClient= stateWriteMap.get(key)* ycsbclientRttMap.get(key);
//			
//			nodeDelayCost += delayfromycsbClient;
//		}
//		return nodeDelayCost;
//	}
//	
//	public static String selectNewMaster(HashMap<String, Double> mapOfNodeandDelay){
//		double masterDelayCost = calculateCostForNode(MasterOwnDelaytoClients.calculatDelayToYCSB());
//		for (String key: mapOfNodeandDelay.keySet() ) {
//			double value = mapOfNodeandDelay.get(key);
//			if (value < minValue){
//				minValue = value;
//				masterWithMinimumDelay = key;
//			}
//			
//		}
//		if (minValue< masterDelayCost){
//		
//		return masterWithMinimumDelay;
//		}
//		else{
//			return ApplicationContext.getMasterExternalConnection().getHost();
//		}
//	}
//}
