package com.kth.csd.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kth.csd.node.Constants;

public class RequestSender {
	
	private String mHost;
	private int mPort;
	
	public RequestSender(String host){
		this(host, Constants.DEFAULT_PORT);		
	}
	
	public RequestSender(String host, int port){
		mHost = host;
		mPort = port;
	}
	
	public int sendRequest(String message){
        Socket clientSocket = null;

        int operationResult  = 123; //assume error occured
        try {
            clientSocket = new Socket(mHost, mPort);
            
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStream.write(message);
            
            outputStream.flush();
            outputStream.close();
            
            clientSocket.close();
            
        } catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error!");
            e.printStackTrace();
        }
		return operationResult;
	}
}
