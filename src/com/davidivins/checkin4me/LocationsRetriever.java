package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;

/**
 * LocationsThread
 * 
 * @author david
 */
class LocationsRetriever implements Runnable
{
	private Activity activity;
	private LocationsRetrieverListener listener;
	private Handler handler;
	private String query;
	private String longitude;
	private String latitude;
	private SharedPreferences settings;
	private ArrayList<Locale> locations_retrieved;
	
	/**
	 * LocationsThread
	 * 
	 * @param activity
	 * @param query
	 * @param longitude
	 * @param latitude
	 * @param settings
	 */
	LocationsRetriever(Activity activity, LocationsRetrieverListener listener, Handler handler, String query, String longitude, String latitude, SharedPreferences settings)
	{
		this.activity = activity;
		this.listener = listener;
		this.handler = handler;
		this.query = query;
		this.longitude = longitude;
		this.latitude = latitude;
		this.settings = settings;
		
		this.locations_retrieved = new ArrayList<Locale>();
	}
	
	/**
	 * run
	 */
	public void run() 
	{
		locations_retrieved = Services.getInstance(activity).getAllLocations(query, longitude, latitude, settings);
		
		if (null != handler)
			handler.post(listener.getLocationsRetrievedCallback());
	}
	
	/**
	 * destroyHandler
	 */
	public void destroyHandler()
	{
		handler = null;
	}
	
	/**
	 * getThreadName
	 * 
	 * @return String
	 */
	public String getThreadName()
	{
		return "LocationsThread";
	}
	
	/**
	 * getLocations
	 * 
	 * @return ArrayList<Locale>
	 */
	public ArrayList<Locale> getLocationsRetrieved()
	{
		return locations_retrieved;
	}
}