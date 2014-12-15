package com.kth.csd.networking.messages;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;
import com.kth.csd.node.operation.KeyValueEntry;

public class OperationWriteMessage extends AbstractNetworkMessage{
	
	private static final long serialVersionUID = 7688236116746219015L;

	public OperationWriteMessage(KeyValueEntry entry){
		super(type.OPERATION_WRITE);
		mData = new KeyValueEntry(entry.getKey(), entry.getValues());
	}
	
	public KeyValueEntry getKeyValueEntry(){
		return (KeyValueEntry) mData;
	}

	@Override
	public String toString() {
		return "OperationWriteMessage [mData=" + (KeyValueEntry)mData + "]";
	}
}
