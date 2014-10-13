package com.yahoo.ycsb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Test {

	public static void main(String [ ] args){
		HashMap<String, ByteIterator> values = new HashMap<String, ByteIterator>();
		ArrayList<Byte> list=new ArrayList<Byte> ();
		String s = new String("value");	
		byte[] b = s.getBytes();
		for(int i=0;i<b.length;i++){
			list.add(b[i]);
		}	
		Iterator<Byte> byteIterator = list.iterator();
		//values.put("key1", byteIterator);
		CSDDB csddb = new CSDDB();		
		csddb.insert("usertable", "csdtest", values);
	}
}
