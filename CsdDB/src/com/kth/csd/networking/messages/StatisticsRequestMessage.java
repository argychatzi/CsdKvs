package com.kth.csd.networking.messages;

import java.io.Serializable;
import java.util.ArrayList;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class StatisticsRequestMessage extends AbstractNetworkMessage implements Serializable{
	
	private static final long serialVersionUID = 5531626847933761299L;

	public StatisticsRequestMessage(ArrayList<String> listOfYcsbClients){
		super(type.STATISTICS_REQ);
		mData = new ArrayList<String>(listOfYcsbClients);
	}
	
	public ArrayList<String> getListOfYcsbClients(){
		return (ArrayList<String>) mData;
	}

	@Override
	public String toString() {
		return "StatisticsRequestMessage [mData=" + (ArrayList<String>)mData + "]";
	}
}