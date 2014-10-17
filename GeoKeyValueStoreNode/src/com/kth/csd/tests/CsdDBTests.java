package com.kth.csd.tests;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.kth.csd.ycsb.CsdDB;
import com.yahoo.ycsb.ByteIterator;

import junit.framework.TestCase;

public class CsdDBTests extends TestCase{
	
	private CsdDB mCsdDb;
	

	@Override
	protected void setUp() throws Exception {
		mCsdDb = new CsdDB();
		super.setUp();
	}
	
	@Test 
	public void testDelete(){
		String arg0 = "arg0";
		String arg1 = "arg1";
		mCsdDb.delete(arg0, arg1);
	}

	@Test
	public void testInsert(){
		String arg0 = "arg0";
		String arg1 = "arg1";
		HashMap<String, ByteIterator> arg3 = Mockito.mock(HashMap.class);
		mCsdDb.insert(arg0, arg1, arg3);
	}
	
	@Test
	public void testRead(){
		String arg0 = "arg0";
		String arg1 = "arg1";
		
		Set<String> arg2 = Mockito.mock(Set.class);
		HashMap<String, ByteIterator> arg3 = Mockito.mock(HashMap.class);
		mCsdDb.read(arg0, arg1, arg2, arg3);
	}
	
	@Test
	public void testScan(){
		String arg0 = "arg0";
		String arg1 = "arg1";
		int arg2 = 2;
		Set<String> arg3 = Mockito.mock(Set.class);
		Vector<HashMap<String, ByteIterator>> arg4 = Mockito.mock(Vector.class);
		mCsdDb.scan(arg0, arg1, arg2, arg3, arg4);
	}
	
	@Test
	public void testUpdate(){
		String arg0 = "arg0";
		String arg1 = "arg1";
		HashMap<String, ByteIterator> arg2 = Mockito.mock(HashMap.class);

		mCsdDb.update(arg0, arg1, arg2);
	}
	

}