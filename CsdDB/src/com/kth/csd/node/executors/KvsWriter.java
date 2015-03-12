package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final long serialVersionUID = 635271620411613274L;

	private static final String TAG = KvsWriter.class.getCanonicalName();
	
	public static String mYcsbclientIp;
	
	public KvsWriter(KeyValueEntry keyValue) {
		mKeyValue = keyValue;
	}
	
	public KvsWriter(KeyValueEntry keyValue, String ycsbClientIp){
		mKeyValue = keyValue;
		mYcsbclientIp = ycsbClientIp;
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
		HashMap<String, Integer> YCSBClientsWriteStatistics= ApplicationContext.getmYcsbClientsStatisticsMapSoFar();
		if( YCSBClientsWriteStatistics.isEmpty()){
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, 1);
			//Logger.d(TAG, "client is the first element in the list");
		} 
		else if(!YCSBClientsWriteStatistics.containsKey(clientIp)){
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, 1);
			//Logger.d(TAG, "Client added to list");
		}
		else{
			int writeStatistics= ApplicationContext.getmYcsbClientsStatisticsMapSoFar().get(clientIp)+1;
			ApplicationContext.updatemYcsbClientsStatisticsMapSoFar(clientIp, writeStatistics);
			//Logger.d(TAG, "counter for client increased, the number of writes is = " + YCSBClientsWriteStatistics.get(clientIp));
		}
	}
}
