package com.kth.csd.node.executors;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.ExponentialMovingAverage;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final long serialVersionUID = 635271620411613274L;

	private static final String TAG = KvsWriter.class.getCanonicalName();
	
	public static String mYcsbclient;
	private Timer mStatisticsTimer = new Timer(); 
	public KvsWriter(KeyValueEntry keyValue) {
		mKeyValue = keyValue;
	}
	
	public KvsWriter(KeyValueEntry keyValue, String ycsbClientIp){
		mKeyValue = keyValue;
		mYcsbclient = ycsbClientIp;
		mStatisticsTimer.schedule(new OperationsPerSecond(),0, OperationsPerSecond.ONE_SECOND *5);
	}
	
	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
		if (ApplicationContext.isMaster()){
			incrementWriteForEveryClient(mYcsbclient); 
			ApplicationContext.getNodeFarm().broadCast(new OperationWriteMessage(mKeyValue));
		}
		
		return new OperationWriteMessage(mKeyValue); 
	}
	// increment the number of writes performed by ycsb clients
	public  void incrementWriteForEveryClient(String clientIPForIncrement){
		if(ApplicationContext.getmYcsbClientsStatisticsMapSoFar().containsKey(clientIPForIncrement)){
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIPForIncrement, ApplicationContext.getmYcsbClientsStatisticsMapSoFar().get(clientIPForIncrement)+1);
		}
		else{
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIPForIncrement, 1);
		}
		
	}
	
 public void getycsbClientWritePerSecStatisticsMapWithEma(){
	 	HashMap<String, Integer> stateSavedPerSecMap = ApplicationContext.getmYcsbClientsStatisticsMapPerSecond();
	 	ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage();
	    for (String key: ApplicationContext.getmYcsbClientsStatisticsMapPerSecond().keySet()){
	    	double movingAverageValue = exponentialMovingAverage.exponentialMovingAverage(stateSavedPerSecMap.get(key));
	    	ApplicationContext.updatemYcsbClientsStatisticsMapPerSecondWithEma(key, movingAverageValue);
	    }
	    
	}
 
 public class OperationsPerSecond extends TimerTask{

		public static final int ONE_SECOND = 999;

		@Override
		public void run() {
			HashMap<String, Integer> stateSavedMapSoFar = ApplicationContext.getmYcsbClientsStatisticsMapSoFar();
			try {
				Thread.sleep(ONE_SECOND);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			HashMap<String, Integer> stateAfterOneSecMap = ApplicationContext.getmYcsbClientsStatisticsMapSoFar();
			for(String key:stateSavedMapSoFar.keySet()){
				ApplicationContext.updatemYcsbClientsStatisticsMapPerSecond(key, stateAfterOneSecMap.get(key)-stateSavedMapSoFar.get(key));
			}
			
		}
	}
	
}
