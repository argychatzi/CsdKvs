package com.kth.csd.node.core;

import com.kth.csd.networking.ConnectionMetaData;
import com.kth.csd.networking.messages.MasterMovedMessage;
import com.kth.csd.utils.Logger;

public class MasterSelector {
	
	private static final String TAG = MasterSelector.class.getCanonicalName();

	public static void notifyMasterChanged(MasterMovedMessage masterData){
		ApplicationContext.getNodes().broadCast(masterData);
	}
}
 