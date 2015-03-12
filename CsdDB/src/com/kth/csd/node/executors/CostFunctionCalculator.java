package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.utils.Logger;

public class CostFunctionCalculator {

	private static String TAG = "CostFunctionCalculator";
	public static double calculateCostForNode(HashMap<String, Double> ycsbclientRttMap, HashMap<String, Double> throuputMap){

		double cost=0;		
		//Logger.d(TAG,"if two maps have the same size="+String.valueOf(ycsbclientRttMap.size()==throuputMap.size()));
		//Logger.d(TAG,"rttmap size="+ycsbclientRttMap.size());
		//Logger.d(TAG,"throuputMap size="+throuputMap.size());
			for (String key: throuputMap.keySet()){
				double delayfromycsbClient= throuputMap.get(key)* ycsbclientRttMap.get(key);
				cost = cost + delayfromycsbClient;
			}	
		
		return cost;
	}
	
}
