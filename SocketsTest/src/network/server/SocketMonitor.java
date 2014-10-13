package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMonitor extends Thread{
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
	
	@Override
	public void run() {
		super.run();
		startListening();
	}
	
	private void startListening(){
		System.out.println("Start listening...");
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
				System.out.println("Received socket!");
				SocketHandler socketHandler = new SocketHandler(socket);
				socketHandler.start();

			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void stopMonitor(){
		System.out.println("Stop monitor");
		stopListening();
		interrupt();
	}
	
	private void stopListening(){		
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
