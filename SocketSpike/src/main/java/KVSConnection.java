package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kth.csd.models.ConnectionMetaData;
import com.kth.csd.node.Constants;
import com.kth.csd.node.operation.KeyValueEntry;

public class KVSConnection {
	
	private String mHost;
	private int mPort;
	private Socket clientSocket = null;
	
	private InputStreamReader inputStream;
	
	public KVSConnection(ConnectionMetaData metaData){
		mHost = metaData.getHost();
		mPort = metaData.getPort();
		try {
			clientSocket = new Socket(mHost, mPort);
		} catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error!");
            e.printStackTrace();
        }
	}
	
	public int sendMessage(String message){
        int operationResult  = 0; //assume error occured
        try {
        	OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());;
			outputStream.write(message);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return operationResult;
	}
	
	public String readMessage(){
		StringBuilder sBuilder = new StringBuilder();
        String line;
		try {
			inputStream = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader bufferReader = new BufferedReader(inputStream);
			line = bufferReader.readLine();
			while (line != null) {
	            sBuilder.append(line);
	            line = bufferReader.readLine();
	            if (line != null) {
	                sBuilder.append("\n");
	            }
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sBuilder.toString();
	}

	@Override
	public String toString() {
		return "KVSConnection [mHost=" + mHost + ", mPort=" + mPort
				+ ", clientSocket=" + clientSocket + "]";
	}
	
	
	
}
