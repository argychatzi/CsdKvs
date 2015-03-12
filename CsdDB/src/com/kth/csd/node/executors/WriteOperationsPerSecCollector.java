package com.kth.csd.node.executors;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.utils.Logger;

public class WriteOperationsPerSecCollector {

	protected static final String TAG = WriteOperationsPerSecCollector.class.getCanonicalName();
	private static HashMap<String, Integer> beforeOneSecStatisticsMap = new HashMap<String, Integer>();
	private static final int INTERVAL = 1000;
	
	private class OperationPerSecTask extends TimerTask{
		
		@Override
		public void run() {
			if(ApplicationContext.isMaster()){
				if(ApplicationContext.getmYcsbClientsStatisticsMapSoFar()!=null){ 
					if(!ApplicationContext.getmYcsbClientsStatisticsMapSoFar().isEmpty()){	
						HashMap<String, Integer> newMap = ApplicationContext.getmYcsbClientsStatisticsMapSoFar();
						if(!beforeOneSecStatisticsMap.isEmpty()){
							for(String key:newMap.keySet()){
								int persec = newMap.get(key)-beforeOneSecStatisticsMap.get(key);
								ApplicationContext.updatemYcsbClientsStatisticsMapPerSecond(key, persec);
								//Logger.d(TAG, "OperationPerSecTask, run: Per sec Write map = "+ApplicationContext.getmYcsbClientsStatisticsMapPerSecond());
							}
						}
						else{
							Logger.d(TAG, "Within OperationPerSecTask class and run method: This is the first beforeOneSecStatisticsMap.");
						}
						beforeOneSecStatisticsMap.putAll(newMap);
						//Logger.d(TAG, "OperationPerSecTask, run: assign the new map as the next old map "+newMap);
					}
				}
				
			}
		}
	}

	public void execute() {
		Timer mTimer = new Timer();
		TimerTask task = new OperationPerSecTask();
		mTimer.scheduleAtFixedRate(task, 0, INTERVAL);
	}
}

