package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author georgios.savvidis
 *
 */
public class SocketHandler extends Thread{
	private Socket mSocket;
	
	public SocketHandler(Socket socket){
		mSocket = socket;
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
			
			System.out.println("Received message: " + sbuilder.toString());
			
			streamReader.close();
			bufferReader.close();
			mSocket.close();
			
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Failed to get InputStream");
		}
	}
}