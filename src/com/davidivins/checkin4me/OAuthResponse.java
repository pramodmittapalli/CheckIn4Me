package com.davidivins.checkin4me;

import java.util.TreeMap;

public class OAuthResponse extends Response
{
	public OAuthResponse()
	{
		super();
	}
	
	public OAuthResponse(boolean success_status, String response_string)
	{
		super(success_status, response_string);
	}
	
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
