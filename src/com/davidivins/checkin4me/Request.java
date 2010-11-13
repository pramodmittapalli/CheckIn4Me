package com.davidivins.checkin4me;

import java.util.TreeMap;
import android.util.Log;

abstract public class Request 
{
	protected static final String TAG      = "Request";
	protected static final String ENCODING = "ISO-8859-1";
	
	protected String method;
	protected String host;
	protected String endpoint;
	protected TreeMap<String, String> query_parameters;
	
	public Request()
	{
		method = "";
		host = "";
		endpoint = "";
		query_parameters = new TreeMap<String, String>();	
	}
	
	public Request(String method, String host, String endpoint)
	{
		this.method = method;
		this.host = host;
		this.endpoint = endpoint;
		query_parameters = new TreeMap<String, String>();	
	}
	
	public Request(Request request)
	{
		this.method = request.getMethod();
		this.host = request.getHost();
		this.endpoint = request.getEndpoint();
		
		query_parameters = new TreeMap<String, String>();
		query_parameters.putAll(request.getQueryParameters());
	}
	
	abstract public Response execute();

	public void setMethod(String method)
	{
		this.method = method;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}
	
	public void addQueryParameter(String key, String value)
	{
		query_parameters.put(key, value);
	}
	
	public String getMethod()
	{
		return method;
	}
	
	public String getHost()
	{
		return host;
	}
	
	public String getEndpoint()
	{
		return endpoint;
	}
	
	public TreeMap<String, String> getQueryParameters()
	{
		return query_parameters;
	}
	
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
