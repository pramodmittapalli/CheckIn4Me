package com.davidivins.checkin4me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * HTTPRequest
 * 
 * @author david
 */
public class HTTPRequest extends Request 
{
	private static final String TAG               = "HTTPRequest";
	private static final String RESPONSE_ENCODING = "UTF-8";
	
	private HashMap<String, String> headers;
	
	/**
	 * HTTPRequest
	 * 
	 * @param method
	 * @param host
	 * @param endpoint
	 */
	public HTTPRequest(String method, String host, String endpoint)
	{
		super(method, host, endpoint);
		headers = new HashMap<String, String>();
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

		Log.i(TAG, "executing HTTP request...");
		
		// make request
		String url_string  = generateURL();
		Log.i(TAG, "request url = " + url_string);
		
		// make http request
		try
		{
			// make background http request for temporary token
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse http_response;
			
			// get request
			if (method.equals("GET"))
			{
				HttpGet httpget = new HttpGet(url_string);
				
				Set<String> keys = headers.keySet();
				for (String key : keys)
				{
					httpget.addHeader(key, headers.get(key));
				}
				
				http_response = httpclient.execute(httpget);
			}
			else // post request
			{
				HttpPost httppost = new HttpPost(url_string);
				
				Set<String> keys = headers.keySet();
				for (String key : keys)
				{
					httppost.addHeader(key, headers.get(key));
				}
				
				http_response = httpclient.execute(httppost);
			}

	    	// get content of request
	    	page = new BufferedReader(new InputStreamReader(
	    			http_response.getEntity().getContent(), RESPONSE_ENCODING));
	    	
	    	// read response into a string
	    	String line;
	    	while ((line = page.readLine()) != null)
	    	{
	    		//Log.i(TAG, "line = " + line);
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
	
	/**
	 * addHeader
	 * 
	 * @param String key
	 * @param String value
	 */
	public void addHeader(String key, String value)
	{
		headers.put(key, value);
	}
}

