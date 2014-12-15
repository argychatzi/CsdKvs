package com.kth.csd.networking.messages;

import java.io.Serializable;

public abstract class AbstractNetworkMessage implements Serializable{

	private static final long serialVersionUID = 5241958078953430441L;

	public enum type {
		STATISTICS_REQ, STATISTICS_RES, MASTER_MOVED, OPERATION_READ, OPERATION_WRITE
	}
	
	private type mType;
	
	protected Object mData;
	
	
	public AbstractNetworkMessage(){
		mType = type.STATISTICS_REQ;
	}
	
	public type getType(){
		return mType;
	}
	
	protected AbstractNetworkMessage(type t){
		mType = t;
	}
}
