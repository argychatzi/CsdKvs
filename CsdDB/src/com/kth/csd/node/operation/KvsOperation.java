package com.kth.csd.node.operation;

import java.io.File;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kth.csd.node.Constants;
import com.yahoo.ycsb.ByteIterator;

public class KvsOperation {

	public enum YCSB_OPERATION {
		READ, WRITE;
	}
	
	@SerializedName("operation")
	protected YCSB_OPERATION mOperation;

	@SerializedName("keyValue")	
	protected KeyValueEntry mKeyValue;
	
	public KvsOperation() {
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValue) {
		mOperation = operation;
		mKeyValue = keyValue;
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
