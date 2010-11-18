package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

public class FoursquareService implements Service
{
	private static final String TAG = "FoursquareService";
	
	private Properties config;
	private DrawableListItem logo;
	private OAuthConnector oauth_connector;
	
	public FoursquareService(Resources resources)
	{
		config = new Properties();
		logo = new FoursquareLogo("servicelogo", R.drawable.foursquare_logo_resized, R.id.servicelogo);
		
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.foursquare);
			config.load(config_file);
			
			// create oauth connector with current configuration
			oauth_connector =  new FoursquareOAuthConnector(config);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, "Failed to open config file");
		}
		
	}

	public String getName() 
	{
		return "Foursquare";
	}
	
	public DrawableListItem getLogo()
	{
		return logo;
	}

	public OAuthConnector getOAuthConnector() 
	{
		return oauth_connector;
	}

	public Request getNewOAuthRequest() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean connected(SharedPreferences settings)
	{
		return settings.contains("foursquare_oauth_token") && 
			(settings.getString("foursquare_oauth_token", null) != null) &&
			settings.contains("foursquare_oauth_token_secret") &&
			(settings.getString("foursquare_oauth_token_secret", null) != null);
	}
}
