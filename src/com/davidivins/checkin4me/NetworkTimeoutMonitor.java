package com.davidivins.checkin4me;

import android.os.Handler;
import android.os.SystemClock;

/**
 * NetworkTimeoutMonitor
 * 
 * @author david
 */
public class NetworkTimeoutMonitor implements Runnable
{
	private static final int FIFTHTEEN_SECONDS = 15000;
	private NetworkTimeoutListener activity;
	private Handler handler;
	 
	/**
	 * NetworkTimeoutMonitor
	 * 
	 * @param NetworkTimeoutListener
	 */
	NetworkTimeoutMonitor(NetworkTimeoutListener activity, Handler handler)
	{
		this.activity = activity;
		this.handler = handler;
	}
	
	/**
	 * run
	 */
	public void run()
	{
		SystemClock.sleep(FIFTHTEEN_SECONDS);
		handler.post(activity.getNetworkTimeoutCallback());
	}
}