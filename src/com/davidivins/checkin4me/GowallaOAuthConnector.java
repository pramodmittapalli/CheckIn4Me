package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

public class GowallaOAuthConnector implements OAuthConnector
{
	private static final String TAG = "GowallaOAuthConnector";
	private static final String ENCODING = "ISO-8859-1";

	private Properties config;
	private String oauth_redirect_uri;
	
	GowallaOAuthConnector(Properties config) 
	{
		this.config = config;
		
		try
		{
			// must be encoded twice :(
			oauth_redirect_uri = URLEncoder.encode(config.getProperty("oauth_redirect_uri"), ENCODING);
		} 
		catch(Exception e) 
		{ 
			Log.e(TAG, ENCODING + " isn't a valid encoding!?");
		}
	}

	public OAuthResponse beginHandshake() 
	{
		return new OAuthResponse(true, "");
	}

	public boolean isSuccessfulInitialResponse(OAuthResponse response) 
	{
		return true;
	}

	public void storeNecessaryInitialResponseData(Editor settingsEditor, OAuthResponse response) { }

	public String generateAuthorizationURL(SharedPreferences settings) 
	{
		String url = config.getProperty("oauth_host") + config.getProperty("oauth_new_token_endpoint")
			+ "?redirect_uri=" + oauth_redirect_uri
			+ "&client_id=" + config.getProperty("oauth_client_id");
		
		Log.i(TAG, "authorization url = " + url);
		return url;
	}

	public boolean isSuccessfulAuthorizationResponse(Uri response) 
	{
		boolean is_successful = false;
		
		if ((null != response) && (response.getQueryParameter("code") != null))
			is_successful = true;
		
		return is_successful;
	}
	
	public void storeNecessaryAuthorizationResponseData(Editor settings_editor, Uri response)
	{
		Log.i(TAG, "code = " + response.getQueryParameter("code"));
		settings_editor.putString("code", response.getQueryParameter("code"));
		settings_editor.commit();
	}

	public OAuthResponse completeHandshake(SharedPreferences settings, Uri previous_response) 
	{
		OAuthResponse response = new OAuthResponse();
		Log.i(TAG, "code in settings = " + settings.getString("code", "-1"));
		if (settings.getString("code", "-1") != "-1")
		{
			GowallaOAuthRequest request = new GowallaOAuthRequest(
					config.getProperty("oauth_http_method"), config.getProperty("oauth_host"), 
					config.getProperty("oauth_access_token_endpoint"));
			
			request.addQueryParameter("grant_type", "authorization_code");
			request.addQueryParameter("client_id", config.getProperty("oauth_client_id"));
			request.addQueryParameter("client_secret", config.getProperty("oauth_client_secret"));
			request.addQueryParameter("code", settings.getString("code", "-1"));
			request.addQueryParameter("redirect_uri", oauth_redirect_uri);
			
			response = (OAuthResponse)request.execute();
		}
		else
		{
			Log.e(TAG, "Attempting to complete handshake without a code");
		}
		
		return response;
	}
	
	public boolean isSuccessfulCompletionResponse(OAuthResponse response) 
	{
		boolean is_successful = false;
		
		// FIX THIS - RESPONSE IS JSON!
		
//		TreeMap<String, String> parameters = response.getQueryParameters();
//		Log.i(TAG, "response_str = " + response.getResponseString());
//		Set<String> keys = parameters.keySet();
//		for (String key : keys)
//		{
//			Log.i(TAG, "RESPONSE PARAMETER KEY = " + key);
//		}
		
		//if ((null != response) && (parameters.get("code") != null))
			//is_successful = true;
		
		return is_successful;
	}
	
	public void storeNecessaryCompletionResponseData(Editor settingsEditor, OAuthResponse response) { }
}
