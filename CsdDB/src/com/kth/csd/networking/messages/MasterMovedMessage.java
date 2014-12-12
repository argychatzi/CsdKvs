package com.kth.csd.networking.messages;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class MasterMovedMessage extends AbstractNetworkMessage{
	
	private static final long serialVersionUID = 1L;

	public MasterMovedMessage(ConnectionMetaData internal, ConnectionMetaData external ){
		super(type.MASTER_MOVED);
		mData = new MasterConnections(internal, external);
	}
	
	public ConnectionMetaData getNewMasterInternal(){
		return ((MasterConnections) mData).getInternalConnection();
	}
	
	public ConnectionMetaData getNewMasterExternal(){
		return ((MasterConnections) mData).getExternalConnection();
	}
	
	private class MasterConnections{
		private ConnectionMetaData internalConnection;
		private ConnectionMetaData externalConnection;
		
		public MasterConnections(ConnectionMetaData internal, ConnectionMetaData external){
			internalConnection = internal;
			externalConnection = external;
		}

		public ConnectionMetaData getInternalConnection() {
			return internalConnection;
		}

		public ConnectionMetaData getExternalConnection() {
			return externalConnection;
		}
		
	}
	
}
