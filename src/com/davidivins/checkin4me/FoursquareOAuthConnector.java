package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.TreeMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;

/**
 * FoursquareOAuth
 * 
 * @author David Ivins
 */
public class FoursquareOAuthConnector implements OAuthConnector
{		
	private static final String TAG      = "FoursquareOAuthConnector";
	private static final String ENCODING = "ISO-8859-1";
	
	private Properties config;
	private String oauth_callback;
	
	/**
	 * constructor
	 */
	FoursquareOAuthConnector(Properties config)
	{
		this.config = config;
		
		try
		{
			// must be encoded twice :(
			oauth_callback = URLEncoder.encode(config.getProperty("oauth_callback"), ENCODING);
		} 
		catch(Exception e) 
		{ 
			Log.e(TAG, ENCODING + " isn't a valid encoding!?");
		}
	}
	
	/**
	 * beginHandshake
	 * 
	 * @return boolean
	 */
	public OAuthResponse beginHandshake()
	{
		FoursquareOAuthRequest request = new FoursquareOAuthRequest(
				config.getProperty("oauth_consumer_secret") + "&",
				config.getProperty("oauth_http_method"), config.getProperty("oauth_host"), 
				config.getProperty("oauth_request_token_endpoint"));
		
		request.addQueryParameter("oauth_callback", oauth_callback);
		request.addQueryParameter("oauth_consumer_key", config.getProperty("oauth_consumer_key"));
		request.addQueryParameter("oauth_nonce", request.generateNonce());
		request.addQueryParameter("oauth_signature_method", config.getProperty("oauth_signature_method"));
		request.addQueryParameter("oauth_timestamp", request.generateTimestamp());
		request.addQueryParameter("oauth_version", config.getProperty("oauth_version"));
		
		OAuthResponse response = (OAuthResponse)request.execute();
		
		Log.i(TAG, response.getResponseString());
		return response;
	}
	
	/**
	 * isSuccessfulInitialResponse
	 * 
	 * @param OAuthResponse
	 * @return boolean
	 */
	public boolean isSuccessfulInitialResponse(OAuthResponse response)
	{
		boolean is_successful = false;
		TreeMap<String, String> parameters = response.getQueryParameters();
		
		if (response.getSuccessStatus() && parameters.containsKey("oauth_token_secret") &&
				parameters.containsKey("oauth_token") && 
				parameters.containsKey("oauth_callback_confirmed") &&
				parameters.get("oauth_callback_confirmed").equals("true"))
			is_successful = true;
		
		return is_successful;
	}
	
	/**
	 * storeNecessaryInitialResponseData
	 * 
	 * @param Editor
	 * @param OAuthResponse
	 */
	public void storeNecessaryInitialResponseData(Editor settings_editor, OAuthResponse response)
	{
		TreeMap<String, String> parameters = response.getQueryParameters();
		settings_editor.putString("foursquare_oauth_token_secret", parameters.get("oauth_token_secret"));
		settings_editor.putString("foursquare_oauth_token", parameters.get("oauth_token"));
		settings_editor.commit();
	}
	
	/**
	 * getAuthorizationURL
	 * 
	 * @return String
	 */
	public String generateAuthorizationURL(SharedPreferences settings)//String oauth_token)
	{
		return config.getProperty("oauth_host") + 
			config.getProperty("oauth_authorize_endpoint") + "?" + 
			"oauth_token=" + settings.getString("foursquare_oauth_token", "-1");//oauth_token;
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
		
		if ((null != response) && 
				(response.getQueryParameter("oauth_token") != null) &&
				(response.getQueryParameter("oauth_verifier") != null))
			is_successful = true;
		
		Log.i(TAG, "oauth_token=" + response.getQueryParameter("oauth_token"));
		Log.i(TAG, "oauth_verifier=" + response.getQueryParameter("oauth_verifier"));
		
		return is_successful;
	}
	
	/**
	 * storeNecessaryAuthorizationResponseData
	 * 
	 * @param settings_editor
	 * @param response
	 */
	public void storeNecessaryAuthorizationResponseData(Editor settings_editor, Uri response) { }
	
	/**
	 * completeHandshake
	 * 
	 * @return boolean
	 */
	public OAuthResponse completeHandshake(SharedPreferences settings, Uri previous_response)
	{
		OAuthResponse response = new OAuthResponse();
		
		String oauth_token_secret = settings.getString("foursquare_oauth_token_secret", null); 
		String oauth_token = previous_response.getQueryParameter("oauth_token");
		String oauth_verifier = previous_response.getQueryParameter("oauth_verifier");
		
		if (oauth_token_secret != null && oauth_token != null && oauth_verifier != null)
		{
			FoursquareOAuthRequest request = new FoursquareOAuthRequest(
					config.getProperty("oauth_consumer_secret") + "&" + oauth_token_secret,
					config.getProperty("oauth_http_method"), config.getProperty("oauth_host"), 
					config.getProperty("oauth_access_token_endpoint"));
			
			request.addQueryParameter("oauth_consumer_key", config.getProperty("oauth_consumer_key"));
			request.addQueryParameter("oauth_nonce", request.generateNonce());
			request.addQueryParameter("oauth_signature_method", config.getProperty("oauth_signature_method"));
			request.addQueryParameter("oauth_timestamp", request.generateTimestamp());
			request.addQueryParameter("oauth_token", oauth_token);
			request.addQueryParameter("oauth_verifier", oauth_verifier);
			request.addQueryParameter("oauth_version", config.getProperty("oauth_version"));
			
			response = (OAuthResponse)request.execute();
		}
		
		Log.i(TAG, "response.success_status = " + response.getSuccessStatus());
		Log.i(TAG, "response.response_string = " + response.getResponseString());
		return response;
	}
	
	/**
	 * isSuccessfulCompletionResponse
	 * 
	 * @param OAuthResponse
	 * @return boolean
	 */
	public boolean isSuccessfulCompletionResponse(OAuthResponse response)
	{
		boolean is_successful = false;
		TreeMap<String, String> parameters = response.getQueryParameters();

		if (response.getSuccessStatus() && parameters.containsKey("oauth_token_secret") &&
				parameters.containsKey("oauth_token"))
			is_successful = true;
		
		return is_successful;
	}
	
	/**
	 * storeNecessaryCompletionResponseData
	 * 
	 * @param settings_editor
	 * @param response
	 */
	public void storeNecessaryCompletionResponseData(Editor settings_editor, OAuthResponse response)
	{
		TreeMap<String, String> parameters = response.getQueryParameters();
		settings_editor.putString("foursquare_oauth_token_secret", parameters.get("oauth_token_secret"));
		settings_editor.putString("foursquare_oauth_token", parameters.get("oauth_token"));
		settings_editor.commit();
		
		Log.i(TAG, "oauth_token_secret = " + parameters.get("oauth_token_secret"));
		Log.i(TAG, "oauth_token = " + parameters.get("oauth_token"));
	}
}
