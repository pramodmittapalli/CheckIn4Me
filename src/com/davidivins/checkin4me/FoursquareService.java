package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * FoursquareService
 * 
 * @author david ivins
 */
public class FoursquareService implements Service
{
	private static final String TAG = "FoursquareService";
	
	private Properties config;
	private OAuthConnector oauth_connector;
	private APIAdapter api_adapter;
	private int service_id;
	
	/**
	 * FoursquareService
	 * 
	 * @param resources
	 */
	public FoursquareService(Resources resources, int service_id)
	{
		this.service_id = service_id;
		config = new Properties();
		
		try 
		{
			InputStream config_file = resources.openRawResource(GeneratedResources.getRaw("foursquare"));
			config.load(config_file);
			
			// create oauth connector with current configuration
			oauth_connector =  new FoursquareOAuthConnector(config);
			api_adapter = new FoursquareAPIAdapter(config, service_id);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, "Failed to open config file");
		}
		
	}
	
	/**
	 * getId
	 * 
	 * @return int id
	 */
	public int getId()
	{
		return service_id;
	}

	/**
	 * getName
	 * 
	 * @return String
	 */
	public String getName() 
	{
		return "Foursquare";
	}
	
	/**
	 * getLogoDrawable
	 * 
	 * @return int
	 */
	public int getLogoDrawable()
	{
		return GeneratedResources.getDrawable("foursquare_logo_resized");
	}
	
	/**
	 * getIconDrawable
	 * 
	 * @return int
	 */
	public int getIconDrawable()
	{
		return GeneratedResources.getDrawable("foursquare25x25");
	}

	/**
	 * getOAuthConnector
	 * 
	 * @return OAuthConnector
	 */
	public OAuthConnector getOAuthConnector() 
	{
		return oauth_connector;
	}
	
	/**
	 * getAPIAdapter
	 * 
	 * @return APIAdapter
	 */
	public APIAdapter getAPIAdapter()
	{
		return api_adapter;
	}
	
	/**
	 * connected
	 * 
	 * @return boolean
	 */
	public boolean connected(SharedPreferences settings)
	{
		return settings.contains("foursquare_oauth_token") && 
			(settings.getString("foursquare_oauth_token", null) != null) &&
			settings.contains("foursquare_oauth_token_secret") &&
			(settings.getString("foursquare_oauth_token_secret", null) != null);
	}
}
