package com.kth.csd.node.executors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;
import com.kth.csd.node.Constants;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;

public class KvsReader extends KvsOperation implements KvsExecutable {

	private static final String TAG = KvsReader.class.getCanonicalName();
	
	protected File mDatabaseFile;
	protected Gson mGson;
	

	public KvsReader(KeyValueEntry keyValueEntry, Socket socket) {
		super(YCSB_OPERATION.READ, keyValueEntry, socket);
		mGson = new Gson();
		mDatabaseFile = new File(Constants.DATABASE_FILE);
	}

	@Override
	public int execute() {
		int resultCode = Constants.RESULT_CODE_FAILURE;
			
		try{
			if(mKeyValue.getKey()!=null){
				FileReader fileReader = new FileReader( mDatabaseFile );
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				
//				System.out.println("READ: " + mKeyValue.getKey());
				
				for( String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine() ){
					KeyValueEntry entry = mGson.fromJson(line, KeyValueEntry.class);
	
					if(entry.getKey().equals(mKeyValue.getKey())){
//						System.out.println("Found key:" + mKeyValue.getKey() + " value:" + entry.getValues());
						resultCode = Constants.RESULT_CODE_SUCCESS;
						break;
					}
				}
				
				bufferedReader.close();
				fileReader.close();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return resultCode;
	}
}
