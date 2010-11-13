package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import android.content.res.Resources;
import android.util.Log;

public class GowallaOAuth extends OAuth
{
	private static final String TAG = "GowallaOAuth";
	private Properties config;
	
	GowallaOAuth(Resources resources) 
	{
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.gowalla);
			config = new Properties();
			config.load(config_file);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, "Failed to open config file");
		}
	}
	
	public Response beginHandshake() 
	{ 
		Response response = new Response(true, ""); 
		return response; 
	}
	
	public String generateAuthorizationURL(String oauth_token)
	{
		//return //"https://gowalla.com/api/oauth/new" 
		return config.getProperty("oauth_host") + config.getProperty("oauth_new_token_endpoint")
			+ "?redirect_uri=" + config.getProperty("oauth_redirect_uri") //checkin4me:///"
			+ "&client_id=" + config.getProperty("oauth_client_id") //12f4a2ac2b4a4afb9aa87b7ee4a16f0a"
			+ "&scope=read-write";
	}
	
	public Response completeHandshake(String a, String b, String c) 
	{ 
		Response response = new Response(true, ""); 
		return response; 
	}
}
