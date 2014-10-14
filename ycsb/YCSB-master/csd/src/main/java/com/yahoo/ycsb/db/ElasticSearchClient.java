package com.yahoo.ycsb.db;

import com.kth.csd.networking.Constants;
import com.kth.csd.networking.RequestSender;
import com.kth.csd.utils.Logger;
import com.yahoo.ycsb.ByteIterator;
import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import com.yahoo.ycsb.StringByteIterator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

public class ElasticSearchClient extends DB {

	private static final String TAG = "ElasticSearchClient";
	private RequestSender mSender;

	@Override
	public void init() throws DBException {
		Logger.d(TAG, "init Called!");
		Properties properties = getProperties();
		properties.list(System.out);
		mSender = new RequestSender();
	}

	@Override
	public void cleanup() throws DBException {
		Logger.d(TAG, "performing cleanup");
	}

	@Override
	public int insert(String table, String key,
		HashMap<String, ByteIterator> values) {
		Logger.d(TAG, "performing insert");

		try {
			mSender.sendRequest(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int delete(String table, String key) {
		Logger.d(TAG, "performing delete");
		return 1;
	}

	@Override
	public int read(String table, String key, Set<String> fields,
		HashMap<String, ByteIterator> result) {
		Logger.d(TAG, "performing read");
		return 1;
	}

	@Override
	public int update(String table, String key,
		HashMap<String, ByteIterator> values) {
		Logger.d(TAG, "performing update");
		return 1;
	}

	@Override
	public int scan(String table, String startkey, int recordcount,
			Set<String> fields, Vector<HashMap<String, ByteIterator>> result) {
		Logger.d(TAG, "performing scan");
		return 1;
	}
}
