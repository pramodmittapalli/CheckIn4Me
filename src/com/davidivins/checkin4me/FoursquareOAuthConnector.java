package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

/**
 * FoursquareOAuthConnector
 * 
 * @author david ivins
 */
public class FoursquareOAuthConnector implements OAuthConnector
{
	private static final String TAG      = "FoursquareOAuthConnector";
	private static final String ENCODING = "ISO-8859-1";

	private Properties config;
	private String oauth_redirect_uri;
	
	/**
	 * FoursquareOAuthConnector
	 * 
	 * @param config
	 */
	FoursquareOAuthConnector(Properties config) 
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

	/**
	 * beginHandshake
	 * 
	 * @return OAuthResponse
	 */
	public OAuthResponse beginHandshake() 
	{
		return new OAuthResponse(true, "");
	}

	/**
	 * isSuccessfulInitialResponse
	 * 
	 * @param OAuthResponse
	 * @return boolean
	 */
	public boolean isSuccessfulInitialResponse(OAuthResponse response) 
	{
		return true;
	}

	/**
	 * storeNecessaryInitialResponseData
	 * 
	 * @param Editor
	 * @param OAuthResponse
	 */
	public void storeNecessaryInitialResponseData(Editor settingsEditor, OAuthResponse response) { }

	public String generateAuthorizationURL(SharedPreferences settings) 
	{
		String url = config.getProperty("oauth_host", "OAUTH_HOST_HERE") 
			+ config.getProperty("oauth_authenticate_endpoint", "OAUTH_AUTHENTICATE_ENDPOINT_HERE")
			+ "?client_id=" + config.getProperty("oauth_client_id", "OAUTH_CLIENT_ID_HERE")
			+ "&response_type=" + config.getProperty("oauth_response_type", "OAUTH_RESPONSE_TYPE_HERE")
			+ "&redirect_uri=" + oauth_redirect_uri
			+ "&display=" + config.getProperty("oauth_display", "OAUTH_DISPLAY_HERE");
		
		Log.i(TAG, "authorization url = " + url);
		return url;
	}

	/**
	 * isSuccessfulAuthorizationResponse
	 * 
	 * @param Uri
	 * @return boolean
	 */
	public boolean isSuccessfulAuthorizationResponse(Uri response) 
	{
		boolean is_successful = false;
		
		if ((null != response) && (response.getQueryParameter("code") != null))
			is_successful = true;
		
		Log.i(TAG, "isSuccessfulAuthorizationResponse = " + is_successful);
		return is_successful;
	}
	
	/**
	 * storeNecessaryAuthorizationResponseData
	 * 
	 * @param Editor
	 * @param Uri
	 */
	public void storeNecessaryAuthorizationResponseData(Editor settings_editor, Uri response)
	{
		Log.i(TAG, "code = " + response.getQueryParameter("code"));
		settings_editor.putString("foursquare_code", response.getQueryParameter("code"));
		settings_editor.commit();
	}

	/**
	 * completeHandshake
	 * 
	 * @param SharedPreferences
	 * @param Uri
	 * @return OAuthResponse
	 */
	public OAuthResponse completeHandshake(SharedPreferences settings, Uri previous_response) 
	{
		OAuthResponse response = new OAuthResponse();
		Log.i(TAG, "code in settings = " + settings.getString("foursquare_code", "-1"));
		
		if (settings.getString("foursquare_code", "-1") != "-1")
		{
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("oauth_http_method", "OAUTH_HTTP_METHOD_HERE"), 
					config.getProperty("oauth_host", "OAUTH_HOST_HERE"), 
					config.getProperty("oauth_access_token_endpoint", "OAUTH_ACCESS_TOKEN_ENDPOINT_HERE"));
			
			request.addQueryParameter("client_id", config.getProperty("oauth_client_id", "OAUTH_CLIENT_ID_HERE"));
			request.addQueryParameter("client_secret", config.getProperty("oauth_client_secret", "OAUTH_CLIENT_SECRET_HERE"));
			request.addQueryParameter("grant_type", "authorization_code");
			request.addQueryParameter("redirect_uri", oauth_redirect_uri); 
			request.addQueryParameter("code", settings.getString("foursquare_code", "CODE_HERE"));
			
			response = (OAuthResponse)request.execute();
		}
		else
		{
			Log.e(TAG, "Attempting to complete handshake without a code");
		}
		
		return response;
	}
	
	/**
	 * isSuccessfulCompletionResponse
	 * 
	 * @param OAuthResponse response
	 * @return boolean
	 */
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
	
	/**
	 * storeNecessaryCompletionResponseData
	 * 
	 * @param Editor
	 * @param OAuthResponse
	 */
	public void storeNecessaryCompletionResponseData(Editor settings_editor, OAuthResponse response) 
	{ 
		try
		{
			JSONObject json = new JSONObject(response.getResponseString());
			Log.i(TAG, "access_token = " + json.getString("access_token"));
			settings_editor.putString("foursquare_oauth_token_secret", json.getString("access_token"));
			settings_editor.commit();
		}
		catch (Exception e)
		{
			Log.i(TAG, "response is not json - " + response.getResponseString());
		}
	}
	
	/**
	 * clearTemporarySettings
	 * 
	 * @param Editor
	 */
	public void clearTemporaryData(Editor settings_editor)
	{
		// clear initial values
		settings_editor.putString("foursquare_code", null);
		settings_editor.commit();
	}
}

