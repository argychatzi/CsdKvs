package main.java;


public class KeyValueStore {
	
	private KVSConnection mMasterConnection;
	private KVSConnection mHostConnection;

	public int read(KeyValueEntry entry){
		return mHostConnection.sendMessage(entry.toString());
	}
	
	public int write(KeyValueEntry entry){
		return mMasterConnection.sendMessage(entry.toString());
	}
}
