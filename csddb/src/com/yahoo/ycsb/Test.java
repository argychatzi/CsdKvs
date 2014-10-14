package com.yahoo.ycsb;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {

	public static void main(String [ ] args){
		try{
			CsdDB db = new CsdDB();
			db.init();
			
			for (int i=0; i<20; i++){
				String key = "key_" + i;
				int result = db.insert(null, key, null);
				System.out.println("Insert: " + key + " result: " + result);
			}
			
			int result = db.read(null, "key_-1", null, null);
			System.out.println("Read: key_-1 result: " + result);

			result = db.read(null, "key_5", null, null);
			System.out.println("Read: key_5 result: " + result);
			
		} catch (FileNotFoundException e){
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
