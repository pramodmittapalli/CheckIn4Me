package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class FoursquareAPIAdapter implements APIAdapter
{
	private static final String TAG = "FoursquareAPIAdapter";
	
	private int service_id;
	private Properties config;
	
	private ArrayList<Locale> latest_locations;
	private boolean latest_checkin_status;
	
	/**
	 * FoursquareAPIAdapter
	 */
	public FoursquareAPIAdapter(Properties config, int service_id)
	{
		this.config = config;
		this.service_id = service_id;
		latest_locations = new ArrayList<Locale>();
		latest_checkin_status = false;
	}
	
	/**
	 * getLocationThread
	 * 
	 * @param query
	 * @param longitude
	 * @param latitude
	 * @param settings
	 * @return LocationThread
	 */
	public Runnable getLocationThread(String query, String longitude, String latitude, SharedPreferences settings)
	{
		return new LocationThread(query, longitude, latitude, settings);
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
	 * getLatestCheckInStatus
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
		private String query;
		private String longitude;
		private String latitude;
		private SharedPreferences settings;
		
		/**
		 * LocationThread
		 * 
		 * @param query
		 * @param longitude
		 * @param latitude
		 */
		LocationThread(String query, String longitude, String latitude, SharedPreferences settings)
		{
			this.query = query;
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

			// build new oauth request
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("oauth_consumer_secret", "-1") + "&" + settings.getString("foursquare_oauth_token_secret", "-1"),
					config.getProperty("api_http_method"), config.getProperty("api_host"), 
					config.getProperty("api_version") + config.getProperty("api_locations_endpoint") + 
					"." + config.getProperty("api_data_format"));
			
			// set request headers
			request.addHeader("User-Agent", "CheckIn4Me:1.0");
			
			// set query parameters
			if (query != null)
				request.addQueryParameter("q", query);
			request.addQueryParameter("oauth_consumer_key", config.getProperty("oauth_consumer_key"));
			request.addQueryParameter("oauth_nonce", request.generateNonce());
			request.addQueryParameter("oauth_signature_method", config.getProperty("oauth_signature_method"));
			request.addQueryParameter("oauth_token", settings.getString("foursquare_oauth_token", "-1"));
			request.addQueryParameter("oauth_timestamp", request.generateTimestamp());
			request.addQueryParameter("oauth_version", config.getProperty("oauth_version"));
			request.addQueryParameter("geolat", latitude);
			request.addQueryParameter("geolong", longitude);
			request.addQueryParameter("l", "50");
			
			// execute http request
			OAuthResponse response = (OAuthResponse)request.execute();
			
			// save locations
			if (response.getSuccessStatus())
				setLocationsFromJson(response.getResponseString(), query);	
		}
		
		/**
		 * setLocationsFromJson
		 * 
		 * @param json
		 */
		private void setLocationsFromJson(String json_string, String query)
		{
			latest_locations.clear();
			String type = "Nearby";
			
			if (query != null)
				type = "Matching Places";
				
			try 
			{
				JSONObject json = new JSONObject(json_string);
				JSONArray groups = json.getJSONArray("groups");
				
				for (int i = 0; i < groups.length(); i++)
				{
					JSONObject group = groups.getJSONObject(i);
					
					if (group.getString("type").equals(type))
					{
						JSONArray venues = group.getJSONArray("venues");
						
						for (int j = 0; j < venues.length(); j++)
						{
							JSONObject venue = venues.getJSONObject(j);
							
							String venue_id = venue.getString("id");
							String name = venue.getString("name");
							String description = "";
							String longitude = venue.getString("geolong");
							String latitude = venue.getString("geolat");
							String address = venue.getString("address");
							String city = venue.getString("city");
							String state = venue.getString("state");
							String zip = "";
							
							Locale location = new Locale(name, description, longitude, latitude,
									address, city, state, zip);
							location.mapServiceIdToLocationId(service_id, venue_id);
							latest_locations.add(location);	
						}
						
						break;
					}
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
	 * CheckInThread
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
			Log.i(TAG, "Checking in on Foursquare");

			// build new oauth request
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("oauth_consumer_secret", "-1") + "&" + settings.getString("foursquare_oauth_token_secret", "-1"),
					config.getProperty("api_checkin_http_method"), config.getProperty("api_host"), 
					config.getProperty("api_version") + config.getProperty("api_checkin_endpoint") + 
					"." + config.getProperty("api_data_format"));
			
			// set request headers
			request.addHeader("User-Agent", "CheckIn4Me:1.0");
			
			// set query parameters
			request.addQueryParameter("oauth_consumer_key", config.getProperty("oauth_consumer_key"));
			request.addQueryParameter("oauth_nonce", request.generateNonce());
			request.addQueryParameter("oauth_signature_method", config.getProperty("oauth_signature_method"));
			request.addQueryParameter("oauth_token", settings.getString("foursquare_oauth_token", "-1"));
			request.addQueryParameter("oauth_timestamp", request.generateTimestamp());
			request.addQueryParameter("oauth_version", config.getProperty("oauth_version"));
			
			HashMap<Integer, String> service_id_location_id_xref = location.getServiceIdToLocationIdMap();
			String vid = service_id_location_id_xref.get(service_id);
			
			request.addQueryParameter("vid", vid);
			request.addQueryParameter("private", "0");
			request.addQueryParameter("geolat", settings.getString("current_latitude", "-1"));
			request.addQueryParameter("geolong", settings.getString("current_longitude", "-1"));
			
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
		private void setLocationsFromJson(String json_string)
		{
			latest_checkin_status = false;
			
			try 
			{
				JSONObject json = new JSONObject(json_string);
				JSONObject checkin = json.getJSONObject("checkin");
				
				@SuppressWarnings("unused")
				String created = checkin.getString("created");
				
				// if we find a created at check-in time/date without throwing an exception, check-in was successful
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
