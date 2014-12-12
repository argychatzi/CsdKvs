package com.kth.csd.networking.messages;

import java.util.HashMap;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class StatisticsResultMessage extends AbstractNetworkMessage{
	
	public StatisticsResultMessage(HashMap<String, Double> results){
		super(type.STATISTICS_RES);
		mData = new HashMap<String, Double>(results);
	}
	
	public  HashMap<String, Double> getResultsOfDelayMeasurement(){
		return (HashMap<String, Double>) mData;
	}
}