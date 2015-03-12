package com.kth.csd.node.operation;

import java.io.Serializable;
import java.util.HashMap;

import com.kth.csd.networking.ConnectionMetaData;

public class KeyValueEntry implements Serializable {
	
	private static final long serialVersionUID = 2480599699103140331L;
	private String key;
	private HashMap<String,String> values;
	
	public KeyValueEntry() {
	}
	
	public KeyValueEntry(String key, HashMap<String,String> values) {
		this.key = key;
		this.values = values;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public HashMap<String,String> getValues() {
		if  (values == null ){
			return new HashMap<String, String>();
		} else{
			return values;
		}
	}
	
	public void setValue(HashMap<String,String> values) {
		getValues().clear();
		getValues().putAll(values);
	}

	@Override
	public String toString() {
		return "KeyValueEntry [key=" + key + ", values=" + values + "]";
	}

}