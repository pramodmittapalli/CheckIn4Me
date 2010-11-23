package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * FoursquareService
 * 
 * @author david
 */
public class FoursquareService implements Service
{
	private static final String TAG = "FoursquareService";
	
	private Properties config;
	private DrawableListItem logo;
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
		logo = new FoursquareLogo("servicelogo", R.drawable.foursquare_logo_resized, R.id.servicelogo);
		
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.foursquare);
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
	 * getName
	 * 
	 * @return String
	 */
	public String getName() 
	{
		return "Foursquare";
	}
	
	/**
	 * getLogo
	 * 
	 * @return DrawableListItem
	 */
	public DrawableListItem getLogo()
	{
		return logo;
	}
	
	/**
	 * getIconDrawable
	 * 
	 * @return int
	 */
	public int getIconDrawable()
	{
		return R.drawable.foursquare25x25;
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
