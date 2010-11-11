package com.davidivins.checkin4me;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

/**
 * FoursquareOAuth
 * 
 * @author david
 */
public class FoursquareOAuth extends OAuth
{
	private String oauth_request_token;
	private String oauth_access_token;
	private String oauth_nonce;
	
	private String oauth_consumer_key;
	private String oauth_consumer_secret;
	
	private String oauth_callback;
	private String oauth_http_method;
	private String oauth_host;
	
	private String oauth_signature_method;
	private String oauth_version;
	
	private String oauth_request_token_endpoint;
	private String oauth_authorize_endpoint;
	private String oauth_access_token_endpoint;
	
	private HashMap<String, String> request_token_response;
	private HashMap<String, String> authorization_response;
	private HashMap<String, String> access_token_response;
		
	private static final String TAG      = "FoursquareOAuth";
	private static final String ENCODING = "ISO-8859-1";
	
	private SharedPreferences settings;
	private SharedPreferences.Editor settings_editor;
	
	/**
	 * constructor
	 */
	FoursquareOAuth(SharedPreferences settings)
	{		
		this.settings = settings;
		this.settings_editor = settings.edit();
		
		//
		// move this stuff into a config file
		//
		oauth_request_token          = "";
		oauth_access_token           = "";
		oauth_nonce                  = "";
		
		oauth_consumer_key           = "1AOTUF0OB4UQQWVS1DFDQWD3LKEBPWOCQPRDP4NYATJCUEAW";
		oauth_consumer_secret        = "ID1ALZP5XUA4YMJ3GSB3Q3GC2NZZFT0VPVYN5JMEBBG0QMEK" + "&"; // needs &
		
		try
		{
			// must be encoded twice :(
			oauth_callback           = URLEncoder.encode("checkin4me:///", ENCODING);
		} 
		catch(Exception e) 
		{ 
			Log.wtf(TAG, ENCODING + " isn't a valid encoding!?");
		}
		
		oauth_http_method            = "GET";
		oauth_host                   = "https://foursquare.com";
		
		oauth_signature_method       = "HMAC-SHA1";
		oauth_version                = "1.0";
		
		oauth_request_token_endpoint = "/oauth/request_token";
		oauth_authorize_endpoint     = "/mobile/oauth/authorize";
		oauth_access_token_endpoint  = "/oauth/access_token";
		
		request_token_response       = new HashMap<String, String>();
		authorization_response       = new HashMap<String, String>();
		access_token_response        = new HashMap<String, String>();
	}
	
	protected void finalize() throws Throwable
	{
		settings_editor.commit();
		super.finalize();
	}
	
	/**
	 * beginHandshake
	 * 
	 * @return boolean
	 */
	public boolean beginHandshake()
	{
		Log.i(TAG, "beginning Foursquare OAuth handshake...");
		boolean request_success_status = false; // assume request failure
		
		// make request token request
		String base_string = generateRequestTokenBaseString();
		Log.i(TAG, "base_string = " + base_string);

		String signature   = getSignature(oauth_consumer_secret, base_string);
		String url_string  = generateRequestTokenURL(signature);
		
		//Log.i(TAG, "base string created = " + base_string);
		//Log.i(TAG, "signature calculated = " + signature);
		//Log.i(TAG, "request token url = " + url_string);
		
		BufferedReader page = null;
		
		// make http request
		try
		{
			//Log.i(TAG, "making http request to Foursquare...");
			// make background http request for temporary token
			HttpClient httpclient = new DefaultHttpClient();
	    	HttpGet httpget       = new HttpGet(url_string);
	    	HttpResponse response = httpclient.execute(httpget);
	    	
	    	// get content of request
	    	page = new BufferedReader(new InputStreamReader(
	    			response.getEntity().getContent(), "UTF-8"));
	    	
	    	//Log.i(TAG, "reading response...");
	    	
	    	// read response into a string
	    	String line;
	    	String response_str = "";
	    	while ((line = page.readLine()) != null)
	    	{
	    		response_str += line;
	    	}
	    	
	    	//Log.i(TAG, "http response = " + response_str);
	    	
	    	// break out response query parameters and store in map
	    	String[] query_parameters = response_str.split("&");
	    	for (String query_parameter : query_parameters)
	    	{
	    		String[] name_value = query_parameter.split("=");
	    		//Log.i(TAG, "" + name_value.length);
	    		if (name_value.length == 2)
	    			request_token_response.put(name_value[0], name_value[1]);
	    	}
	    	
	    	// check returned data for correct values
			if (request_token_response.containsKey("oauth_token") &&
					request_token_response.containsKey("oauth_token_secret") &&
					request_token_response.containsKey("oauth_callback_confirmed") &&
					request_token_response.get("oauth_callback_confirmed").equals("true"))
				request_success_status = true;
			
			settings_editor.putString("oauth_token", request_token_response.get("oauth_token"));
			settings_editor.putString("oauth_token_secret", request_token_response.get("oauth_token_secret"));
			settings_editor.commit();

		}
		catch (IOException e)
		{
			Log.e(TAG, "EXCEPTION: " + e.getMessage());
			request_success_status = false;
		}

		//Log.i(TAG, "token request success status = " + request_success_status);
		return request_success_status;
	}
	
