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
	//private Timer mFlushToDiskTimer = new Timer();
	//protected File mDatabaseFile;
	private Gson mGson;
	private static KeyValueStore sKeyValueStore;

	private KeyValueStore(){

		//mFlushToDiskTimer.scheduleAtFixedRate(new FlushToDisk(sKeyValueStore), 0, Constants.FLUSH_TO_DISK_PERIOD);
		mGson = new Gson();
		
		//String databaseFile = ApplicationContext.getOwnInternalConnection().getHost() + ".txt";
		//mDatabaseFile = new File(databaseFile);
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
			Logger.d(TAG, "not found in memory");
			//value = readValueFromFile(key.toString());
			value = new HashMap<String, String>();
		}
		return value;
	}
	

	@Override
	public HashMap<String, String> put(String key, HashMap<String, String> value) {
		return super.put(key, value);
	}
/*	
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
	}*/
	/*
	private class FlushToDisk extends TimerTask{
		private static final String TAG = "FlushToDisk";
		private KeyValueStore keyValueStore;
		
		public FlushToDisk(KeyValueStore keyValueStore) {
			this.keyValueStore = keyValueStore;
		}

		@Override
		public void run() {
			Logger.d(TAG,  "will flush to disk");
			flushToDisk();
		}
		
		private void flushToDisk(){
			if (keyValueStore==null || keyValueStore.isEmpty()){
				Logger.d(TAG,  "keyValueStore is empty");	
			}
			
			else {
			
			Logger.d(TAG,  "keyValueStore is not empty");
			for (String key: keyValueStore.keySet()) {

				if(key!=null && keyValueStore.get(key) != null){
					
					try{
						
						//TODO have Writers as member fields
						FileWriter fileWriter = new FileWriter(mDatabaseFile, false);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
						
						KeyValueEntry entry = new KeyValueEntry(key, keyValueStore.get(key));
						
						bufferedWriter.write( mGson.toJson(entry) );
						bufferedWriter.newLine();
						
						bufferedWriter.close();
						fileWriter.close();
					} catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		}
	}*/
}


