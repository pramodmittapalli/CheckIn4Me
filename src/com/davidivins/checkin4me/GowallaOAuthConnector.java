package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONObject;

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
		
		Log.i(TAG, "isSuccessfulAuthorizationResponse = " + is_successful);
		return is_successful;
	}
	
	public void storeNecessaryAuthorizationResponseData(Editor settings_editor, Uri response)
	{
		Log.i(TAG, "code = " + response.getQueryParameter("code"));
		settings_editor.putString("gowalla_code", response.getQueryParameter("code"));
		settings_editor.commit();
	}

	public OAuthResponse completeHandshake(SharedPreferences settings, Uri previous_response) 
	{
		OAuthResponse response = new OAuthResponse();
		Log.i(TAG, "code in settings = " + settings.getString("gowalla_code", "-1"));
		if (settings.getString("gowalla_code", "-1") != "-1")
		{
			GowallaOAuthRequest request = new GowallaOAuthRequest(
					config.getProperty("oauth_http_method"), config.getProperty("oauth_host"), 
					config.getProperty("oauth_access_token_endpoint"));
			
			request.addQueryParameter("grant_type", "authorization_code");
			request.addQueryParameter("client_id", config.getProperty("oauth_client_id"));
			request.addQueryParameter("client_secret", config.getProperty("oauth_client_secret"));
			request.addQueryParameter("code", settings.getString("gowalla_code", "-1"));
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
		
		try
		{
			JSONObject json = new JSONObject(response.getResponseString());			
			if (json.has("access_token"))
				is_successful = true;
		}
		catch (Exception e)
		{
			Log.i(TAG, "response is not json - " + response.getResponseString());
		}
		
		Log.i(TAG, "isSuccessfulCompletionResponse = " + is_successful);
		return is_successful;
	}
	
	public void storeNecessaryCompletionResponseData(Editor settings_editor, OAuthResponse response) 
	{ 
		try
		{
			JSONObject json = new JSONObject(response.getResponseString());
			Log.i(TAG, "access_token = " + json.getString("access_token"));
			
			settings_editor.putString("gowalla_access_token", json.getString("access_token"));
			settings_editor.commit();
		}
		catch (Exception e)
		{
			Log.i(TAG, "response is not json - " + response.getResponseString());
		}
	}
}
