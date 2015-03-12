package com.kth.csd.node.executors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.DelayMeasurement;
import com.kth.csd.utils.Logger;

public class StatisticsCollector {
	
	protected static final String TAG = StatisticsCollector.class.getCanonicalName();
	private static final int SEND_STATISTIC_REQUEST_INTERVAL = 10000;

	public void startPollingFarm(){
		
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
					if(ApplicationContext.isMaster()){
						Logger.d(TAG,"startPollingFarm");
						if(ApplicationContext.getYcsbWritingIPs()!=null){
							if(!ApplicationContext.getYcsbWritingIPs().isEmpty()){
								Logger.d(TAG, "ipsOfWritingYcsbClients"+ApplicationContext.getYcsbWritingIPs());
								ArrayList<String> listOfYcsbClients = ApplicationContext.getYcsbWritingIPs();
								AbstractNetworkMessage requestMsg = new StatisticsRequestMessage(listOfYcsbClients);		
								ApplicationContext.getNodeFarm().broadCast(requestMsg);
								Logger.d(TAG, "Statistics request sent" + requestMsg );			
								//After master sends STATISTICS_REQ to slaves, he should measure his own delay too!
								DelayMeasurement measurement = new DelayMeasurement();
								HashMap<String,Double> rawDelay = measurement.pingAndGetDelay(listOfYcsbClients);	
								HashMap<String,Double> processedDelay = measurement.getProcessedDelay(rawDelay);
								ApplicationContext.masterOwnDelay = processedDelay;
							}
							else
								Logger.d(TAG,"ipsOfWritingYcsbClients is empty!");

							
						}
					}

				}
			};
			timer.scheduleAtFixedRate(task, 0, SEND_STATISTIC_REQUEST_INTERVAL);	
		}
}
