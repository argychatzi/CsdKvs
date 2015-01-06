package com.kth.csd.node.executors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.node.Constants;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.ExponentialMovingAverage;
import com.kth.csd.utils.Logger;

public class MasterSelector {

	private static final String TAG = MasterSelector.class.getCanonicalName();

	private class SelectNewMasterTask extends TimerTask {
		public static final int SELECT_NEW_MASTER_INTERVAL = 15000;
		private String currentMasterIp = ApplicationContext
				.getOwnExternalConnection().getHost();

		@Override
		public void run() {
			if (ApplicationContext.isMaster()) {
				HashMap<String, Double> nodeWithDelayCostMap = ApplicationContext.getNodeWithDelayCostMap();
				// Logger.d(TAG,"slave cost map ="+nodeWithDelayCostMap);
				getycsbClientWritePerSecStatisticsMapWithEma();
				if (nodeWithDelayCostMap != null) {
					if (!nodeWithDelayCostMap.isEmpty()) {
						String newMasterIp = selectNewMaster(nodeWithDelayCostMap);
						// Logger.d(TAG,
						// "during comparison with last: "+newMasterIp);
						// Logger.d(TAG, "node " + currentMasterIp + "compares "
						// + currentMasterIp + " and " + newMasterIp);

						if (!currentMasterIp.equals(newMasterIp)) {
							Logger.d(TAG, "broadcast");
							brodcastUpdate(newMasterIp);
						}
					}
				}
				// String newMasterIp = selectNewMaster(nodeWithDelayCostMap);
				// String currentMasterIp =
				// ApplicationContext.getOwnExternalConnection().getHost();
				// The new ip should come from the cost function
				// String newMasterIp = mockMasterMoving(currentMasterIp);
			}
		}

		private void brodcastUpdate(String newMasterIp) {
			Logger.d(TAG, "selectNewMasterAndBrodcastAPossibleUpdate: currentMaster: " + ApplicationContext.getMasterExternalConnection().getHost() + "newMaster " + newMasterIp);
			Logger.d(TAG, "New master elected, switching ips");
			ApplicationContext.assignNewMaster(newMasterIp);
		}

		private String selectNewMaster(HashMap<String, Double> mapOfNodeandDelay) {
			/*
			 * HashMap<String, Double> throuputMap =
			 * ApplicationContext.getmYcsbClientsStatisticsMapPerSecondWithEma
			 * (); double masterDelayCost =
			 * CostFunctionCalculator.calculateCostForNode
			 * (ApplicationContext.masterOwnDelay,throuputMap);
			 * Logger.d(TAG,"masterDelayCost"+masterDelayCost);
			 */
			double minValue = Integer.MAX_VALUE;
			String masterWithMinimumDelay = null;
			// Logger.d(TAG, "mapOfNodeandDelay" +
			// mapOfNodeandDelay.toString());

			if (mapOfNodeandDelay == null || allEqualDelayCostChecker(mapOfNodeandDelay)) {
				return currentMasterIp;
			} else {
				for (String key : mapOfNodeandDelay.keySet()) {
					double value = mapOfNodeandDelay.get(key);
					if (value < minValue) {
						minValue = value;
						masterWithMinimumDelay = key;
					}
				}
			}
			return masterWithMinimumDelay;
		}

		// This checker if added so that if the delay cost for every node
		// including
		// the current master is the same, instead of going through the process
		// of new
		// master selection, it will enable us to make the current master
		// continue as a master
		public boolean allEqualDelayCostChecker( HashMap<String, Double> mapOfNodeandDelay) {
			Logger.d(TAG, "allEqualDelayCostChecker called");
			boolean flag = true;
			ArrayList<Double> mArrayList = new ArrayList<Double>();
			for (String key : mapOfNodeandDelay.keySet()) {
				mArrayList.add(mapOfNodeandDelay.get(key));
			}
			Logger.d(TAG, "mArrayList size=" + mArrayList.size());
			double first = mArrayList.get(0);
			for (int i = 1; i < mArrayList.size() && flag; i++) {
				Logger.d(TAG, "checking mArrayList element " + i + ":"+ mArrayList.get(i));
				if (mArrayList.get(i) != first) {
					flag = false;
				}
				Logger.d(TAG, "flag" + ":" + flag);
			}
			return flag;
		}

		public void getycsbClientWritePerSecStatisticsMapWithEma() {
			HashMap<String, Integer> stateSavedPerSecMap = ApplicationContext.getmYcsbClientsStatisticsMapPerSecond();
			ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage();
			for (String key : ApplicationContext.getmYcsbClientsStatisticsMapPerSecond().keySet()) {
				double movingAverageValue = exponentialMovingAverage.exponentialMovingAverage(stateSavedPerSecMap.get(key));
				ApplicationContext.updatemYcsbClientsStatisticsMapPerSecondWithEma(key, movingAverageValue);
			}
			// Logger.d(TAG,
			// "SelectNewMasterTask, updater called before delay cost calculation");
		}
	}
	
	public void execute() {
		Timer mTimer = new Timer();
		TimerTask task = new SelectNewMasterTask();
		mTimer.scheduleAtFixedRate(task, 0, SelectNewMasterTask.SELECT_NEW_MASTER_INTERVAL);
	}
}
