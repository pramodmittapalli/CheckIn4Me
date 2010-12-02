package com.davidivins.checkin4me;

import java.util.HashMap;
import java.util.TreeMap;
import android.util.Log;

/**
 * Request
 * 
 * @author david ivins
 */
abstract public class Request 
{
	protected static final String TAG      = "Request";
	protected static final String ENCODING = "ISO-8859-1";
	
	protected String method;
	protected String host;
	protected String endpoint;
	protected HashMap<String, String> headers;
	protected TreeMap<String, String> query_parameters;
	
	/**
	 * Request
	 */
	public Request()
	{
		method = "";
		host = "";
		endpoint = "";
		headers = new HashMap<String, String>();
		query_parameters = new TreeMap<String, String>();
	}
	
	/**
	 * Request
	 * 
	 * @param method
	 * @param host
	 * @param endpoint
	 */
	public Request(String method, String host, String endpoint)
	{
		this.method = method;
		this.host = host;
		this.endpoint = endpoint;
		headers = new HashMap<String, String>();
		query_parameters = new TreeMap<String, String>();	
	}
	
	/**
	 * Request
	 * 
	 * @param request
	 */
	public Request(Request request)
	{
		this.method = request.getMethod();
		this.host = request.getHost();
		this.endpoint = request.getEndpoint();
		
		query_parameters = new TreeMap<String, String>();
		query_parameters.putAll(request.getQueryParameters());
	}
	
	/**
	 * execute
	 * 
	 * @return Response
	 */
	abstract public Response execute();

	/**
	 * setMethod
	 * 
	 * @param method
	 */
	public void setMethod(String method)
	{
		this.method = method;
	}
	
	/**
	 * setHost
	 * 
	 * @param host
	 */
	public void setHost(String host)
	{
		this.host = host;
	}
	
	/**
	 * setEndpoint
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
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
	
	/**
	 * addQueryParameter
	 * 
	 * @param key
	 * @param value
	 */
	public void addQueryParameter(String key, String value)
	{
		query_parameters.put(key, value);
	}
	
	/**
	 * getMethod
	 * 
	 * @return String
	 */
	public String getMethod()
	{
		return method;
	}
	
	/**
	 * getHost
	 * 
	 * @return String
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * getEndpoint
	 * 
	 * @return String
	 */
	public String getEndpoint()
	{
		return endpoint;
	}
	
	/**
	 * getQueryParameters
	 * 
	 * @return TreeMap<String, String>
	 */
	public TreeMap<String, String> getQueryParameters()
	{
		return query_parameters;
	}
	
	/**
	 * getURIQueryParametersAsString
	 * 
	 * @return String
	 */
	protected String getURIQueryParametersAsString()
	{
		String uri_query_parameters = "";
		
		for (String key : query_parameters.keySet())
		{
			if (!uri_query_parameters.equals(""))
				uri_query_parameters += "&";
			
			uri_query_parameters += key + "=" + query_parameters.get(key);
		}
		
		Log.i(TAG, "uri_query_parameters = " + uri_query_parameters);
		return uri_query_parameters;
	}
}
