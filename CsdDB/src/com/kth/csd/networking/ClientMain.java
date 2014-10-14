package com.kth.csd.networking;




/**
 *
 * @author georgios.savvidis
 */
public class ClientMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	RequestSender sender = new RequestSender();
    	sender.sendRequest();
    }
}