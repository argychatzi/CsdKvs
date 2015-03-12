package multicastServer;
/*
 * @ jawad munir 
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastServer {
	
		
	public static void main(String[] args) {
		DatagramSocket udpsocket = null;
		DatagramPacket dataPacket = null;
		byte[] buf;
		final int port = 4488;
		
		    //int iteration = 0;
		    
		    
		    // datagram multicast server
		    
		try {
			
		    udpsocket = new DatagramSocket();
		    //long counter = 0;
		    String str = "";
		      
		    BufferedReader brObj= new BufferedReader(new FileReader("/home/jawad/ik2200/multicastServer/src/db.txt"));
		      
		    while ((str=brObj.readLine())!= null) {
		    	
		    	 
		        // outstr = "I am multicast server jawad " + counter;
		        //counter++;
		    	//System.out.println(str);
		        buf = str.getBytes();
		 
		        //Send to multicast IP address and port
		        InetAddress address = InetAddress.getByName("224.0.0.1");
		        dataPacket = new DatagramPacket(buf, buf.length, address, port);
		 
		        udpsocket.send(dataPacket);
		        
		 
		        //System.out.println(outstr);
		      //System.out.println(str);
		      //udpsocket.close();
		        try {
		          Thread.sleep(500);
		        } catch (InterruptedException ie) {
		        }
		      }
		      
		    } catch (IOException ioe) {
		      System.out.println(ioe);
		    }
		    udpsocket.close();
		  }
		}


