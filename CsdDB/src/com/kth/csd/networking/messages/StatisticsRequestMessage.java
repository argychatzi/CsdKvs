package com.kth.csd.networking.messages;

import java.util.ArrayList;
import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class StatisticsRequestMessage extends AbstractNetworkMessage{
	
	public StatisticsRequestMessage(ArrayList<String> listOfYcsbClients){
		super(type.STATISTICS_REQ);
		mData = new ArrayList<String>(listOfYcsbClients);
	}
	
	public ArrayList<String> getListOfYcsbClients(){
		return (ArrayList<String>) mData;
	}
}