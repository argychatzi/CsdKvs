package com.kth.csd.tests;

import org.junit.Test;

import com.kth.csd.utils.Logger;

import junit.framework.TestCase;

public class LoggerTests extends TestCase{
	
	private String TAG = "LoggerTests";
	
	@Test
	public void testDebugLoggingOnTerminal(){
		Logger.d(TAG, "testDebugLoggingOnTerminal");
	}
	
	@Test
	public void testDebugLoggingOnFile(){
		Logger.df(TAG, "testDebugLoggingOnFile");
	}

	@Test
	public void testDebugLoggingOnFileMoreThanOneLogs(){
		Logger.df(TAG, "testDebugLoggingOnFileMoreThanOneLogs");
		Logger.df(TAG, "testDebugLoggingOnFileMoreThanOneLogs2");
		Logger.df(TAG, "testDebugLoggingOnFileMoreThanOneLogs3");
		Logger.df(TAG, "testDebugLoggingOnFileMoreThanOneLogs4");
	}
	
	@Test
	public void testDebugLoggingOnTerminalNullTag(){
		Logger.d(null, "testDebugLoggingOnTerminalNullTag");
	}

	@Test
	public void testDebugLoggingOnTerminalNullMessage(){
		Logger.d(TAG, null);
	}

	@Test
	public void testErrorLoggingOnTerminal(){
		Logger.e(TAG, "testErrorLoggingOnTerminal");
	}

	@Test
	public void testErrorLoggingOnTerminalWithNullTag(){
		Logger.e(null, "testErrorLoggingOnTerminalWithNullTag");
	}

	@Test
	public void testErrorLoggingOnTerminalWithNullMessage(){
		Logger.e(TAG, null);
	}

}
