package com.davidivins.checkin4me;

import java.util.ArrayList;

public class Services 
{
	private ArrayList<Service> services;
	
	public Services()
	{
		services = new ArrayList<Service>();
		services.add(new Service("servicelogo", R.drawable.foursquare_logo_resized, R.id.servicelogo));
		services.add(new Service("servicelogo", R.drawable.gowalla_logo_resized, R.id.servicelogo));
		services.add(new Service("servicelogo", R.drawable.brightkite_logo_resized, R.id.servicelogo));
	}
	
	public ArrayList<Service> getServicesAsList()
	{
		return services;
	}

	public String[] getKeys()
	{
		String[] keys = new String[services.size()];
		
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = services.get(i).getKey();
		}
		
		return keys;
	}
	
	public int[] getIds()
	{
		int[] ids = new int[services.size()];
		
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = services.get(i).getId();
		}
		
		return ids;
	}
}
