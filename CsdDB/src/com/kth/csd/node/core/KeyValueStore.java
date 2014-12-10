package com.kth.csd.node.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.networking.messages.OperationWriteMessage;
import com.kth.csd.node.Constants;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;


public class KeyValueStore extends java.util.HashMap<String, HashMap<String, String>> {
	
	private static final long serialVersionUID = 1L;
	private static final String TAG = KeyValueStore.class.getCanonicalName();
	private Timer mFlushToDiskTimer = new Timer();
	private Timer mStatisticsTimer = new Timer(); 
	protected File mDatabaseFile;
	private int mWriteOperationsPerformedSoFar;
	private int mWriteOperationsPerSecond;
	private Gson mGson;
	private static ConnectionMetaData connectionMetaData;
	private static AbstractNetworkMessage abstractNetworkMessage;
	private static OperationReadMessage operationReadMessage;
	
	//private static NodeFarm nodeFarm;
	
	private static KeyValueStore sKeyValueStore;
	
	private String writingClientIP; 
	private static HashMap <String, Integer> ycsbClientsStatisticsMapSoFar = new HashMap <String, Integer> ();
	public static HashMap <String, Integer> ycsbClientsStatisticsMapPerSecond = new HashMap <String, Integer>();
	public static HashMap <String, Double> ycsbClientsStatisticsMapPerSecondWithEma = new HashMap <String, Double>();
	
	private KeyValueStore(){
		mFlushToDiskTimer.scheduleAtFixedRate(new FlushToDisk(), 0, Constants.FLUSH_TO_DISK_PERIOD);
		mStatisticsTimer.schedule(new OperationsPerSecond(),0, OperationsPerSecond.TIME_WINDOW);
		mGson = new Gson();
		mDatabaseFile = new File(Constants.DATABASE_FILE);
		//mWriteOperationsPerformedSoFar = 0;
	}
	
	public static KeyValueStore getInstance(){
		if (sKeyValueStore == null){
			sKeyValueStore = new KeyValueStore();
		}
		return sKeyValueStore;
	}
	
	@Override
	public HashMap<String, String> get(Object key) {
		HashMap<String, String> value = super.get(key);
		if (value == null ){
			Logger.d(TAG, "not found in memory, going to file");
			value = readValueFromFile(key.toString());
		}
		return value;
	}
	

	@Override
	public HashMap<String, String> put(String key, HashMap<String, String> value) {
	// data broadcast to all other slave nodes
		ApplicationContext.getNodes().broadCast(updateSlaveNodes(key, value));
		Logger.d(TAG, "data replication to slave nodes");
		
	    incrementWriteForEveryClient(getWritingClientIP());
		return super.put(key, value);
	}
	
	private HashMap<String, String> readValueFromFile(String key){
		HashMap<String, String> value = new HashMap<>();
		try{
			if(key!=null){
				FileReader fileReader = new FileReader(mDatabaseFile);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
				for( String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine() ){
					KeyValueEntry entry = mGson.fromJson(line, KeyValueEntry.class);
	
					if(entry.getKey().equals(key)){
						value = entry.getValues();
						break;
					}
				}
				
				bufferedReader.close();
				fileReader.close();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return value;
	}
	
	
	//casting keyvalueEntry to AbstractNetworkMessage
	public AbstractNetworkMessage updateSlaveNodes(String key, HashMap<String, String> value){
	KeyValueEntry keyValueEntry = new KeyValueEntry(key, value);
	OperationWriteMessage operationWriteMessage = new OperationWriteMessage(keyValueEntry);
		return operationWriteMessage;
	}
	
	public void updateWritingClientIP(String currentlyWritingClientIP){
	this.writingClientIP = currentlyWritingClientIP;
}

	public  void incrementWriteForEveryClient(String clientIPForIncrement){
	if(ycsbClientsStatisticsMapSoFar.containsKey(clientIPForIncrement)){
		ycsbClientsStatisticsMapSoFar.put(clientIPForIncrement, ycsbClientsStatisticsMapSoFar.get(clientIPForIncrement)+1);
	}
	else{
		ycsbClientsStatisticsMapSoFar.put(clientIPForIncrement, 1);
	}
}
	public HashMap <String, Integer> getYcsbClientsStatisticsMapSoFar(){
	return ycsbClientsStatisticsMapSoFar;
}
	public String getWritingClientIP(){
	return writingClientIP;
}

// getter for delay cost calculation for a node
	public static HashMap<String, Integer> getycsbClientWritePerSecStatistics(){
	return ycsbClientsStatisticsMapPerSecond;
}

	public static HashMap<String, Double> getycsbClientWritePerSecStatisticsMapWithEma(){
    HashMap<String, Integer> tempHashMap = new HashMap<String, Integer>();
    tempHashMap = getycsbClientWritePerSecStatistics();
    for (String key: tempHashMap.keySet()){

    	double movingAverageValue = ExponentialMovingAverage.exponentialMovingAverage(tempHashMap.get(key));
    	ycsbClientsStatisticsMapPerSecondWithEma.put(key, movingAverageValue);
    }
	return ycsbClientsStatisticsMapPerSecondWithEma;
}


	public class OperationsPerSecond extends TimerTask{

		public static final int TIME_WINDOW = 1000;

		@Override
		public void run() {
			
			ArrayList<Integer> tempOperations = new ArrayList<Integer>();
			int counter=0;
			for(String key:ycsbClientsStatisticsMapSoFar.keySet()){
				tempOperations.add(getYcsbClientsStatisticsMapSoFar().get(key));
			}
			try {
				Thread.sleep(TIME_WINDOW -1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	
			for(String key:ycsbClientsStatisticsMapSoFar.keySet()){
	
				ycsbClientsStatisticsMapPerSecond.put(key, getYcsbClientsStatisticsMapSoFar().get(key)-tempOperations.get(counter));	
				counter++;
			}
		}
	}
	
	private class FlushToDisk extends TimerTask{
		private static final String TAG = "FlushToDisk";

		@Override
		public void run() {
			Logger.d(TAG,  "will flush to disk");
		}
		
		private int writeToKvs(KeyValueEntry keyValueEntry){
			int resultCode = Constants.RESULT_CODE_FAILURE;
			if(keyValueEntry.getKey()!=null && keyValueEntry.getValues() != null){
				
				try{

					//TODO have Writers as member fields
					FileWriter fileWriter = new FileWriter(mDatabaseFile, true);
		            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		            KeyValueEntry entry = new KeyValueEntry(keyValueEntry.getKey(), keyValueEntry.getValues());
		
		            bufferedWriter.write( mGson.toJson(entry) );
		            bufferedWriter.newLine();
		
		            bufferedWriter.close();
		            fileWriter.close();
					
					resultCode = Constants.RESULT_CODE_SUCCESS;
				} catch(IOException e){
					e.printStackTrace();
				}
			} else {
				
			}
			return resultCode;
		}
	}
}


