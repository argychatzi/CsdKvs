package com.kth.csd.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gson.Gson;
import com.kth.csd.node.KvsOperationMessageQueue;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

/**
 * @author georgios.savvidis
 *
 */
public class SocketHandler extends Thread{
	
	private static final String TAG = SocketHandler.class.getCanonicalName();
	private Socket mSocket;
	private Gson mGson;
	private KvsOperationMessageQueue mBox;
	
	public SocketHandler(Socket socket, KvsOperationMessageQueue box) {
		mSocket = socket;
		mGson = new Gson();
		mBox = box;
	}
	
	@Override
	public void run() {
		super.run();
		
		try{
			InputStreamReader streamReader = new InputStreamReader(mSocket.getInputStream());			    
			    
			StringBuilder sbuilder = new StringBuilder();
	        BufferedReader bufferReader = new BufferedReader(streamReader);

            String line = bufferReader.readLine();

            while (line != null) {
                sbuilder.append(line);
                line = bufferReader.readLine();
                if (line != null) {
                    sbuilder.append("\n");
                }
            }			
			
			streamReader.close();
			bufferReader.close();
			mSocket.close();
			String message = sbuilder.toString();
			KvsOperation operation = mGson.fromJson(message, KvsOperation.class);
//			Logger.d(TAG, "received msg of type " + operation.getYcsbOperationType());
			mBox.enqueueRequestOperation(operation);

		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Failed to get InputStream");
		}
	}
}