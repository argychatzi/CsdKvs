package com.kth.csd.networking.interfaces.external;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.KvsClient.YcsbTrafficInputInteraceHolder;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.networking.messages.AbstractNetworkMessage;
import com.kth.csd.networking.messages.AbstractNetworkMessage.type;
import com.kth.csd.utils.Logger;

public class ClientExternalInputInterface extends IoHandlerAdapter {
	
	private static final String TAG = ClientExternalInputInterface.class.getCanonicalName();
	private YcsbTrafficInputInteraceHolder mHolder;
	
	public ClientExternalInputInterface(YcsbTrafficInputInteraceHolder holder) {
		mHolder = holder;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		Logger.d(TAG, "messageReceived ... " + message);
		
		AbstractNetworkMessage response = (AbstractNetworkMessage) message;
		
		if (response.getType() == type.MASTER_MOVED){
			
			Logger.d(TAG, "MASTER_MOVED ... " + message);
				ConnectionMetaData newMasterConnectionMetadata = ((MasterMovedMessage)response).getNewMasterExternal();
				Logger.d(TAG, "master moved to:" + newMasterConnectionMetadata);
				session.close(true);
				mHolder.onMasterNodeMoved(newMasterConnectionMetadata);
		}
		
		super.messageReceived(session, message);
	}
}
