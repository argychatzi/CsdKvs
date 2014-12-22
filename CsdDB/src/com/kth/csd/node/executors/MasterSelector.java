package com.kth.csd.node.executors;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.Constants;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.utils.Logger;

public class MasterSelector {

	private static final String TAG = MasterSelector.class.getCanonicalName();
	private static double minValue = Integer.MAX_VALUE;
	
	private class SelectNewMasterTask extends TimerTask{
		public static final int SELECT_NEW_MASTER_INTERVAL = 10000;
		private String currentMasterIp = ApplicationContext.getOwnExternalConnection().getHost();
		
		@Override
		public void run() {
			if(ApplicationContext.isMaster()){
				HashMap<String, Double> nodeWithDelayCostMap = ApplicationContext.getNodeWithDelayCostMap();
				Logger.d(TAG,"slave cost map ="+nodeWithDelayCostMap);
				if(nodeWithDelayCostMap!=null){ 
					if(!nodeWithDelayCostMap.isEmpty()){					
						String newMasterIp = selectNewMaster(nodeWithDelayCostMap);
						Logger.d(TAG, "node " + currentMasterIp + "compares " + currentMasterIp + " and " + newMasterIp);
						
						if (!currentMasterIp.equals(newMasterIp)){
							Logger.d(TAG, "broadcast");
							brodcastUpdate(newMasterIp);	
						}			
					}
				}

//				String newMasterIp = selectNewMaster(nodeWithDelayCostMap);
//				String currentMasterIp = ApplicationContext.getOwnExternalConnection().getHost();
				//The new ip should come from the cost function
				//String newMasterIp = mockMasterMoving(currentMasterIp);
				
			}
		}
		
//		private String mockMasterMoving(String ip) {
//			String result = "192.168.0.2";
//			if(ip.equals("192.168.0.2")){
//				result = "192.168.0.5";
//			} else if(ip.equals("192.168.0.5")){
//				result = "192.168.0.4";
//			} else if(ip.equals("192.168.0.4")){
//				result = "192.168.0.7";
//			} else if(ip.equals("192.168.0.7")){
//				result = "192.168.0.8";
//			} else if(ip.equals("192.168.0.8")){
//				result = "192.168.0.10";
//			} else if(ip.equals("192.168.0.10")){
//				result = "192.168.0.11";
//			} else if(ip.equals("192.168.0.11")){
//				result = "192.168.0.12";
//			}
//			return result;
//		}

		private void brodcastUpdate(String newMasterIp){
			Logger.d(TAG, "selectNewMasterAndBrodcastAPossibleUpdate: currentMaster: " +ApplicationContext.getMasterExternalConnection().getHost() + "newMaster " + newMasterIp);
			Logger.d(TAG, "New master elected, switching ips");
			ApplicationContext.assignNewMaster(newMasterIp);
		}
		
		private String selectNewMaster(HashMap<String, Double> mapOfNodeandDelay){
			HashMap<String, Double> throuputMap = ApplicationContext.getmYcsbClientsStatisticsMapPerSecondWithEma();
			double masterDelayCost = CostFunctionCalculator.calculateCostForNode(ApplicationContext.masterOwnDelay,throuputMap);
			Logger.d(TAG,"masterDelayCost"+masterDelayCost);
			
			String masterWithMinimumDelay = null;
			//Logger.d(TAG, "mapOfNodeandDelay" + mapOfNodeandDelay.toString());
			if (mapOfNodeandDelay==null){
				return currentMasterIp;
			}
			for (String key: mapOfNodeandDelay.keySet() ) {
				double value = mapOfNodeandDelay.get(key);
				if (value < minValue) {
					minValue = value;
					masterWithMinimumDelay = key;
				}
			}
			if (minValue< masterDelayCost) {
				return masterWithMinimumDelay;
			} else {
				return ApplicationContext.getMasterExternalConnection().getHost();
			}
		}
	}


	public void execute() {
		Timer mTimer = new Timer();
		TimerTask task = new SelectNewMasterTask();
		mTimer.scheduleAtFixedRate(task, 0, SelectNewMasterTask.SELECT_NEW_MASTER_INTERVAL);
	}


}
