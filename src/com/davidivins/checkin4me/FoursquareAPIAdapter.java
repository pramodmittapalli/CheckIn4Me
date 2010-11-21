package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import android.content.SharedPreferences;

public class FoursquareAPIAdapter implements APIAdapter
{
	private static final String TAG = "FoursquareAPIAdapter";
	private Properties config;
	
	/**
	 * FoursquareAPIAdapter
	 */
	public FoursquareAPIAdapter(Properties config)
	{
		this.config = config;
	}

	/**
	 * getLocations
	 * 
	 * @param String longitude
	 * @param String latitude
	 * @param SharedPreferences settings
	 * @return ArrayList<Locale>
	 */
	public ArrayList<Locale> getLocations(String longitude, String latitude, SharedPreferences settings)
	{
		ArrayList<Locale> locations = new ArrayList<Locale>();
		
		return locations;
	}
}
