package com.kth.csd.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kth.csd.utils.Logger;
import com.sun.jmx.snmp.Timestamp;

public class RequestSender {
	
	
	public void sendRequest(){
		sendRequest(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
	}

	public void sendRequest(String host, int port){
        System.out.println("Send Request " + new Timestamp().toString());
        Socket clientSocket = null; // hi everyone 

        try {
            clientSocket = new Socket(host, port);
            
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            String messageOnWire =  "Hello there! " + new Timestamp().toString() ; 
            outputStream.write(messageOnWire);
            
            System.out.println("sent to server :"+ messageOnWire );
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
