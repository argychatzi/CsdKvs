package com.kth.csd.server;

import com.kth.csd.server.socket.SocketMonitor;


/**
 *
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