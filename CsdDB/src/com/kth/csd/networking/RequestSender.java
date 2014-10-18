package com.kth.csd.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestSender {
	
	public int sendRequest(String host, int port, String message){
        Socket clientSocket = null;

        int operationResult  = 123; //assume error occured
        try {
            clientSocket = new Socket(host, port);
            
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStream.write(message);
            
//            System.out.println("sent to server :"+ message );
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
