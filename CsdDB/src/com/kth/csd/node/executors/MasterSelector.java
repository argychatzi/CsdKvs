package com.kth.csd.node.executors;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.Constants;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.MasterOwnDelaytoClients;
import com.kth.csd.utils.Logger;

public class MasterSelector {

	private static final String TAG = MasterSelector.class.getCanonicalName();
	private static String masterWithMinimumDelay = null;
	private static double minValue = Integer.MAX_VALUE;
	
	private Timer mTimer = null;
	
	private class SelectNewMasterTask extends TimerTask{
		public static final int SELECT_NEW_MASTER_INTERVAL = 1000;
		
		@Override
		public void run() {
			if(ApplicationContext.isMaster()){
				HashMap<String, Double> nodeWithDelayCostMap = ApplicationContext.getNodeWithDelayCostMap();

//				String newMasterIp = selectNewMaster(nodeWithDelayCostMap);
				String newMasterIp = "192.168.0.5";
				
				if (!ApplicationContext.getMasterExternalConnection().getHost().equals(newMasterIp)){
					brodcastUpdate(newMasterIp);	
				}
			}
		}
		
		private void brodcastUpdate(String newMasterIp){
			Logger.d(TAG, "selectNewMasterAndBrodcastAPossibleUpdate: currentMaster: " +ApplicationContext.getMasterExternalConnection().getHost() + "newMaster " + newMasterIp);
			Logger.d(TAG, "New master elected, switching ips");
			ConnectionMetaData internal = new ConnectionMetaData(newMasterIp,  Constants.DEFAULT_INTERNAL_PORT);
			ConnectionMetaData external = new ConnectionMetaData(newMasterIp, Constants.DEFAULT_EXTERNAL_PORT);
			ApplicationContext.assignNewMaster(internal, external);
		}
		
		private String selectNewMaster(HashMap<String, Double> mapOfNodeandDelay){
			double masterDelayCost = CostFunctionCalculator.calculateCostForNode(MasterOwnDelaytoClients.calculatDelayToYCSB());
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
		if(mTimer == null){
			Logger.d(TAG, "executing master selector");
			mTimer = new Timer();
			mTimer.schedule(new SelectNewMasterTask(), 0, SelectNewMasterTask.SELECT_NEW_MASTER_INTERVAL);
		}
	}


}
