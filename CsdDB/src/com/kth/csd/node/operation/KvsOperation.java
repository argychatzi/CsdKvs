package com.kth.csd.node.operation;

import java.net.Socket;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class KvsOperation {

	public enum YCSB_OPERATION {
		READ, WRITE;
	}
	
	@SerializedName("operation")
	protected YCSB_OPERATION mOperation;

	@SerializedName("keyValue")	
	protected KeyValueEntry mKeyValue;
	
	protected Socket mResponseSocket;
	
	public KvsOperation() {
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValueEntry) {
		mOperation = operation;
		mKeyValue = keyValueEntry;
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValue, Socket socket) {
		mOperation = operation;
		mKeyValue = keyValue;
		mResponseSocket = socket;
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

	public Socket getCommunicationSocket() {
		return mResponseSocket;
	}

	@Override
	public String toString() {
		return "RequestOperation [operation=" + mOperation + ", key=" + mKeyValue.getKey()
				+ ", value=" + mKeyValue.getValues() + "]";
	}
	
}
