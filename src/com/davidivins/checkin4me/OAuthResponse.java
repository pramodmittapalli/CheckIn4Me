package com.davidivins.checkin4me;

import java.util.TreeMap;

/**
 * OAuthResponse
 * 
 * @author david ivins
 */
public class OAuthResponse extends Response
{
	/**
	 * OAuthResponse
	 */
	public OAuthResponse()
	{
		super();
	}
	
	/**
	 * OAuthResponse
	 * 
	 * @param success_status
	 * @param response_string
	 */
	public OAuthResponse(boolean success_status, String response_string)
	{
		super(success_status, response_string);
	}
	
	/**
	 * getQueryParameters
	 * 
	 * @return TreeMap<String, String>
	 */
	public TreeMap<String, String> getQueryParameters()
	{
		TreeMap<String, String> query_parameters = new TreeMap<String, String>();
		
		// break out response query parameters and store in map
		String[] parameters = response_string.split("&");
		
		for (String parameter : parameters)
		{
			String[] key_value = parameter.split("=");
			
			if (key_value.length == 2)
				query_parameters.put(key_value[0], key_value[1]);
		}
		
		return query_parameters;
	}
}
