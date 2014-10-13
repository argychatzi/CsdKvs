package com.kth.csd.networking;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;

public class TestUpdater {
	  
	  public void send(List<Socket> allSockets,Commands commands){
	   Gson gson = new Gson();
	   String request = gson.toJson(commands);
	      char[] incomingRequest = request.toCharArray();
	     
	      for(Socket socket : allSockets){
	       PrintWriter printWriter;
	       try {
	        printWriter = new PrintWriter(socket.getOutputStream());
	        printWriter.write(incomingRequest, 0, incomingRequest.length);
	        printWriter.flush();
	       } 
	       catch (IOException e) {
	        System.out.println("Exeption raised while writing to the socket" + e.toString());
	       }
	    
	      }
	  }
	}
