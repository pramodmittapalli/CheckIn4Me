package com.davidivins.checkin4me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * GowallaOAuthRequest
 * 
 * @author david
 */
public class GowallaOAuthRequest extends Request 
{
	private static final String TAG               = "GowallaOAuthRequest";
	private static final String RESPONSE_ENCODING = "UTF-8";
		
	/**
	 * FoursquareOAuthRequest
	 * 
	 * @param method
	 * @param host
	 * @param endpoint
	 */
	public GowallaOAuthRequest(String method, String host, String endpoint)
	{
		super(method, host, endpoint);
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

		Log.i(TAG, "executing Gowalla OAuth request...");
		
		// make request
		String url_string  = generateURL();
		Log.i(TAG, "request url = " + url_string);
		
		// make http request
		try
		{
			// make background http request for temporary token
			HttpClient   httpclient    = new DefaultHttpClient();
	    	HttpPost     httppost      = new HttpPost(url_string);
	    		
	    	HttpResponse http_response = httpclient.execute(httppost);

	    	// get content of request
	    	page = new BufferedReader(new InputStreamReader(
	    			http_response.getEntity().getContent(), RESPONSE_ENCODING));
	    	
	    	// read response into a string
	    	String line;
	    	while ((line = page.readLine()) != null)
	    	{
	    		Log.i(TAG, "line = " + line);
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
	 * generateURL
	 * 
	 * @return URL for the request
	 */
	private String generateURL()
	{
		return host + endpoint + "?" + getURIQueryParametersAsString();
	}
}

