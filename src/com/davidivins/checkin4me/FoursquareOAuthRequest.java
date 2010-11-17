package com.davidivins.checkin4me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;
import android.util.Log;

public class FoursquareOAuthRequest extends Request 
{
	private static final String TAG               = "FoursquareOAuthRequest";
	private static final String HASHING_ALGO      = "HmacSHA1";
	private static final String RESPONSE_ENCODING = "UTF-8";
	
	private String signing_key;
	
	/**
	 * FoursquareOAuthRequest
	 * 
	 * @param method
	 * @param host
	 * @param endpoint
	 */
	public FoursquareOAuthRequest(String signing_key, String method, String host, String endpoint)
	{
		super(method, host, endpoint);
		this.signing_key = signing_key;
	}
	
	/**
	 * getNonce
	 * 
	 * @return String
	 */
	public String generateNonce()
	{
		Random random = new Random();
		return Long.toString(Math.abs(random.nextLong()), 60000);
	}
	
	/**
	 * getTimestamp
	 * 
	 * @return String
	 */
	public String generateTimestamp()
	{
		return Integer.toString((int)(System.currentTimeMillis() / 1000L));
	}

	/**
	 * execute
	 * 
	 * @return Response from executing the request
	 */
	@Override
	public Response execute() 
	{
		BufferedReader page = null;
		OAuthResponse response = new OAuthResponse();

		Log.i(TAG, "executing Foursquare OAuth request...");
		
		// make request
		String base_string = generateBaseString();
		String signature   = calculateSignature(signing_key, base_string);
		String url_string  = generateURL(signature);
		
		Log.i(TAG, "base string generated = " + base_string);
		Log.i(TAG, "signature calculated  = " + signature);
		Log.i(TAG, "request url generated = " + url_string);
		
		// make http request
		try
		{
			// make background http request for temporary token
			HttpClient   httpclient    = new DefaultHttpClient();
	    	HttpGet      httpget       = new HttpGet(url_string);
	    	HttpResponse http_response = httpclient.execute(httpget);
	    	
	    	// get content of request
	    	page = new BufferedReader(new InputStreamReader(
	    			http_response.getEntity().getContent(), RESPONSE_ENCODING));
	    		    	
	    	// read response into a string
	    	String line;
	    	while ((line = page.readLine()) != null)
	    	{
	    		response.appendResponseString(line);
	    	}

	    	response.setSuccessStatus(true);	    	
		}
		catch (IOException e)
		{
			response.set(false, e.getMessage());
			Log.e(TAG, "EXCEPTION: " + e.getMessage());
		}

		Log.i(TAG, "response.getSuccessStatus = " + response.getSuccessStatus());
		Log.i(TAG, "response.getResponseString = " + response.getResponseString());
		return response;
	}
	
	/**
	 * generateBaseString
	 * 
	 * @return Base string generated for request
	 */
	private String generateBaseString()
	{
		String base_string = "";
		
		try
		{
			base_string = URLEncoder.encode(method, ENCODING) + "&" + 
				URLEncoder.encode(host, ENCODING)             + 
				URLEncoder.encode(endpoint, ENCODING)         + "&" + 
				URLEncoder.encode(getURIQueryParametersAsString(), ENCODING);
		}
		catch (Exception e)
		{
			Log.wtf(TAG, ENCODING + " doesn't exist!?");
		}
		
		return base_string;
	}
	
	/**
	 * calculateSignature
	 * 
	 * @param secret_key
	 * @param base_string
	 * @return Signature calculated for base string
	 */
	private String calculateSignature(String secret_key, String base_string)
	{
		String out_str = "";

		try
		{
			SecretKey key = new SecretKeySpec(secret_key.getBytes(), HASHING_ALGO);

			Mac m = Mac.getInstance(HASHING_ALGO);
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
	
	/**
	 * generateURL
	 * 
	 * @param signature
	 * @return URL for the request
	 */
	private String generateURL(String signature)
	{
		return host + endpoint + "?" + getURIQueryParametersAsString() + "&" +
			"oauth_signature=" + signature;
	}
}
