package com.kth.csd.networking.messages;

import java.util.HashMap;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class StatisticsResultMessage extends AbstractNetworkMessage{
	
	public StatisticsResultMessage(HashMap<String, Integer> results){
		super(type.STATISTICS_RES);
		mData = new HashMap<String, Integer>(results);
	}
	
	public  HashMap<String, Double> getRttStatistics(){
		return (HashMap<String, Double>) mData;
	}
}