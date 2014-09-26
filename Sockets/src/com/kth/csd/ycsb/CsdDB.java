package com.kth.csd.ycsb;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import com.kth.csd.utils.Logger;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;


public class CsdDB extends DB{

	private static final String TAG = "CsdDB";

	@Override
	public int delete(String arg0, String arg1) {
		Logger.d(TAG, "performing delete");
		return 0;
	}

	@Override
	public int insert(String arg0, String arg1,
			HashMap<String, ByteIterator> arg2) {
		Logger.d(TAG, "performing insert");
		return 0;
	}

	@Override
	public int read(String arg0, String arg1, Set<String> arg2,
			HashMap<String, ByteIterator> arg3) {
		Logger.d(TAG, "performing read");
		return 0;
	}

	@Override
	public int scan(String arg0, String arg1, int arg2, Set<String> arg3,
			Vector<HashMap<String, ByteIterator>> arg4) {
		Logger.d(TAG, "performing scan");
		return 0;
	}

	@Override
	public int update(String arg0, String arg1,
			HashMap<String, ByteIterator> arg2) {
		Logger.d(TAG, "performing update");
		return 0;
	}

}
