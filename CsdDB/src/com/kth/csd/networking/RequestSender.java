package com.kth.csd.networking;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.kth.csd.utils.Logger;
import com.sun.jmx.snmp.Timestamp;

public class RequestSender {
	
	public void sendRequest(String host, int port, String message){
        Socket clientSocket = null;

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
	}
}
