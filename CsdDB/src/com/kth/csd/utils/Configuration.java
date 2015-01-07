package com.kth.csd.utils;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.kth.csd.networking.ConnectionMetaData;

public class Configuration {
	
	@SerializedName("masterInternal")
	private ConnectionMetaData masterInternalConnectionMetaData;
	
	@SerializedName("masterExternal")
	private ConnectionMetaData masterExternalConnectionMetaData;
	
	@SerializedName("ownInternal")
	private ConnectionMetaData ownInternalConnectionMetaData;
	
	@SerializedName("ownExternal")
	private ConnectionMetaData ownExternalConnectionMetaData;
	
	@SerializedName("nodes")
	private ArrayList<ConnectionMetaData> nodesInFarm;

	public ConnectionMetaData getMasterInternalConnectionMetaData() {
		return masterInternalConnectionMetaData;
	}

	public void setMasterInternalConnectionMetaData(
			ConnectionMetaData masterInternalConnectionMetaData) {
		this.masterInternalConnectionMetaData = masterInternalConnectionMetaData;
	}

	public ConnectionMetaData getMasterExternalConnectionMetaData() {
		return masterExternalConnectionMetaData;
	}

	public void setMasterExternalConnectionMetaData(
			ConnectionMetaData masterExternalConnectionMetaData) {
		this.masterExternalConnectionMetaData = masterExternalConnectionMetaData;
	}

	public ConnectionMetaData getOwnInternalConnectionMetaData() {
		return ownInternalConnectionMetaData;
	}

	public void setOwnInternalConnectionMetaData(
			ConnectionMetaData ownInternalConnectionMetaData) {
		this.ownInternalConnectionMetaData = ownInternalConnectionMetaData;
	}

	public ConnectionMetaData getOwnExternalConnectionMetaData() {
		return ownExternalConnectionMetaData;
	}

	public void setOwnExternalConnectionMetaData(
			ConnectionMetaData ownExternalConnectionMetaData) {
		this.ownExternalConnectionMetaData = ownExternalConnectionMetaData;
	}

	public ArrayList<ConnectionMetaData> getNodesInFarm() {
		return nodesInFarm;
	}

	public void setNodesInFarm(ArrayList<ConnectionMetaData> nodesInFarm) {
		this.nodesInFarm = nodesInFarm;
	}

	@Override
	public String toString() {
		return "Configuration [masterInternalConnectionMetaData="
				+ masterInternalConnectionMetaData
				+ ", masterExternalConnectionMetaData="
				+ masterExternalConnectionMetaData
				+ ", ownInternalConnectionMetaData="
				+ ownInternalConnectionMetaData
				+ ", ownExternalConnectionMetaData="
				+ ownExternalConnectionMetaData + ", nodesInFarm="
				+ nodesInFarm + "]";
	}
}
