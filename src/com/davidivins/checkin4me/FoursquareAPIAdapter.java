package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import com.davidivins.checkin4me.GowallaAPIAdapter.LocationThread;

import android.content.SharedPreferences;
import android.util.Log;

public class FoursquareAPIAdapter implements APIAdapter
{
	private static final String TAG = "FoursquareAPIAdapter";
	private Properties config;
	ArrayList<Locale> latest_locations;
	
	/**
	 * FoursquareAPIAdapter
	 */
	public FoursquareAPIAdapter(Properties config)
	{
		this.config = config;
		latest_locations = new ArrayList<Locale>();
	}

	/**
	 * getLocations
	 * 
	 * @param String longitude
	 * @param String latitude
	 * @param SharedPreferences settings
	 * @return ArrayList<Locale>
	 */
//	public ArrayList<Locale> getLocations(String longitude, String latitude, SharedPreferences settings)
//	{
//		ArrayList<Locale> locations = new ArrayList<Locale>();
//		
//		return locations;
//	}
	
	/**
	 * getLocationThread
	 * 
	 * @return LocationThread
	 */
	public Runnable getLocationThread(String longitude, String latitude, SharedPreferences settings)
	{
		return new LocationThread(longitude, latitude, settings);
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
		private SharedPreferences settings;
		
		/**
		 * LocationThread
		 * 
		 * @param longitude
		 * @param latitude
		 */
		LocationThread(String longitude, String latitude, SharedPreferences settings)
		{
			this.longitude = longitude;
			this.latitude = latitude;
			this.settings = settings;
		}

		/**
		 * run
		 */
		public void run() 
		{
			Log.i(TAG, "Retrieving Foursquare Locations");

//			// build new http request
//			HTTPRequest request = new HTTPRequest(
//				config.getProperty("api_http_method"), config.getProperty("api_host"), 
//				config.getProperty("api_locations_endpoint"));
//			
//			// set request headers
//			request.addHeader("X-Gowalla-API-Key", config.getProperty("oauth_client_id"));
//			request.addHeader("Accept", "application/" + config.getProperty("api_format"));
//			
//			// set query parameters
//			request.addQueryParameter("lat", latitude);
//			request.addQueryParameter("lng", longitude);
//			request.addQueryParameter("radius", "50");
			
			// execute http request
			//OAuthResponse response = (OAuthResponse)request.execute();
			OAuthResponse response = new OAuthResponse();
			
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
