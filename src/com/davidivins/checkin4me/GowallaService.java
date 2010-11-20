package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * GowallaService
 * 
 * @author david
 */
public class GowallaService implements Service
{
	private static final String TAG = "GowallaService";
	private Properties config;
	private DrawableListItem logo;
	private OAuthConnector oauth_connector;
	
	/**
	 * GowallaService
	 * 
	 * @param resources
	 */
	public GowallaService(Resources resources)
	{
		config = new Properties();
		logo = new FoursquareLogo("servicelogo", R.drawable.gowalla_logo_resized, R.id.servicelogo);
		
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.gowalla);
			config.load(config_file);
			
			oauth_connector = new GowallaOAuthConnector(config);
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
		return "Gowalla";
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
	 * getOAuthConnector
	 * 
	 * @return OAuthConnector
	 */
	public OAuthConnector getOAuthConnector() 
	{
		return oauth_connector;
	}

	/**
	 * getNewOAuthRequest
	 * 
	 * @return Request
	 */
	public Request getNewOAuthRequest() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * connected
	 * 
	 * @param SharedPreferences
	 * @return boolean
	 */
	public boolean connected(SharedPreferences settings)
	{
		return settings.contains("gowalla_access_token") && 
			(settings.getString("gowalla_access_token", null) != null);
	}
}
