package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * Analytics
 * 
 * @author david
 */
public class Analytics 
{
	private static final String TAG = "Analytics";
	private Properties config;
	private GoogleAnalyticsTracker tracker;
	
	/**
	 * Analytics
	 * 
	 * @param activity
	 */
	public Analytics(Activity activity)
	{
		config = new Properties();
		
		try 
		{
			InputStream config_file = activity.getResources().openRawResource(GeneratedResources.getRaw("analytics"));
			config.load(config_file);
			
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.start(config.getProperty("ua_number", "-1"), activity);
			
			Log.i(TAG, "Started tracker for ua_number: " + config.getProperty("ua_number", "-1"));

		} 
		catch (Exception e) 
		{
			Log.e(TAG, "Failed to open config file");
		}
	}
	
	/**
	 * getTracker
	 * 
	 * @return GoogleAnalyticsTracker
	 */
	public GoogleAnalyticsTracker getTracker()
	{	
		return tracker;
	}
}