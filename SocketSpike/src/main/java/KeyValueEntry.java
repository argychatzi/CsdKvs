package main.java;

import java.util.HashMap;

import com.yahoo.ycsb.ByteIterator;

public class KeyValueEntry {
	
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
		return values;
	}
	public void setValue(HashMap<String,String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "KeyValueEntry [key=" + key + ", values=" + values + "]";
	}
	


}