package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class GowallaAPIAdapter implements APIAdapter
{
	private static final String TAG = "GowallaAPIAdapter";
	private Properties config;
	private ArrayList<Locale> latest_locations;
	private int service_id;
	
	/**
	 * GowallaAPIAdapter
	 */
	public GowallaAPIAdapter(Properties config, int service_id)
	{
		Log.i(TAG, "service_id = " + service_id);
		this.config = config;
		this.service_id = service_id;
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
		 * @throws JSONException 
		 */
		private void setLocationsFromJson(String json_string)
		{
			// clear locations
			latest_locations.clear();
			
			try 
			{
				JSONObject json = new JSONObject(json_string);
				JSONArray spots = json.getJSONArray("spots");
				
				for (int i = 0; i < spots.length(); i++)
				{
					JSONObject spot = spots.getJSONObject(i);
					
					String name = spot.getString("name");
					String description = spot.getString("description");
					String checkins_url = spot.getString("checkins_url");
					String longitude = spot.getString("lng");
					String latitude = spot.getString("lat");
					
					JSONObject addr = spot.getJSONObject("address");
					String city = addr.getString("locality");
					String state = addr.getString("region");
					
					String address = "";
					String zip = "";
					
					String[] temp = checkins_url.split("\\?");
					String[] spot_id_key_value = temp[1].split("\\=");
					String spot_id = spot_id_key_value[1];
					
					Locale location = new Locale(name, description, longitude, latitude, 
							address, city, state, zip);
					location.mapServiceIdToLocationId(service_id, spot_id);
					latest_locations.add(location);
				}
			} 
			catch (JSONException e) 
			{
				Log.e(TAG, "JSON Exception: " + e.getMessage());
				Log.e(TAG, "Could not parse json response: " + json_string);
			}
		}
	}
}