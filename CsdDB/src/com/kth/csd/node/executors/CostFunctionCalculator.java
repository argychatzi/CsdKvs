package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.node.core.ApplicationContext;

public class CostFunctionCalculator {

	
	public static double calculateCostForNode(HashMap<String, Double> ycsbclientRttMap){
		double cost = 10;
//		HashMap<String, Double> map = ApplicationContext.getmYcsbClientsStatisticsMapPerSecondWithEma();
//		for (String key: map.keySet()){
//			double delayfromycsbClient= map.get(key)* ycsbclientRttMap.get(key);
//			cost += delayfromycsbClient;
//		}
		return cost;
	}
	
}
