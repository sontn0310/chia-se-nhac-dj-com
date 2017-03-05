package com.bteam.sharedj.listeners;


import com.bteam.sharedj.models.Data;

public interface INetworkCallback {
	public void onLoadStart();
	public void onResultData(Data result);

	public void onErrorData();

}
