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
	
	public static String mYcsbclientIp;
	private Timer mStatisticsTimer = new Timer(); 
	
	public KvsWriter(KeyValueEntry keyValue) {
		mKeyValue = keyValue;
	}
	
	public KvsWriter(KeyValueEntry keyValue, String ycsbClientIp){
		mKeyValue = keyValue;
		mYcsbclientIp = ycsbClientIp;
		mStatisticsTimer.schedule(new OperationsPerSecond(),0, OperationsPerSecond.ONE_SECOND *5);
	}
	
	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		//Logger.d(TAG, "ApplicationContext.getKeyValueStore()" + ApplicationContext.getKeyValueStore().toString());
		ApplicationContext.getKeyValueStore().put(mKeyValue.getKey(), mKeyValue.getValues());
		//Logger.d(TAG, "ApplicationContext.getKeyValueStore()" + ApplicationContext.getKeyValueStore().toString());

		if (ApplicationContext.isMaster()){
			Logger.d(TAG," execute"+"I am master and I am going to incrementWriteForClientWithIp");
			incrementWriteForClientWithIp(mYcsbclientIp); 
			ApplicationContext.getNodeFarm().broadCast(new OperationWriteMessage(mKeyValue));
			Logger.d(TAG,"Broadcast finished");
		}
		
		return new OperationWriteMessage(mKeyValue); 
	}
	
	// increment the number of writes performed by ycsb clients
	public  void incrementWriteForClientWithIp(String clientIp){
		Logger.d(TAG,"incrementWriteForClientWithIp ApplicationContext.getmYcsbClientsStatisticsMapSoFar()"+ApplicationContext.getmYcsbClientsStatisticsMapSoFar());
		Logger.d(TAG,"incrementWriteForClientWithIp clientIp"+clientIp);
		//Logger.d(TAG,"ApplicationContext.getmYcsbClientsStatisticsMapSoFar().containsKey(clientIp)" + ApplicationContext.getmYcsbClientsStatisticsMapSoFar().toString());
		HashMap<String, Integer> YCSBClientsWriteStatistics= ApplicationContext.getmYcsbClientsStatisticsMapSoFar();
		if( YCSBClientsWriteStatistics.isEmpty()){
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, 1);
			Logger.d(TAG, "client is the first element in the list");
		} 
		else if(!YCSBClientsWriteStatistics.containsKey(clientIp)){
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, 1);
			Logger.d(TAG, "Client added to list");
		}
		else{
			int writeStatistics= ApplicationContext.getmYcsbClientsStatisticsMapSoFar().get(clientIp)+1;
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, writeStatistics);
			Logger.d(TAG, "counter for client increased, the number of writes is = " + YCSBClientsWriteStatistics.get(clientIp));
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
