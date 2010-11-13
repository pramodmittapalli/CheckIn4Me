package com.davidivins.checkin4me;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

/**
 * FoursquareOAuth
 * 
 * @author David Ivins
 */
public class FoursquareOAuth extends OAuth
{	
//	private String oauth_consumer_key;
//	private String oauth_consumer_secret;
//	
//	private String oauth_callback;
//	private String oauth_http_method;
//	private String oauth_host;
//	
//	private String oauth_signature_method;
//	private String oauth_version;
//	
//	private String oauth_request_token_endpoint;
//	private String oauth_authorize_endpoint;
//	private String oauth_access_token_endpoint;
		
	private static final String TAG      = "FoursquareOAuth";
	private static final String ENCODING = "ISO-8859-1";
	
	private Properties config;
	private String oauth_callback;
	
	/**
	 * constructor
	 */
	FoursquareOAuth(Resources resources)
	{
		try 
		{
			InputStream config_file = resources.openRawResource(R.raw.foursquare);
			config = new Properties();
			config.load(config_file);
		} 
		catch (Exception e) 
		{
			Log.e(TAG, "Failed to open config file");
		}
		
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
	public Response beginHandshake()
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
	 * getAuthorizationURL
	 * 
	 * @return String
	 */
	public String generateAuthorizationURL(String oauth_token)
	{
		return config.getProperty("oauth_host") + 
			config.getProperty("oauth_authorize_endpoint") + "?" + 
			"oauth_token=" + oauth_token;
	}
	
	/**
	 * completeHandshake
	 * 
	 * @return boolean
	 */
	public Response completeHandshake(String oauth_token_secret, String oauth_token, String oauth_verifier)
	{
		OAuthResponse response = new OAuthResponse();
		
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
}
