package main.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMonitor {

	private static final String TAG = SocketMonitor.class.getCanonicalName();
	
	private ServerSocket mServerSocket;
	private boolean mStopListening;
	private KvsOperationMessageQueue mInBox;
	
	
	public SocketMonitor(KvsOperationMessageQueue inBox, int port) {
		try{
			mInBox = inBox;
			mServerSocket = new ServerSocket(port);
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Could not initiate ServerSocket");
		}
	}
	
	public void startListening(){
		Logger.d(TAG, "Start listening...");

		if( mServerSocket == null){
			System.out.println("The ServerSocket has not been initiated");
			return;
		}
		
		mStopListening = false;
		
		while( !mStopListening ){
			
			try{
				Socket socket = mServerSocket.accept();
                //The code bellow is blocked until a connection is received by mServerSocket.accept().
				//Handle the socket in a separate thread in order to be able to handle multiple sockets simultaneously.
				//Logger.d(TAG, "Received socket!");

				SocketHandler socketHandler = new SocketHandler(socket, mInBox);
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
