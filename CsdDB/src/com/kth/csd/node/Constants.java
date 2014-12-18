package com.kth.csd.node;

public class Constants {
	
	public static final int DEFAULT_EXTERNAL_PORT = 6000;
	public static final int DEFAULT_INTERNAL_PORT = 5000;
	
	public static final int SECONDARY_EXTERNAL_PORT = 5448;
	public static final int SECONDARY_INTERNAL_PORT = 6000;
	
	public static final String DEFAULT_HOST = "localhost"; //"127.0.0.1";

	public static final int NUMBER_OF_EXECUTORS = 1;
	
	public static final int RESULT_CODE_SUCCESS = 0;
	public static final int RESULT_CODE_FAILURE = -1;
	public static final int RESULT_CODE_MASTER_MOVED = 2;
	
	
	public static final String MASTER_MOVED_HOST_IP_KEY = "MASTER_MOVED_HOST_IP_KEY";
	public static final String MASTER_MOVED_HOST_PORT_KEY = "MASTER_MOVED_HOST_PORT_KEY";
	public static final String MASTER_MOVED_KEY = "MASTER_MOVED_KEY";

	public static final long FLUSH_TO_DISK_PERIOD = 10000;
	public static final double ALPHA = 0.9;
	
	
  	
}
