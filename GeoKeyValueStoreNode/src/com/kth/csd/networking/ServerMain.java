package com.kth.csd.networking;



/**
 * @author georgios.savvidis
 */
public class ServerMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	SocketMonitor socketMonitor = new SocketMonitor();
    	socketMonitor.startListening();
    }
}