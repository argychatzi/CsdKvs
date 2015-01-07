package com.kth.csd.node.executors;
import com.kth.csd.networking.messages.AbstractNetworkMessage;

public interface KvsExecutable {
		public AbstractNetworkMessage execute();
}