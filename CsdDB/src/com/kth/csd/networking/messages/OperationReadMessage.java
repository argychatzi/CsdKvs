package com.kth.csd.networking.messages;

import java.io.Serializable;

import com.kth.csd.networking.messages.AbstractNetworkMessage.type;
import com.kth.csd.node.operation.KeyValueEntry;

public class OperationReadMessage extends AbstractNetworkMessage implements Serializable{
	
	private static final long serialVersionUID = 7042169554675531400L;

	public OperationReadMessage(KeyValueEntry entry){
		super(type.OPERATION_READ);
		mData = new KeyValueEntry(entry.getKey(), entry.getValues());
	}
	
	public KeyValueEntry getKeyValueEntry(){
		return (KeyValueEntry) mData;
	}

	@Override
	public String toString() {
		return "OperationReadMessage [mData=" + (KeyValueEntry)mData + "]";
	}
}
