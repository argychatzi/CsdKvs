package com.kth.csd.node.core;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import com.codahale.metrics.EWMA;
import com.kth.csd.utils.Logger;
/*
+ * A class that calculates the exponential moving average on a list of double numbers
+ * Input list of numbers i.e. [16, 135, 65,13]  
+ * Output is a list of number after taking the history of numbers (Alpha value) into consideration using 
+ * the EMA i.e. [15.88, 136.12, 55.19]
+ * 
 */
public class ExponentialMovingAverageExpanded {
	//private static final String nodeIP = "1.1.1.1";
	private static final String TAG = "ExponentialMovingAverageExpanded";
	private double alpha;
	private final long interval=1;
	TimeUnit intervalUnit=TimeUnit.SECONDS;
	private DecimalFormat numberFormat = new DecimalFormat("000.00");
	private HashMap<String, Double> outputHashmapEMAResults = new HashMap<String, Double>();
	private HashMap<String, EWMA> EMAHashmapKeeper = new HashMap<String, EWMA>() ;
	//String [] testIP;	
	private ArrayList<String> clientYCSPListIP;
	// In the constructor creating EMAHashmapKeeper object that will keep the state of the different EMA objects.
	public ExponentialMovingAverageExpanded(double alpha, ArrayList<String> clientYCSPListIP ) {
		this.alpha =alpha;
		this.clientYCSPListIP=clientYCSPListIP;
		
		Logger.d(TAG, "inside ExponentialMovingAverageExpanded constructor");
		for (int i=0; i<clientYCSPListIP.size(); i++){
			Logger.d(TAG, "i="+ i);
			Logger.d(TAG, "filling the EMA object" + "alpha="+alpha+"interval"+interval+"intervalUnit="+intervalUnit );
			Logger.d(TAG, "Here");
			try{
				Logger.d(TAG, "EWMAObj created");
				//ExponentialMovingAverage exponentialMovingAverage = new ExponentialMovingAverage();
				EWMA mEWMAObj = new EWMA(alpha, interval, intervalUnit);
				EMAHashmapKeeper.put(clientYCSPListIP.get(i), mEWMAObj );
				Logger.d(TAG, "EMAHashmapKeeper put");
			}
			catch (Exception e) {
				Logger.d(TAG, e.toString());
			}
		}
	}

	public HashMap<String, Double> calculatExponentialMovingAverage(HashMap<String, Double> ResultsHashmapOfLatencyInMS) {

		outputHashmapEMAResults.clear();
		for (String key : ResultsHashmapOfLatencyInMS.keySet()) {
			EWMA ewmaObjRefrence = EMAHashmapKeeper.get(key);
			ewmaObjRefrence.update(ResultsHashmapOfLatencyInMS.get(key).longValue());
			ewmaObjRefrence.tick();
			Double resultsValue = Double.parseDouble(numberFormat.format(ewmaObjRefrence.getRate(intervalUnit)));
			outputHashmapEMAResults.put(key, resultsValue);

		}
		/* The output format is a Hashmap (String, Hashmap), which will be {nodeIP= {ClientIP=DelayMS, ..etc}}
		 * Example of output:
		 * EMA calculated from the measurement data is : {1.1.1.1={facebook.com=232.75, google.com=3.11, Yahoo=0.0}}
		 * with 0.0 value for failed ping.
		 */
		return outputHashmapEMAResults;

	}


}