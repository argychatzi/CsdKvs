package com.kth.csd.node.core;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;
import com.kth.csd.utils.Logger;


/*
+ * Delay Measurement class as part of the Master Selection logic, 
+ * that measures the delay from slave nodes to client nodes using Ping command
+ * Author: Ahmed Sadek  
 */

// Master IP, Clients IPs are assumed to be an input 

public class DelayMeasurement {

//	private static Logger LOG = Logger.getLogger( DelayMeasurement.class.getName() );
//	private  static Handler fileHandler;
	private static HashMap<String, Double> resultsHashmap= new HashMap<String, Double>();
	private static String pingCmd = "";
	private static String inputLine;
	private final static String TAG = "DelayMeasurement";
	static String[] timeString;
	
	/*
+	  This method will calculate the delay between slave node and client node
+	  Input is a string IP1,IP2,IP3  i.e. 10.10.10.1,10.10.10.2,10.10.10.3,10.10.10.4
+	  Output is returned using two methods  getResultsString or getResultsList
+	  Output format NodeIP IP1:Delay,IP2:Delay,IP3:Delay i.e.  1.1.1.1 google.com:3,facebook.com:142,bbc.com:44
+	 */
	public static int CalculateDelayFromSlaveToClientNode(ArrayList<String> clientsIPString) {


		try {
			if (clientsIPString.isEmpty()) {
				Logger.d(TAG,"Error in client IP list recieved ! ");
			}
			Logger.d(TAG,"Configuration done.");
		

			/* iterating over the IP list and pinging each IP using the System process.*/
			for (String clientIP : clientsIPString) {
				Runtime runtime = Runtime.getRuntime();
				pingCmd = "ping " + clientIP + " -c 1";			
				Process sysProcess = runtime.exec(pingCmd);			
				String lines = new String();
				BufferedReader incoming = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));
				// receiving the ping results and searching for time value
				//Logger.d(TAG,"inputLine is null ? "+String.valueOf(inputLine==null));
				//The whole line is like:blablabla .. time= xx ms ..blabla
				//The code below is to extract this time value by splitting the whole line for twice
				while ((inputLine = incoming.readLine()) != null) {	
					if(inputLine.contains("time=")){
						String[] tempString = inputLine.split("time=");
//						Logger.d(TAG,"a1 is "+tempString[1]);
						timeString = tempString[1].split("ms");
//						Logger.d(TAG,"b0 is "+timeString[0]);
						break;
					}
					Logger.d(TAG,"inputLine="+inputLine);
					lines += inputLine;
				}
				Logger.d(TAG,"final lines="+lines);
				incoming.close();

				if(timeString[0]!=null){
					Logger.d(TAG, "time found");
					Double convertstringtodouble = Double.parseDouble(timeString[0]);			
					resultsHashmap.put(clientIP, convertstringtodouble);
					Logger.d(TAG, "time="+convertstringtodouble);
				}
				else {
					resultsHashmap.put(clientIP, 0.0);
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return 1;
	}

	public static HashMap<String, Double> getDelayResultsHashmap(){
		return resultsHashmap;
	} 
}