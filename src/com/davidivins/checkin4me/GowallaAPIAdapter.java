package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class GowallaAPIAdapter implements APIAdapter
{
	private static final String TAG = "GowallaAPIAdapter";
	
	private int service_id;
	private Properties config;
	
	private ArrayList<Locale> latest_locations;
	private boolean latest_checkin_status;
	
	/**
	 * GowallaAPIAdapter
	 */
	public GowallaAPIAdapter(Properties config, int service_id)
	{
		Log.i(TAG, "service_id = " + service_id);
		this.config = config;
		this.service_id = service_id;
		latest_locations = new ArrayList<Locale>();
		latest_checkin_status = false;
	}
	
	/**
	 * getLocationThread
	 * 
	 * @param longitude
	 * @param latitude
	 * @param settings
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
	 * getCheckInThread
	 * 
	 * @param location
	 * @param settings
	 * @return CheckInThread
	 */
	public Runnable getCheckInThread(Locale location, SharedPreferences settings)
	{
		latest_checkin_status = false;
		return new CheckInThread(location, settings);
	}
	
	/**
	 * getLatestCheckInStatuse
	 * 
	 * @return boolean
	 */
	public boolean getLatestCheckInStatus()
	{
		return latest_checkin_status;
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
	
	/**
	 * LocationThread
	 * 
	 * @author david
	 */
	class CheckInThread implements Runnable
	{
		private Locale location;
		private SharedPreferences settings;
		
		/**
		 * CheckInThread
		 * 
		 * @param location
		 * @param settings
		 */
		CheckInThread(Locale location, SharedPreferences settings)
		{
			this.location = location;
			this.settings = settings;
		}

		/**
		 * run
		 */
		public void run() 
		{
			Log.i(TAG, "Checking in with Gowalla");

			// build new http request
			HTTPRequest request = new HTTPRequest(
				config.getProperty("oauth_http_method"), config.getProperty("api_secure_host"), 
				config.getProperty("api_checkin_endpoint"));
			
			// set request headers
			request.addHeader("X-Gowalla-API-Key", config.getProperty("oauth_client_id"));
			request.addHeader("Accept", "application/" + config.getProperty("api_format"));
			
			HashMap<Integer, String> service_id_location_id_xref = location.getServiceIdToLocationIdMap();
			String spot_id = service_id_location_id_xref.get(service_id);
			
			// set query parameters
			request.addQueryParameter("oauth_token", settings.getString("gowalla_access_token", "-1"));
			request.addQueryParameter("spot_id", spot_id);
			request.addQueryParameter("comment", "");
			request.addQueryParameter("lat", settings.getString("current_latitude", "-1"));
			request.addQueryParameter("lng", settings.getString("current_longitude", "-1"));
			request.addQueryParameter("post_to_twitter", "0");
			request.addQueryParameter("post_to_facebook", "0");
			
			// execute http request
			OAuthResponse response = (OAuthResponse)request.execute();
			
			// save locations
			if (response.getSuccessStatus())
				setStatusFromJson(response.getResponseString());	
		}
		
		/**
		 * setStatusFromJson
		 * 
		 * @param json
		 * @throws JSONException 
		 */
		private void setStatusFromJson(String json_string)
		{
			// clear latest status
			latest_checkin_status = false;
			
			try 
			{
				JSONObject json = new JSONObject(json_string);
				
				@SuppressWarnings("unused")
				String created_at = json.getString("created_at");
				
				// if we found a created_at time/date and didn't throw an exception, check-in succeeded
				latest_checkin_status = true;
			} 
			catch (JSONException e) 
			{
				Log.i(TAG, "JSON Exception: " + e.getMessage());
				Log.i(TAG, "Could not parse json response: " + json_string);
			}
		}
	}
}