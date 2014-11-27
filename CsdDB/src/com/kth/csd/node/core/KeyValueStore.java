package com.kth.csd.node.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.kth.csd.node.Constants;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.utils.Logger;


public class KeyValueStore extends java.util.HashMap<String, HashMap<String, String>> {

	private static final String TAG = KeyValueStore.class.getCanonicalName();
	private Timer mFlushToDiskTimer = new Timer();
	protected File mDatabaseFile;
	private Gson mGson;
	
	private static KeyValueStore sKeyValueStore;
	
	private KeyValueStore(){
		mFlushToDiskTimer.scheduleAtFixedRate(new FlushToDisk(), 0, Constants.FLUSH_TO_DISK_PERIOD);
		mGson = new Gson();
		mDatabaseFile = new File(Constants.DATABASE_FILE);
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
	
	private class FlushToDisk extends TimerTask{
		private static final String TAG = "FlushToDisk";

		@Override
		public void run() {
			Logger.d(TAG,  "will flush to disk");
			//
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
