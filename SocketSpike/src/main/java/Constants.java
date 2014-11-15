package main.java;

public class Constants {
	
	public static final int DEFAULT_PORT = 4448;
	public static final String DEFAULT_HOST = "localhost"; //"127.0.0.1";

	public static final int NUMBER_OF_EXECUTORS = 1;
	
	public static final int RESULT_CODE_SUCCESS = 0;
	public static final int RESULT_CODE_FAILURE = -1;
	public static final int RESULT_CODE_MASTER_MOVED = 2;
	
	
	public static final String MASTER_MOVED_NEW_IP_KEY = "NEW_IP_KEY";
	public static final String MASTER_MOVED_NEW_PORT_KEY = "NEW_PORT_KEY";
	
	public static final String DATABASE_FILE = "db.txt";
	
}
