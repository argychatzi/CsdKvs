package com.kth.csd.node.core;

import java.util.ArrayList;
import java.util.HashMap;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Logger;

public class MasterOwnDelaytoClients {
private static final String TAG = MasterOwnDelaytoClients.class.getCanonicalName();
private  static ExponentialMovingAverageExpanded MasteremaStateObj;
private static HashMap<String, Double> masteremaResults;
	
public static HashMap<String, Double> calculatDelayToYCSB() {
	
	HashMap<String, Double> delayResultsHashmap = new HashMap<String, Double>();
	
//		if (!(ApplicationContext.getYcsbIPs()==null)){
//			ArrayList<String> ListOfYcsbClients = ApplicationContext.getYcsbIPs();
//			if (!MasteremaStateObj.equals(null)){
//				Logger.d(TAG, "EWMA Master Object");
//				MasteremaStateObj = new ExponentialMovingAverageExpanded(Constants.ALPHA, ListOfYcsbClients);
//			}
//			Logger.d(TAG, "Master pinging client");
//			DelayMeasurement.CalculateDelayFromSlaveToClientNode(ListOfYcsbClients);
//			masteremaResults = MasteremaStateObj.calculatExponentialMovingAverage(DelayMeasurement.getDelayResultsHashmap());
//			delayResultsHashmap = DelayMeasurement.getDelayResultsHashmap();		
//		}
		return delayResultsHashmap;
	}
}
