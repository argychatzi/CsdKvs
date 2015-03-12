package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author georgios.savvidis
 *
 */
public class SocketHandler extends Thread{
	
	private static final String TAG = SocketHandler.class.getCanonicalName();
	private Socket mSocket;
	private KvsOperationMessageQueue mBox;
	
	public SocketHandler(Socket socket, KvsOperationMessageQueue box) {
		mSocket = socket;
		mBox = box;
	}
	
	@Override
	public void run() {
		super.run();
		
		try{
			InputStreamReader streamReader = new InputStreamReader(mSocket.getInputStream());			    
			    
			StringBuilder sbuilder = new StringBuilder();
	        BufferedReader bufferReader = new BufferedReader(streamReader);

            String line = bufferReader.readLine();

            while (line != null) {
                sbuilder.append(line);
                line = bufferReader.readLine();
                if (line != null) {
                    sbuilder.append("\n");
                }
            }			
			streamReader.close();
			bufferReader.close();
			mSocket.close();
			String message = sbuilder.toString();

			Logger.d(TAG,  "message = "+ message);
			
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Failed to get InputStream");
		}
	}
}