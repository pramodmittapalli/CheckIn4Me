package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * FoursquareAPIAdapter
 * 
 * @author david ivins
 */
public class FoursquareAPIAdapter implements APIAdapter
{
	private static final String TAG = "FoursquareAPIAdapter";
	
	private int service_id;
	private Properties config;
	
	private ArrayList<Locale> latest_locations;
	private boolean latest_checkin_status;
	
	/**
	 * FoursquareAPIAdapter
	 * 
	 * @param Properties config
	 * @param int service_id
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
			this.query     = query;
			this.longitude = longitude;
			this.latitude  = latitude;
			this.settings  = settings;
		}

		/**
		 * run
		 */
		public void run() 
		{
			Log.i(TAG, "Retrieving Foursquare Locations");

			// build new oauth request
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("api_http_method"), config.getProperty("api_host"), 
					config.getProperty("api_version") + config.getProperty("api_locations_endpoint"));
			
			// set request headers
			request.addHeader("User-Agent", "CheckIn4Me:2.0");  // TODO: set this from meta-data
			
			// set query parameters
			if (query != null)
				request.addQueryParameter("query", query);
			request.addQueryParameter("ll", latitude + "," + longitude);
			request.addQueryParameter("l", "50");
			request.addQueryParameter("oauth_token", settings.getString("foursquare_oauth_token_secret", "FOURSQUARE_ACCESS_TOKEN_HERE"));
			
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
			Log.i(TAG, "json_string = " + json_string);
			if (null != query) Log.i(TAG, "query = " + query);
			
			latest_locations.clear();
			String type = "nearby";
			
			// if a query exists, look for the "places" group instead of "nearby"
			if (query != null)
				type = "places";
				
			try 
			{
				// get the json response string as a json object
				JSONObject full_response = new JSONObject(json_string);
				JSONObject response = full_response.getJSONObject("response");
				JSONArray groups = response.getJSONArray("groups");
				
				// loop through groups and find the group that is either the query results or the nearby places
				for (int i = 0; i < groups.length(); i++)
				{
					JSONObject current_object = groups.getJSONObject(i);
					
					// check the type of the current group
					if (current_object.getString("type").equals(type))
					{
						// get this group's venues
						JSONArray venues = current_object.getJSONArray("items");
						
						// store each venue as a new locale
						for (int j = 0; j < venues.length(); j++)
						{
							// get venue information
							JSONObject venue = venues.getJSONObject(j);
							
							String venue_id    = venue.getString("id");
							String name        = venue.getString("name");
							String description = "";
							
							// get venue location information
							JSONObject venue_location = venue.getJSONObject("location");
							
							String latitude  = venue_location.getString("lat");
							String longitude = venue_location.getString("lng");
							
							String address   = (venue_location.has("address")) ? venue_location.getString("address") : "";
							//String cross_street   = (venue_location.has("crossStreet")) ? venue_location.getString("crossStreet") : "";
							String city      = (venue_location.has("city")) ? venue_location.getString("city") : "";
							String state     = (venue_location.has("state")) ? venue_location.getString("state") : "";
							String zip       = (venue_location.has("postalCode")) ? venue_location.getString("postalCode") : "";
							//String distance   = (venue_location.has("distance")) ? venue_location.getString("distance") : "";
							//String country   = (venue_location.has("country")) ? venue_location.getString("country") : "";

							// create a new locale object with the venue's data
							Locale location = new Locale(name, description, longitude, latitude,
									address, city, state, zip);
							location.mapServiceIdToLocationId(service_id, venue_id);
							
							// add the new locale to the latest locations list
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
					config.getProperty("api_checkin_http_method"), config.getProperty("api_host"), 
					config.getProperty("api_version") + config.getProperty("api_checkin_endpoint"));
			
			// set request headers
			request.addHeader("User-Agent", "CheckIn4Me:2.0"); // TODO: get this from meta-data 
			
			// set query parameters
			request.addQueryParameter("oauth_token", 
					settings.getString("foursquare_oauth_token_secret", "FOURSQUARE_ACCESS_TOKEN_HERE"));
			
			HashMap<Integer, String> service_id_location_id_xref = location.getServiceIdToLocationIdMap();
			String vid = service_id_location_id_xref.get(service_id);
			
			request.addQueryParameter("venueId", vid);
			request.addQueryParameter("ll", settings.getString("current_latitude", "CURRENT_LATITUDE_HERE") + "," +
					 settings.getString("current_longitude", "CURRENT_LONGITUDE_HERE"));
			request.addQueryParameter("broadcast", "public");
			
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
			Log.i(TAG, "json_string = " + json_string);
			
			latest_checkin_status = false;
			
			try 
			{
				// get the json response string as a json object
				JSONObject full_response = new JSONObject(json_string);
				JSONObject response = full_response.getJSONObject("response");
				JSONObject checkin_info = response.getJSONObject("checkin");
				
				// get checkin status from fields returned in json response
				latest_checkin_status = checkin_info.has("id") && checkin_info.has("createdAt");
			} 
			catch (JSONException e) 
			{
				Log.i(TAG, "JSON Exception: " + e.getMessage());
				Log.i(TAG, "Could not parse json response: " + json_string);
			}
		}
	}
}
