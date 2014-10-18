package com.kth.csd.node.executors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;
import com.kth.csd.node.Constants;
import com.kth.csd.node.executors.KvsExecutor.KvsExecutable;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;

public class KvsWriter extends KvsOperation implements KvsExecutable {

	private static final String TAG = KvsWriter.class.getCanonicalName();
	
	protected File mDatabaseFile;
	protected Gson mGson;

	public KvsWriter(KeyValueEntry keyValue, Socket socket) {
		super(YCSB_OPERATION.WRITE, keyValue, socket);
		mGson = new Gson();
		mDatabaseFile = new File(Constants.DATABASE_FILE);
	}

	@Override
	public int execute() {
		int resultCode = Constants.RESULT_CODE_FAILURE;
		
		if(mKeyValue.getKey()!=null && mKeyValue.getValues() != null){
				
			try{
				System.out.println("INSERT key = " + mKeyValue.getKey());
	
				FileWriter fileWriter = new FileWriter(mDatabaseFile, true);
	            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	
	            KeyValueEntry entry = new KeyValueEntry(mKeyValue.getKey(), mKeyValue.getValues());
	
	            bufferedWriter.write( mGson.toJson(entry) );
	            bufferedWriter.newLine();
	
	            bufferedWriter.close();
	            fileWriter.close();
				
				resultCode = Constants.RESULT_CODE_SUCCESS;
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return resultCode;
	}

}
