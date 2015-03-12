package com.kth.csd.networking;

import com.kth.csd.node.operation.KeyValueEntry;

public interface ExecutionResultCommunicator {

	public void excutionFinished(KeyValueEntry entry);
}
