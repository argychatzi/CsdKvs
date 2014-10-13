package com.kth.csd.networking;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.LinkedList;


public class UpdateRequestSender {
	
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 4444;
	private List<Socket> sockets = new LinkedList<Socket>();
	
	public void updateSendRequest(){
		updateSendRequest(DEFAULT_HOST, DEFAULT_PORT);
	}

	public void updateSendRequest(String host, int port){
        System.out.println("Update Request");
        Socket clientSocket = null; 

        try {
            clientSocket = new Socket(host, port);
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            outputStream.write( "Update Request!" );
            outputStream.flush();
            outputStream.close();
            sockets.add(clientSocket);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error!");
            e.printStackTrace();
        }
	}
}
