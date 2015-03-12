package com.kth.csd.node.operation;

import java.io.Serializable;
import java.util.HashMap;

public class KvsOperation implements Serializable {

	private static final long serialVersionUID = -5066643976863368509L;

	public enum YCSB_OPERATION {
		READ, WRITE;
	}
	
	protected YCSB_OPERATION mOperation;

	protected KeyValueEntry mKeyValue;
	
	protected int result;
	
	public KvsOperation() {
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValueEntry) {
		mOperation = operation;
		mKeyValue = keyValueEntry;
	}
	
	public KeyValueEntry getKeyValue() {
		return mKeyValue;
	}

	public YCSB_OPERATION getYcsbOperationType() {
		return mOperation;
	}

	public String getKey() {
		return mKeyValue.getKey();
	}

	public HashMap<String, String> getValue() {
		return mKeyValue.getValues();
	}
	
	@Override
	public String toString() {
		return "RequestOperation [operation=" + mOperation + ", key=" + mKeyValue.getKey()
				+ ", value=" + mKeyValue.getValues() + "]";
	}

}
