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
	 * getLocations
	 * 
	 * @param String longitude
	 * @param String latitude
	 * @param SharedPreferences settings
	 * @return ArrayList<Locale>
	 */
//	public ArrayList<Locale> getLocations(String longitude, String latitude, SharedPreferences settings)
//	{
//		Log.i(TAG, "Retrieving Gowalla Locations");
//		ArrayList<Locale> locations = new ArrayList<Locale>();
//
//		HTTPRequest request = new HTTPRequest(
//			config.getProperty("api_http_method"), config.getProperty("api_host"), 
//			config.getProperty("api_locations_endpoint"));
//		
//		request.addHeader("X-Gowalla-API-Key", config.getProperty("oauth_client_id"));
//		request.addHeader("Accept", "application/" + config.getProperty("api_format"));
//		request.addQueryParameter("lat", latitude);
//		request.addQueryParameter("lng", longitude);
//		request.addQueryParameter("radius", "50");
//			
//		OAuthResponse response = (OAuthResponse)request.execute();
//		
//		// {"total_results":50,"per_page":50,"total_pages":1,"current_page":1,"spots":[{"items_count":5,"photos_count":3,"checkins_count":109,"url":"/spots/676476","lat":39.951908274,"_image_url_200":"http://static.gowalla.com/spots/676476-86d17f82d3ac32e4061fc12ffa51f12c-200.png?1","description":"Commonly referred to as Penn, the University of Pennsylvania is the nation's fourth-oldest institution of higher education.","users_count":73,"lng":-75.194914341,"address":{"locality":"Philadelphia","region":"PA"},"highlights_url":"/spots/676476/highlights","radius_meters":750,"spot_categories":[{"url":"/categories/134","name":"University"}],"items_url":"/spots/676476/items","name":"University of Pennsylvania","strict_radius":false,"trending_level":0,"activity_url":"/spots/676476/events","_image_url_50":"http://static.gowalla.com/spots/676476-5936c1901b29d760a4ca8abf14a811d6-100.png?1","image_url":"http://static.gowalla.com/spots/676476-5936c1901b29d760a4ca8abf14a811d6-100.png?1","checkins_url":"/checkins?spot_id=676476"},{"items_count":0,"photos_count":1,"url":"/spots/1246655","lat":39.95725494,"checkins_count":6,"_image_url_200":"http://static.gowalla.com/categories/16-017d496fbdb50dca6782702eef2d5c94-200.png?1","description":"Awesome.","lng":-75.19576555,"address":{"locality":"Philadelphia","region":"PA"},"users_count":6,"spot_categories":[{"url":"/categories/16","name":"American"}],"highlights_url":"/spots/1246655/highlights","radius_meters":75,"items_url":"/spots/1246655/items","trending_level":0,"name":"Chilis","strict_radius":false,"_image_url_50":"http://static.gowalla.com/categories/16-1f31710ac58f380ed7a48f5acbeba790-100.png?1","activity_url":"/spots/1246655/events","image_url":"http://static.gowalla.com/categories/16-1f31710ac58f380ed7a48f5acbeba790-100.png?1","checkins_url":"/checkins?spot_id=1246655"},{"items_count":0,"photos_count":0,"url":"/spots/464529","lat":39.95585209,"checkins_count":11,"_image_url_200":"http://static.gowalla.com/categories/24-602659589368399c1ab8d500d2b8a4da-200.png?1","description":"Live music, decent beer selection.","users_count":11,"lng":-75.19656699,"address":{"locality":"Philadelphia","region":"PA"},"spot_categories":[{"url":"/categories/24","name":"Pub"}],"highlights_url":"/spots/464529/highlights","radius_meters":75,"items_url":"/spots/464529/items","trending_level":0,"name":"The Blockley Pourhouse","strict_radius":false,"activity_url":"/spots/464529/events","_image_url_50":"http://static.gowalla.com/categories/24-1ce1ad9e41ae85d468b71e483daac930-100.png?1","checkins_url":"/checkins?spot_id=464529","image_url":"http://static.gowalla.com/categories/24-1ce1ad9e41ae85d468b71e483daac930-100.png?1"},{"items_count":2,"photos_count":0,"url":"/spots/1232369","lat":39.95598495,"checkins_count":9,"_image_url_200":"http://static.gowalla.com/categories/103-85d251d2886f5afe13ed12e182dd9cf2-200.png?1","description":null,"lng":-75.19688373,"address":{"locality":"Philadelphia","region":"PA"},"users_count":9,"spot_categories":[{"url":"/categories/103","name":"Theatre"}],"highlights_url":"/spots/1232369/highlights","radius_meters":75,"items_url":"/spots/1232369/items","trending_level":0,"name":"International House Philadelphia","strict_radius":false,"_image_url_50":"http://static.gowalla.com/categories/103-a2fe64cadd32e2349b82776bbc364f21-100.png?1","activity_url":"/spots/1232369/events","image_url":"http://static.gowalla.com/categories/103-a2fe64cadd32e2349b82776bbc364f21-100.png?1","checkins_url":"/checkins?spot_id=1232369"},{"items_count":0,"photos_count":0,"url":"/spots/1094680","lat":39.9555302402,"checkins_count":13,"_image_url_200":"http://static.gowalla.com/categories/148-d03e0ec0dcf34507e23821e2ba05fb16-200.png?1","description":"Hell","users_count":8,"lng":-75.1952241958,"address":{"locality":"Philadelphia","region":"PA"},"spot_categories":[{"url":"/categories/148","name":"Medical School"}],"highlights_url":"/spots/1094680/highlights","radius_meters":75,"items_url":"/spots/1094680/items","trending_level":0,"name":"ECFMG","strict_radius":f
//		
//		Log.i(TAG, "response.getSuccessStatus() = " + response.getSuccessStatus());
//		Log.i(TAG, "response.getResponseString() = " + response.getResponseString());
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