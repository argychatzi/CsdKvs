package com.kth.csd.networking.messages;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class MasterMovedMessage extends AbstractNetworkMessage{
	
	public MasterMovedMessage(ConnectionMetaData metaData){
		super(type.MASTER_MOVED);
		mData = new ConnectionMetaData(metaData.getHost(), metaData.getPort());
	}
	
	public ConnectionMetaData getNewMaster(){
		return (ConnectionMetaData) mData;
	}
	
}
