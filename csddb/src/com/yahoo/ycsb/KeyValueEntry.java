package com.yahoo.ycsb;

import java.util.HashMap;

public class KeyValueEntry {
	
	private String key;
	private HashMap<String,ByteIterator> values;
	
	public KeyValueEntry(String key, HashMap<String,ByteIterator> values) {
		this.key = key;
		this.values = values;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public HashMap<String,ByteIterator> getValues() {
		return values;
	}
	public void setValue(HashMap<String,ByteIterator> values) {
		this.values = values;
	}
}
