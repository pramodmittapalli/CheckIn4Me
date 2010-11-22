package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import android.content.SharedPreferences;
import android.util.Log;

public class GowallaAPIAdapter implements APIAdapter
{
	private static final String TAG = "GowallaAPIAdapter";
	private Properties config;
	private ArrayList<Locale> latest_locations;
	
	/**
	 * GowallaAPIAdapter
	 */
	public GowallaAPIAdapter(Properties config)
	{
		this.config = config;
		latest_locations = new ArrayList<Locale>();
	}
	
	/**
	 * getLocationThread
	 * 
	 * @return LocationThread
	 */
	public Runnable getLocationThread(String longitude, String latitude, SharedPreferences settings)
	{
		return new LocationThread(longitude, latitude);
	}
	
	/**
	 * getLatestLocations
	 * 
	 * @return ArrayList<Locale>
	 */
	public ArrayList<Locale> getLatestLocations()
	{
		return latest_locations;
	}
	
	/**
	 * LocationThread
	 * 
	 * @author david
	 */
	class LocationThread implements Runnable
	{
		private String longitude;
		private String latitude;
		
		/**
		 * LocationThread
		 * 
		 * @param longitude
		 * @param latitude
		 */
		LocationThread(String longitude, String latitude)
		{
			this.longitude = longitude;
			this.latitude = latitude;
		}

		/**
		 * run
		 */
		public void run() 
		{
			Log.i(TAG, "Retrieving Gowalla Locations");

			// build new http request
			HTTPRequest request = new HTTPRequest(
				config.getProperty("api_http_method"), config.getProperty("api_host"), 
				config.getProperty("api_locations_endpoint"));
			
			// set request headers
			request.addHeader("X-Gowalla-API-Key", config.getProperty("oauth_client_id"));
			request.addHeader("Accept", "application/" + config.getProperty("api_format"));
			
			// set query parameters
			request.addQueryParameter("lat", latitude);
			request.addQueryParameter("lng", longitude);
			request.addQueryParameter("radius", "50");
			
			// execute http request
			OAuthResponse response = (OAuthResponse)request.execute();
			
			// save locations
			if (response.getSuccessStatus())
				setLocationsFromJson(response.getResponseString());	
		}
		
		/**
		 * setLocationsFromJson
		 * 
		 * @param json
		 */
		private void setLocationsFromJson(String json)
		{
			latest_locations.clear();
			
			// store locations here
		}
	}
}