package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * BrightkiteService
 * 
 * @author david ivins
 */
public class BrightkiteService implements Service
{
	private static final String TAG = "BrightkiteService";
	private Properties config;
	private int service_id;
	
	/**
	 * BrightkiteService
	 * 
	 * @param resources
	 */
	public BrightkiteService(Resources resources, int service_id)
	{
		this.service_id = service_id;
		config = new Properties();
		
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
		return "Brightkite";
	}
	
	/**
	 * getLogoDrawable
	 * 
	 * @return int
	 */
	public int getLogoDrawable()
	{
		return R.drawable.brightkite_logo_resized;
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