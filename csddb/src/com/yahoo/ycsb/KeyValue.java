

package com.yahoo.ycsb;

import java.util.HashMap;

public class KeyValue {
	
	private String key;
	private HashMap<String,String> values;
	public KeyValue(String key, HashMap<String, String> values) {
		super();
		this.key = key;
		this.values = values;
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public HashMap<String, String> getValues() {
		return values;
	}
	public void setValues(HashMap<String, String> values) {
		this.values = values;
	}



	
	
	

	

}
