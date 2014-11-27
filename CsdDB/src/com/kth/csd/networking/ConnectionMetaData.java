package com.kth.csd.networking;

public class ConnectionMetaData {

	private String host;
	private int port;
	
	public ConnectionMetaData(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	
}
