package com.kth.csd.node.executors;

import java.util.HashMap;

import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.OperationReadMessage;
import com.kth.csd.node.core.ApplicationContext;
import com.kth.csd.node.core.KeyValueStore;
import com.kth.csd.node.operation.KeyValueEntry;
import com.kth.csd.node.operation.KvsOperation;
import com.kth.csd.utils.Logger;

public class KvsReader extends KvsOperation implements KvsExecutable {

	
	private static final long serialVersionUID = -8712103519172890713L;
	private static final String TAG = KvsReader.class.getCanonicalName();
	
	public KvsReader(KeyValueEntry keyValueEntry) {
		super(YCSB_OPERATION.READ, keyValueEntry);
	}

	@Override
	public AbstractNetworkMessage execute() {
		Logger.d(TAG, "executing ...");
		KeyValueStore keyValueStore = ApplicationContext.getKeyValueStore();
		//HashMap<String,String> hashMap = keyValueStore.get(mKeyValue);
		
		//The argument that should have been passed to the get method of the keyvaluestore
		//was just the key not the whole mkeyvalue. Actually this was the point that 
		//makes every read operation unsuccessfull even if the entry is already in the store.
		HashMap<String,String> hashMap = keyValueStore.get(mKeyValue.getKey());
		mKeyValue.getValues().putAll(hashMap);
		Logger.d(TAG, "The keyValue read is: "+mKeyValue);
		return new OperationReadMessage(mKeyValue);
	}
}
