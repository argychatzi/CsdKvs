package com.kth.csd.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kth.csd.utils.Logger;

public class RequestSender {
	
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 4444;
	
	public void sendRequest(){
		sendRequest(DEFAULT_HOST, DEFAULT_PORT);
	}

	public void sendRequest(String host, int port){
        System.out.println("Send Request");
        Socket clientSocket = null; // hi everyone 

        try {
            clientSocket = new Socket(host, port);
            
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStream.write( "Hello there!" );
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
	}
}