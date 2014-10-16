package com.kth.csd.node.operation;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class KvsOperation {

	public enum YCSB_OPERATION {
		READ, WRITE;
	}

	@SerializedName("operation")
	protected YCSB_OPERATION mOperation;

	@SerializedName("key")
	protected String mKey;

	@SerializedName("value")
	protected HashMap<String, String> mValue;

	public KvsOperation(YCSB_OPERATION operation, String key, HashMap<String, String> value) {
		mOperation = operation;
		mKey = key;
		mValue = value;
	}

	public YCSB_OPERATION getYcsbOperationType() {
		return mOperation;
	}

	public String getKey() {
		return mKey;
	}

	public HashMap<String, String> getValue() {
		return mValue;
	}

	@Override
	public String toString() {
		return "RequestOperation [operation=" + mOperation + ", key=" + mKey
				+ ", value=" + mValue + "]";
	}
}
