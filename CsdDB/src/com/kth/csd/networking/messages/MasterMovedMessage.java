package com.kth.csd.networking.messages;

import java.io.Serializable;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;

public class MasterMovedMessage extends AbstractNetworkMessage implements Serializable{
	
	private static final long serialVersionUID = 7041586755481399997L;

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
	
	private class MasterConnections implements Serializable{
		private static final long serialVersionUID = -5775109155326515762L;

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

		@Override
		public String toString() {
			return "MasterConnections [internalConnection="
					+ internalConnection + ", externalConnection="
					+ externalConnection + "]";
		}
	}

	@Override
	public String toString() {
		return "MasterMovedMessage [mData=" + (MasterConnections)mData + "]";
	}
}
