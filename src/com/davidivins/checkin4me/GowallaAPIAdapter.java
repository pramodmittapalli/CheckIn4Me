package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import android.content.SharedPreferences;

public class GowallaAPIAdapter implements APIAdapter
{
	private static final String TAG = "GowallaAPIAdapter";
	private Properties config;
	
	/**
	 * GowallaAPIAdapter
	 */
	public GowallaAPIAdapter(Properties config)
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