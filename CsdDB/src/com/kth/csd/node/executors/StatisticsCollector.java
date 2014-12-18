package com.kth.csd.node.executors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.node.core.ApplicationContext;

public class StatisticsCollector {
	
	protected static final String TAG = StatisticsCollector.class.getCanonicalName();
	private static final int SEND_STATISTIC_REQUEST_INTERVAL = 10000;

	public void startPollingFarm(){
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
					if(ApplicationContext.isMaster()){
						if(ApplicationContext.getYcsbWritingIPs()!=null){
							ArrayList<String> listOfYcsbClients = ApplicationContext.getYcsbWritingIPs();
							AbstractNetworkMessage requestMsg = new StatisticsRequestMessage(listOfYcsbClients);		
							ApplicationContext.getNodeFarm().broadCast(requestMsg);
						}
					}

				}
			};
			timer.scheduleAtFixedRate(task, 0, SEND_STATISTIC_REQUEST_INTERVAL);	
		}
}
