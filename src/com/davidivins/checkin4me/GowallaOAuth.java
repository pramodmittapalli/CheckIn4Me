package com.davidivins.checkin4me;

import android.net.Uri;
import android.util.Log;

public class GowallaOAuth extends OAuth
{
	private String oauth_access_token;
	private static final String TAG = "GowallaOAuth";
	
	GowallaOAuth() 
	{
		//oauth_consumer_key = "12f4a2ac2b4a4afb9aa87b7ee4a16f0a";
		oauth_access_token = "";
	}
	
	public boolean beginHandshake() { return true; }
	
	public String generateAuthorizationURL()
	{
		return "https://gowalla.com/api/oauth/new" 
			+ "?redirect_uri=checkin4me:///"
			+ "&client_id=12f4a2ac2b4a4afb9aa87b7ee4a16f0a"
			+ "&scope=read-write";
	}
	
	public void processAuthorizationResponseURI(Uri uri)
	{
		//if (uri.getQueryParameter("code") != null)
		//	authorization_response.put("oauth_token", uri.getQueryParameter("code"));
		//Log.i(TAG, x);//uri.getQueryParameter("code"));
	}
	
	public boolean completeHandshake() { return true; }
	public boolean hasAccessToken() { return false; }
	public String getAccessToken() { return ""; }
}
