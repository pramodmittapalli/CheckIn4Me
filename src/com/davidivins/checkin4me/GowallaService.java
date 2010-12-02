package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

/**
 * GowallaService
 * 
 * @author david ivins
 */
public class GowallaService implements Service
{
	private static final String TAG = "GowallaService";
	private Properties config;
	private DrawableListItem logo;
	private OAuthConnector oauth_connector;
	private APIAdapter api_adapter;
	private int service_id;
	
	/**
	 * GowallaService
	 * 
	 * @param resources
	 */
	public GowallaService(Resources resources, int service_id)
	{
		this.service_id = service_id;
		
		config = new Properties();
		logo = new FoursquareLogo("servicelogo", R.drawable.gowalla_logo_resized, R.id.servicelogo);
		
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.gowalla);
			config.load(config_file);
			
			oauth_connector = new GowallaOAuthConnector(config);
			api_adapter = new GowallaAPIAdapter(config, service_id);
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
	 * getIconDrawable
	 * 
	 * @return int
	 */
	public int getIconDrawable()
	{
		return R.drawable.gowalla25x25;
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
	 * @param SharedPreferences
	 * @return boolean
	 */
	public boolean connected(SharedPreferences settings)
	{
		return settings.contains("gowalla_access_token") && 
			(settings.getString("gowalla_access_token", null) != null);
	}
}