	/**
	 * getAuthorizationURL
	 * 
	 * @return String
	 */
	public String generateAuthorizationURL()
	{
		String url = "";
		
		if (request_token_response.containsKey("oauth_token"))
			url = oauth_host + oauth_authorize_endpoint + "?" + 
				"oauth_token=" + request_token_response.get("oauth_token");
		
		return url;
	}
	
	/**
	 * processAuthorizationResponseURI
	 * 
	 * @param Uri
	 */
	public void processAuthorizationResponseURI(Uri uri)
	{
		Log.i(TAG, "saved oauth_token = " + settings.getString("oauth_token", "666"));
		Log.i(TAG, "saved oauth_token_secret = " + settings.getString("oauth_token_secret", "666"));

		authorization_response.put("oauth_token", uri.getQueryParameter("oauth_token"));
		authorization_response.put("oauth_verifier", uri.getQueryParameter("oauth_verifier"));
		
		Log.i(TAG, "received oauth_token = " + authorization_response.get("oauth_token"));
		Log.i(TAG, "received oauth_verifier = " + authorization_response.get("oauth_verifier"));
	}
	
	/**
	 * completeHandshake
	 * 
	 * @return boolean
	 */
	public boolean completeHandshake()
	{
		boolean request_success_status = false; // assume request failure
		
		Log.i(TAG, "completing handshake");
		// make request token request
		String base_string = generateAccessTokenBaseString();
		Log.i(TAG, "base string = " + base_string);
		String signature   = getSignature(
				oauth_consumer_secret + settings.getString("oauth_token_secret", "666"),//request_token_response.get("oauth_token_secret"), 
				base_string);
		String url_string  = generateAccessTokenURL(signature);
		Log.i(TAG, "url = " + url_string);
		
		BufferedReader page = null;
		
		// make http request
		try
		{
			// make background http request for temporary token
			HttpClient httpclient = new DefaultHttpClient();
	    	HttpGet httpget       = new HttpGet(url_string);
	    	HttpResponse response = httpclient.execute(httpget);
	    	
	    	// get content of request
	    	page = new BufferedReader(new InputStreamReader(
	    			response.getEntity().getContent(), "UTF-8"));
	    	
	    	// read response into a string
	    	String line;
	    	String response_str = "";
	    	while ((line = page.readLine()) != null)
	    	{
	    		response_str += line;
	    	}
	    	
	    	Log.i(TAG, response_str);
	    	// break out response query parameters and store in map
	    	String[] query_parameters = response_str.split("&");
	    	for (String query_parameter : query_parameters)
	    	{ 
	    		String[] name_value = query_parameter.split("=");
	    		Log.i(TAG, name_value[0] + " = " + name_value[1]);
	    		if (name_value.length == 2)
	    			request_token_response.put(name_value[0], name_value[1]);
	    	}
	    	
	    	// check returned data for correct values
//			if (request_token_response.containsKey("oauth_token") &&
//					request_token_response.containsKey("oauth_token_secret") &&
//					request_token_response.containsKey("oauth_callback_confirmed") &&
//					request_token_response.get("oauth_callback_confirmed").equals("true"))
				request_success_status = true;

		}
		catch (IOException e)
		{
			Log.e(TAG, "EXCEPTION: " + e.getMessage());
			request_success_status = false;
		}

		Log.i(TAG, "returning " + request_success_status);
		return request_success_status;
	}
	
	/**
	 * hasAccessToken
	 * 
	 * @return boolean
	 */
	public boolean hasAccessToken()
	{
		return (!oauth_access_token.equals(""));
	}
	
	/**
	 * getAccessToken
	 * 
	 * @return String
	 */
	public String getAccessToken()
	{
		return oauth_access_token;
	}

	/**
	 * getNonce
	 * 
	 * @return String
	 */
	private String getNonce()
	{
		if (oauth_nonce.equals(""))
		{
			Random random = new Random();
			oauth_nonce =  Long.toString(Math.abs(random.nextLong()), 60000);
		}
		
		return oauth_nonce;
	}
	
