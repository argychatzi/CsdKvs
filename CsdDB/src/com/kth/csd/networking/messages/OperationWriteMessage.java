package com.kth.csd.networking.messages;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;
import com.kth.csd.node.operation.KeyValueEntry;

public class OperationWriteMessage extends AbstractNetworkMessage{
	
	public OperationWriteMessage(KeyValueEntry entry){
		super(type.OPERATION_WRITE);
		mData = new KeyValueEntry(entry.getKey(), entry.getValues());
	}
	
	public KeyValueEntry getKeyValueEntry(){
		return (KeyValueEntry) mData;
	}
}
