package com.kth.csd.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMonitor {
    private static final int DEFAULT_PORT = 4444;

	private ServerSocket mServerSocket;
	private boolean mStopListening;
	
	public SocketMonitor() {
		try{
			mServerSocket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Could not initiate ServerSocket");
		}
	}
	
	public void startListening(){
		System.out.println("Start listening...");
		if( mServerSocket == null){
			System.out.println("The ServerSocket has not been initiated");
			return;
		}
		
		mStopListening = false;
		
		while( !mStopListening ){
			
			//This block will break only when the stopListening() is called.
			//Ada: but seems stopListening() is never called 
			
			try{
				Socket socket = mServerSocket.accept();
                //The code bellow is blocked until a connection is received by mServerSocket.accept().
				//Handle the socket in a separate thread in order to be able to handle multiple sockets simultaneously.
				System.out.println("Received socket!");
				SocketHandler socketHandler = new SocketHandler(socket);
				socketHandler.start();

			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void stopListening(){		
		if( mServerSocket == null){
			System.out.println("The ServerSocket has not been initiated");
			return;
		}
		
		try{
			mStopListening = true;
			mServerSocket.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Could not close ServerSocket");
		}
	}
}
