package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.res.Resources;
import android.util.Log;

public class BrightkiteService implements Service
{
	private static final String TAG = "BrightkiteService";
	private Properties config;
	private DrawableListItem logo;
	
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

	public String getName() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public DrawableListItem getLogo()
	{
		return logo;
	}

	public OAuthConnector getOAuthConnector() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Request getNewOAuthRequest() {
		// TODO Auto-generated method stub
		return null;
	}
}