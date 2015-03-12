package com.kth.csd.node.core;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import com.codahale.metrics.EWMA;
import com.kth.csd.node.Constants;
import com.kth.csd.utils.Logger;


/*
+ * Delay Measurement class as part of the Master Selection logic, 
+ * that measures the delay from slave nodes to client nodes using Ping command
+ * Author: Ahmed Sadek  
 */

// Master IP, Clients IPs are assumed to be an input 

public class DelayMeasurement {
	private final static String TAG = "DelayMeasurement";
	private String pingCmd = "";
	private String inputLine;
	private String[] timeString;

	private final double alpha = Constants.ALPHA;
	private final long interval=1;
	private final TimeUnit intervalUnit=TimeUnit.SECONDS;
	private final DecimalFormat numberFormat = new DecimalFormat("000.00");
	//private HashMap <String, EWMA> keeper = ApplicationContext.ewmaKeeper;
	public static HashMap <String, EWMA> keeper = new HashMap<String, EWMA>();
	private final double TIME_NOT_FOUND_VALUE = 10000;

	/*
+	  This method will calculate the delay between slave node and client node
+	  Input is a string IP1,IP2,IP3  i.e. 10.10.10.1,10.10.10.2,10.10.10.3,10.10.10.4
+	  Output is returned using two methods  getResultsString or getResultsList
+	  Output format NodeIP IP1:Delay,IP2:Delay,IP3:Delay i.e.  1.1.1.1 google.com:3,facebook.com:142,bbc.com:44
+	 */

	public DelayMeasurement() {
	}

	public HashMap<String, Double> pingAndGetDelay(ArrayList<String> clientsIPString)  {
		HashMap<String, Double> rawDelay= new HashMap<String, Double>();

		if (clientsIPString==null) {
			Logger.d(TAG,"clientsIPString is null ");
		}
		else if(clientsIPString.isEmpty()){
			Logger.d(TAG,"clientsIPString is empty");
		}	
		else{
			/* iterating over the IP list and pinging each IP using the System process.*/
			for (String clientIP : clientsIPString) {
				Runtime runtime = Runtime.getRuntime();
				pingCmd = "ping " + clientIP + " -c 1";			
				Process sysProcess;
				try {
					sysProcess = runtime.exec(pingCmd);
					String lines = new String();
					BufferedReader incoming = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));
					while ((inputLine = incoming.readLine()) != null) {	
						if(inputLine.contains("time=")){
							String[] tempString = inputLine.split("time=");
							timeString = tempString[1].split("ms");
							break;
						}
						lines += inputLine;
					}
					incoming.close();
					if(timeString[0]!=null){				
						//Change the unit from ms to us
						Double convertstringtodouble = Double.parseDouble(timeString[0])*1000;			
						rawDelay.put(clientIP, convertstringtodouble);
						//Logger.d(TAG, "time="+convertstringtodouble);
					}
					else {
						Logger.d(TAG, "time not found");
						rawDelay.put(clientIP, TIME_NOT_FOUND_VALUE);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			


			}

		}
		return rawDelay;

	}







	public HashMap<String, Double> getProcessedDelay(HashMap<String, Double> rawDelay) {
		HashMap<String, Double> processedDelay = new HashMap<String, Double>();
		EWMA ewmaObject;

		if (keeper==null){
			Logger.d(TAG, "keeper is null");	
		}

		for (String clientIP : rawDelay.keySet()) {
			if(keeper.containsKey(clientIP)){
				ewmaObject = keeper.get(clientIP);
				//Logger.d(TAG,"processedDelay"+"IP already exits in the keeper"+clientIP );
			}			
			else{
				//Logger.d(TAG,"processedDelay"+"IP doesn't exit in the keeper, adding"+clientIP );
				ewmaObject = new EWMA(alpha, interval, intervalUnit); 
				keeper.put(clientIP, ewmaObject);
			}

			ewmaObject.update(rawDelay.get(clientIP).longValue());
			ewmaObject.tick();
			Double resultsValue = Double.parseDouble(numberFormat.format(ewmaObject.getRate(intervalUnit)));
			processedDelay.put(clientIP, resultsValue);

		}
		return processedDelay;

	}


}