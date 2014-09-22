package com.kth.csd.client;

import com.kth.csd.client.requestsender.RequestSender;



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