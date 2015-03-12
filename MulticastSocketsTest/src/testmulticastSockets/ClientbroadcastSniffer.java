package testmulticastSockets;

/*
 * @jawad munir
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClientbroadcastSniffer {
	public static void main(String[] args) throws IOException {
		// create a multicast socket, and binds it to port 4449 hard-coded
	    MulticastSocket mSOCK = new MulticastSocket(4449);
	    /*so client are listening, server broadcast comes or not but we keep
	     * on listening , and not only listen but but also join the the correct 
	     * multicast group; 
	   */
	    // jion the multicast group
	   // connect to multicast address "Class D" IP address,  
	    
	    InetAddress InetAddObj = InetAddress.getByName("224.0.0.1");
	    
	    // using method joinGroup() on mSOCK, and pass our Multicast IP
	    mSOCK.joinGroup(InetAddObj);
	    // now the socket is setup and we ready to recieve packets
	    // declare a Datagram packet, to receive the broadcast 
	    DatagramPacket datapacket;
	    
	    /* get a few DatagramPacket datapackets from broadcast server,
	     * and the following loops can been seen clearly that i want to grab
	     * 3 packets from the broadcast server, when i connect.  
	    */
	    byte[] buf = new byte[1024]; // buffer 
	      datapacket = new DatagramPacket(buf, buf.length);
	    
	    //for (int i = 0; i < 3; i++) {
	      while (datapacket.getData()!= null){
	     // byte[] buf = new byte[1024]; // buffer 
	      datapacket = new DatagramPacket(buf, buf.length);
	      mSOCK.receive(datapacket);
	      /* finally lets do something useful with the data we recieved, 
	       * print it out stdout
	       */
	      System.out.println("Received data from: " + datapacket.getAddress().toString() + ":" + datapacket.getPort() + " with length: " +datapacket.getLength());
	      
	      String str = new String(datapacket.getData(), 0, datapacket.getLength());
	      
	      System.out.println(str);
	      // still need to write recieved broadcast on slave db.txt
	      
	      //System.out.println("broadcast recieved from server: " + str);
	    }
	    // finished recieving data, leave the multicast group

	    mSOCK.leaveGroup(InetAddObj);
	    mSOCK.close(); // close the socket
	  }
	}