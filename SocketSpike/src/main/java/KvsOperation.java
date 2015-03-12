package main.java;

import java.net.Socket;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.kth.csd.networking.KVSConnection;

public class KvsOperation {

	public enum YCSB_OPERATION {
		READ, WRITE;
	}
	
	@SerializedName("operation")
	protected YCSB_OPERATION mOperation;

	@SerializedName("keyValue")	
	protected KeyValueEntry mKeyValue;
	
	protected KVSConnection mConnection;
	
	public KvsOperation() {
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValueEntry) {
		mOperation = operation;
		mKeyValue = keyValueEntry;
	}
	
	public KvsOperation(YCSB_OPERATION operation, KeyValueEntry keyValue, KVSConnection connection) {
		mOperation = operation;
		mKeyValue = keyValue;
		mConnection = connection;
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
	
	public void setConnecitonPoint(KVSConnection connection){
		mConnection = connection;
	}

	public KVSConnection getCommunicationPoint() {
		return mConnection;
	}

	@Override
	public String toString() {
		return "RequestOperation [operation=" + mOperation + ", key=" + mKeyValue.getKey()
				+ ", value=" + mKeyValue.getValues() + "]";
	}
	
	
}
