package com.kth.csd.networking.messages;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;
import com.kth.csd.node.operation.KeyValueEntry;

public class OperationReadMessage extends AbstractNetworkMessage{
	
	public OperationReadMessage(KeyValueEntry entry){
		super(type.OPERATION_READ);
		mData = new KeyValueEntry(entry.getKey(), entry.getValues());
	}
	
	public KeyValueEntry getKeyValueEntry(){
		return (KeyValueEntry) mData;
	}
}
