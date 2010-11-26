package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class FoursquareAPIAdapter implements APIAdapter
{
	private static final String TAG = "FoursquareAPIAdapter";
	private Properties config;
	private ArrayList<Locale> latest_locations;
	private int service_id;
	
	/**
	 * FoursquareAPIAdapter
	 */
	public FoursquareAPIAdapter(Properties config, int service_id)
	{
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

			// build new http request
//			HTTPRequest request = new HTTPRequest(
//				config.getProperty("api_http_method"), config.getProperty("api_host"), 
//				config.getProperty("api_version") + config.getProperty("api_locations_endpoint") + 
//				"." + config.getProperty("api_data_format"));
			
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("oauth_consumer_secret", "-1") + "&" + settings.getString("foursquare_oauth_token_secret", "-1"),
					config.getProperty("api_http_method"), config.getProperty("api_host"), 
					config.getProperty("api_version") + config.getProperty("api_locations_endpoint") + 
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
			request.addQueryParameter("geolat", latitude);
			request.addQueryParameter("geolong", longitude);
			request.addQueryParameter("l", "50");
			
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
			latest_locations.clear();
			
			// store locations here
			// {"groups":[{"type":"Trending Now","venues":[{"id":38157,"name":"Jon M. Huntsman Hall - Wharton School","primarycategory":{"id":79002,"fullpathname":"College & Education:Academic Building","nodename":"Academic Building","iconurl":"http://foursquare.com/img/categories/education/default.png"},"address":"3730 Walnut St","crossstreet":"38th St","city":"Philadelphia","state":"PA","zip":"19104","verified":true,"geolat":39.953518,"geolong":-75.1979,"stats":{"herenow":"5"},"phone":"2158983030","twitter":"wharton","hasTodo":"false","distance":375},{"id":1375737,"name":"Van Pelt Library (UPenn)","primarycategory":{"id":79133,"fullpathname":"Home / Work / Other:Library","nodename":"Library","iconurl":"http://foursquare.com/img/categories/building/default.png"},"address":"3420 Walnut Street","city":"Philadelphia","state":"Pennsylvania","zip":"19104","verified":false,"geolat":39.952739,"geolong":-75.193498,"stats":{"herenow":"5"},"hasTodo":"false","distance":463}]},{"type":"Nearby","venues":[{"id":5203495,"name":"ECFMG","primarycategory":{"id":79115,"fullpathname":"Home / Work / Other:Corporate / Office","nodename":"Corporate / Office","iconurl":"http://foursquare.com/img/categories/building/default.png"},"address":"3624 Market Street","city":"Philadelphia","state":"PA","verified":false,"geolat":39.956251,"geolong":-75.195408,"stats":{"herenow":"0"},"hasTodo":"false","distance":45},{"id":86066,"name":"DreamIt Ventures","primarycategory":{"id":79116,"fullpathname":"Home / Work / Other:Corporate / Office:Tech Startup","nodename":"Tech Startup","iconurl":"http://foursquare.com/img/categories/building/default.png"},"address":"3711 Market St. Suite 800","crossstreet":"Market and 37th","city":"Philadelphia","state":"PA","verified":false,"geolat":39.9566,"geolong":-75.197,"stats":{"herenow":"0"},"hasTodo":"false","distance":100},{"id":4527553,"name":"University of Pennsylvania Health System - 3701 Market Street","primarycategory":{"id":79136,"fullpathname":"Home / Work / Other:Medical:Doctor's Office","nodename":"Doctor's Office","iconurl":"http://foursquare.com/img/categories/building/medical.png"},"address":"3701 Market Street (7th Floor)","city":"Philadelphia","state":"PA","zip":"19104","verified":false,"geolat":39.9564491,"geolong":-75.1961361,"stats":{"herenow":"0"},"hasTodo":"false","distance":26},{"id":61912,"name":"University City Science Center","primarycategory":{"id":79005,"fullpathname":"College & Education:Academic Building:Science","nodename":"Science","iconurl":"http://foursquare.com/img/categories/education/default.png"},"address":"3711 Market St, Suite 800","crossstreet":"at Market and 37th","city":"Philadelphia","state":"PA","zip":"19104","verified":false,"geolat":39.9565,"geolong":-75.1964,"stats":{"herenow":"0"},"phone":"2159666000","hasTodo":"false","distance":48},{"id":153902,"name":"MidAtlantic Restaurant & Tap Room","primarycategory":{"id":96351,"fullpathname":"Food:New American","nodename":"New American","iconurl":"http://foursquare.com/img/categories/food/default.png"},"address":"3711 Market St","city":"Philadelphia","state":"PA","zip":"19104","verified":true,"geolat":39.956641,"geolong":-75.196958,"stats":{"herenow":"0"},"phone":"2153863711","hasTodo":"false","distance":97},{"id":1664565,"name":"GSPP | Penn Therapy and Fitness","primarycategory":{"id":79134,"fullpathname":"Home / Work / Other:Medical","nodename":"Medical","iconurl":"http://foursquare.com/img/categories/building/medical.png"},"address":"3624 Market St., 1st floor","crossstreet":"37th St.","city":"Philadelphia","state":"PA","zip":"19104","verified":false,"geolat":39.956251,"geolong":-75.195408,"stats":{"herenow":"0"},"twitter":"PennMedNews","hasTodo":"false","distance":45},{"id":388287,"name":"The Nosh","primarycategory":{"id":79230,"fullpathname":"Shops:Food & Drink:Deli / Bodega","nodename":"Deli / Bodega","iconurl":"http://foursquare.com/img/categories/shops/food_deli.png"},"address":"3600 Market Street","city":"Philadelphia","state":"Pennsylvania","zip":"19104","verified":f
			// clear locations
			latest_locations.clear();
			
			try 
			{
				JSONObject json = new JSONObject(json_string);
				JSONArray groups = json.getJSONArray("groups");
				
				for (int i = 0; i < groups.length(); i++)
				{
					JSONObject group = groups.getJSONObject(i);
					
					if (group.getString("type").equals("Nearby"))
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
}
