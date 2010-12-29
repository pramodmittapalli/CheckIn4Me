package com.davidivins.checkin4me;

import android.os.Handler;
import android.os.SystemClock;

/**
 * GPSTimeoutMonitor
 * 
 * @author david
 */
public class GPSTimeoutMonitor implements Runnable
{
	private static final int FIFTHTEEN_SECONDS = 15000;
	private GPSTimeoutListener activity;
	private Handler handler;
	 
	/**
	 * GPSTimeoutMonitor
	 * 
	 * @param GPSTimeoutListener
	 */
	GPSTimeoutMonitor(GPSTimeoutListener activity, Handler handler)
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
		
		if (null != handler)
			handler.post(activity.getGPSTimeoutCallback());
	}
	
	/**
	 * destroyHandler
	 */
	public void destroyHandler()
	{
		handler = null;
	}
}
