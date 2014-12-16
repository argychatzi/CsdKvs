package com.kth.csd.networking.messages;

import java.util.HashMap;

public class StatisticsResultMessage extends AbstractNetworkMessage{
	
	private static final long serialVersionUID = 1400344446837376847L;

	public StatisticsResultMessage(HashMap<String, Double> results){
		super(type.STATISTICS_RES);
		mData = new HashMap<String, Double>(results);
	}
	
	public HashMap<String, Double> getResultsOfDelayMeasurement(){
		return (HashMap<String, Double>) mData;
	}

	@Override
	public String toString() {
		return "StatisticsResultMessage [mData=" + (HashMap<String, Double>)mData + "]";
	}
	
}