package com.kth.csd.node;

public class Constants {

	//FOR TESTING PURPOSE ONLY
//	public static final int MASTER_DEFAULT_PORT = 4000;
    public static final int INTERNAL_PORT = 4001;
	public static final int EXTERNAL_PORT = 3000;
	public static final int SLAVE_INTERNAL_PORT = 5001;
	public static final String MASTER_IP = "192.168.0.3"; 
	public static final String SLAVE_IP = "192.168.0.4"; 
//  IF IN MASTER MODE
//	public static final int DEFAULT_PORT = MASTER_DEFAULT_PORT;
//	public static final int INTERNAL_PORT = MASTER_INTERNAL_PORT;
	//IF IN SLAVE MODE
	//public static final int DEFAULT_PORT = SLAVE_DEFAULT_PORT;
	//public static final int INTERNAL_PORT = SLAVE_INTERNAL_PORT;
	//public static final String DEFAULT_HOST = SLAVE_IP; //"127.0.0.1";

	public static final int NUMBER_OF_EXECUTORS = 1;
	
	public static final int RESULT_CODE_SUCCESS = 0;
	public static final int RESULT_CODE_FAILURE = -1;
	public static final int RESULT_CODE_MASTER_MOVED = 2;
	
	
	public static final String MASTER_MOVED_HOST_IP_KEY = "MASTER_MOVED_HOST_IP_KEY";
	public static final String MASTER_MOVED_HOST_PORT_KEY = "MASTER_MOVED_HOST_PORT_KEY";
	public static final String MASTER_MOVED_KEY = "MASTER_MOVED_KEY";

	public static final String DATABASE_FILE = "db.txt";
	public static final long FLUSH_TO_DISK_PERIOD = 10000;
	
	
  	
}