	/**
	 * getTimestamp
	 * 
	 * @return String
	 */
	private String getTimestamp()
	{
		return Integer.toString((int)(System.currentTimeMillis() / 1000L));
	}
	
	/**
	 * getRequestTokenURI
	 * 
	 * @return String
	 */
	private String getRequestTokenURIQueryParameters()
	{
		// uri parameters must be in alphabetical order
		return "oauth_callback="       + oauth_callback         +
			"&oauth_consumer_key="     + oauth_consumer_key     +
			"&oauth_nonce="            + getNonce()             +
			"&oauth_signature_method=" + oauth_signature_method +
			"&oauth_timestamp="        + getTimestamp()         +
			"&oauth_version="          + oauth_version;
	}
	
	/**
	 * getAccessTokenURI
	 * 
	 * @return String
	 */
	private String getAccessTokenURIQueryParameters()
	{
		String nonce = getNonce();
		String timestamp = getTimestamp();
		
		Log.i(TAG, "oauth_consumer_key = " + oauth_consumer_key);
		Log.i(TAG, "oauth_nonce = " + nonce);
		Log.i(TAG, "oauth_signature_method = " + oauth_signature_method);
		Log.i(TAG, "oauth_timestamp = " + timestamp);
		Log.i(TAG, "oauth_token = " + authorization_response.get("oauth_token"));
		Log.i(TAG, "oauth_verifier = " + authorization_response.get("oauth_verifier"));
		Log.i(TAG, "oauth_version = " + oauth_version);


		// uri parameters must be in alphabetical order
		return "oauth_consumer_key="   + oauth_consumer_key                           +
			"&oauth_nonce="            + nonce                                   +
			"&oauth_signature_method=" + oauth_signature_method                       +
			"&oauth_timestamp="        + timestamp                               +
			"&oauth_token="            + authorization_response.get("oauth_token")    +
			"&oauth_verifier="         + authorization_response.get("oauth_verifier") +
			"&oauth_version="          + oauth_version;
	}
	
	/**
	 * generateRequestTokenBaseString
	 * 
	 * @return String
	 */
	private String generateRequestTokenBaseString()
	{
		String base_string = "";
		
		try
		{
			base_string = oauth_http_method                               + "&" + 
				URLEncoder.encode(oauth_host, ENCODING)                   + 
				URLEncoder.encode(oauth_request_token_endpoint, ENCODING) + "&" + 
				URLEncoder.encode(getRequestTokenURIQueryParameters(), ENCODING);
		}
		catch (Exception e)
		{
			Log.wtf(TAG, ENCODING + " doesn't exist!?");
		}
		
		return base_string;
	}
	
	/**
	 * generateAccessTokenBaseString
	 * 
	 * @return String
	 */
	private String generateAccessTokenBaseString()
	{
		String base_string = "";
		
		try
		{
			base_string = oauth_http_method                              + "&" +
				URLEncoder.encode(oauth_host, ENCODING)                  +
				URLEncoder.encode(oauth_access_token_endpoint, ENCODING) + "&" +
				URLEncoder.encode(getAccessTokenURIQueryParameters(), ENCODING);
		}
		catch(Exception e)
		{
			Log.wtf(TAG, ENCODING + " doesn't exist!?");
		}
		
		return base_string;
	}
	
	/**
	 * generateRequestTokenURL
	 * 
	 * @return String
	 */
	private String generateRequestTokenURL(String signature)
	{
		return oauth_host                       + 
			oauth_request_token_endpoint        + "?" + 
			getRequestTokenURIQueryParameters() + "&" +
			"oauth_signature="                  + signature;
	}
	
	/**
	 * generateAccessTokenURL
	 * 
	 * @return String
	 */
	private String generateAccessTokenURL(String signature)
	{
		return oauth_host                      + 
			oauth_access_token_endpoint        + "?" + 
			getAccessTokenURIQueryParameters() + "&" +
			"oauth_signature="                 + signature;
	}
	
	/**
	 * getSignature
	 * 
	 * @param String
	 * @return String
	 */
	private String getSignature(String secret_key, String base_string)
	{
		String out_str = "";

		try
		{
			SecretKey key = new SecretKeySpec(secret_key.getBytes(), "HmacSHA1");

			Mac m = Mac.getInstance("HmacSHA1");
			m.init(key);

			byte[] mac = m.doFinal(base_string.getBytes());

			out_str = Base64.encodeToString(mac, Base64.NO_WRAP);
			out_str = URLEncoder.encode(out_str, ENCODING);
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
		
		Log.i(TAG, "signature calculated = " + out_str);
		return out_str;
	}
}
