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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mData == null) ? 0 : mData.hashCode());
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNetworkMessage other = (AbstractNetworkMessage) obj;
		if (mData == null) {
			if (other.mData != null)
				return false;
		} else if (!mData.equals(other.mData))
			return false;
		if (mType != other.mType)
			return false;
		return true;
	}
}
