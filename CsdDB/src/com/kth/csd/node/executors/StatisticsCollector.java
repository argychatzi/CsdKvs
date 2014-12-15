package com.kth.csd.node.executors;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.StatisticsRequestMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.utils.Logger;

public class StatisticsCollector {
	
	protected static final String TAG = StatisticsCollector.class.getCanonicalName();
	private static final int sendRequestInterval = 10000;//1 second

	public void startPollingFarm(){
    	if(ApplicationContext.isMaster()) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){
				@Override
				public void run() {
					if(ApplicationContext.getYcsbIPs()!=null){
						ArrayList<String> listOfYcsbClients = ApplicationContext.getYcsbIPs();
						AbstractNetworkMessage requestMsg = new StatisticsRequestMessage(listOfYcsbClients);		
						ApplicationContext.getNodeFarm().broadCast(requestMsg);
					}
				}
			};
			timer.scheduleAtFixedRate(task, 0, sendRequestInterval);	
		}
    }
}
