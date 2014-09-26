package com.kth.csd.tests;

import org.junit.Test;

import com.kth.csd.ycsb.CsdDB;

import junit.framework.TestCase;

public class YcsbTests extends TestCase{
	
	private CsdDB mCsdDb;
	
	@Test
	public void testRead(){
		mCsdDb = new CsdDB();
		
		String arg0 = "arg0";
		String arg1 = "arg1";
		
		Set<String> arg2 = new
		
		mCsdDb.read("arg0", "arg1", "arg2", "arg3");
		
	}

}
