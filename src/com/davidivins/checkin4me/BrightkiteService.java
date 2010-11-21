package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * BrightkiteService
 * 
 * @author david
 */
public class BrightkiteService implements Service
{
	private static final String TAG = "BrightkiteService";
	private Properties config;
	private DrawableListItem logo;
	
	/**
	 * BrightkiteService
	 * 
	 * @param resources
	 */
	public BrightkiteService(Resources resources)
	{
		config = new Properties();
		logo = new FoursquareLogo("servicelogo", R.drawable.brightkite_logo_resized, R.id.servicelogo);
		
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.brightkite);
			config.load(config_file);
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
		return "Brightkite";
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
		return R.drawable.brightkite25x25;
	}

	/**
	 * getOAuthConnector
	 * 
	 * @return null
	 */
	public OAuthConnector getOAuthConnector() 
	{
		// brightkite doesn't work yet
		return null;
	}
	
	/**
	 * getAPIAdapter
	 * 
	 * @return APIAdapter
	 */
	public APIAdapter getAPIAdapter()
	{
		// brightkite doesn't work yet
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
		return false;
	}
}