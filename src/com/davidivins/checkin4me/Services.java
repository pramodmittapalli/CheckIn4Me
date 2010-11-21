package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * Services
 * 
 * @author david
 */
public class Services
{
	private static Services instance;	
	private ArrayList<Service> services;
	
	/**
	 * Services
	 * 
	 * @param resources
	 */
	private Services(Resources resources)
	{
		services = new ArrayList<Service>();
		services.add(new FoursquareService(resources));
		services.add(new GowallaService(resources));
		services.add(new BrightkiteService(resources));
	}
	
	/**
	 * getInstance
	 * 
	 * @param activity
	 * @return Services
	 */
	static public Services getInstance(Activity activity)
	{
		if (null == instance)
			instance = new Services(activity.getResources());
		
		return instance;
	}
	
	/**
	 * getServiceById
	 * 
	 * @param id
	 * @return Service
	 */
	public Service getServiceById(int id)
	{
		return services.get(id);
	}
	
	/**
	 * getServicesAsArrayList
	 * 
	 * @return ArrayList<Service>
	 */
	public ArrayList<Service> getServicesAsArrayList()
	{
		return services;
	}
	
	/**
	 * getLogoKeys
	 * 
	 * @return String[]
	 */
	public String[] getLogoKeys()
	{
		String[] keys = new String[services.size()];
		
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = services.get(i).getLogo().getKey();
		}
		
		return keys;
	}
	
	/**
	 * getLogoDrawables
	 * 
	 * @return int[]
	 */
	public int[] getLogoDrawables()
	{
		int[] drawables = new int[services.size()];
		
		for (int i = 0; i < drawables.length; i++)
		{
			drawables[i] = services.get(i).getLogo().getDrawable();
		}
		
		return drawables;
	}
	
	/**
	 * getLogoIds
	 * 
	 * @return int[]
	 */
	public int[] getLogoIds()
	{
		int[] ids = new int[services.size()];
		
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = services.get(i).getLogo().getId();
		}
		
		return ids;
	}
	
	/**
	 * getAllLocations
	 * 
	 * @param String longitude
	 * @param String latutude
	 * @param SharedParameters settings
	 * @return ArrayList<Locale>
	 */
	public ArrayList<Locale> getAllLocations(String longitude, String latitude, SharedPreferences settings)
	{
		ArrayList<ArrayList<Locale>> location_lists = new ArrayList<ArrayList<Locale>>();
		
		for (Service service : services)
		{
			if (service.connected(settings))
				location_lists.add(service.getAPIAdapter().getLocations(longitude, latitude, settings));
		}
		
		ArrayList<Locale> locations = mergeLocations(location_lists);
		return locations;
	}
	
	/**
	 * mergeLocations
	 * 
	 * @param ArrayList<ArrayList<Locale>> location_lists
	 * @return ArrayList<Locale>
	 */
	private ArrayList<Locale> mergeLocations(ArrayList<ArrayList<Locale>> location_lists)
	{
		ArrayList<Locale> locations;
		
		// if location lists is empty, use empty locations list
		if (location_lists.isEmpty())
			locations = new ArrayList<Locale>();
		else // otherwise, start with first locations list as base, and merge with that
		{
			locations = location_lists.get(0);
			location_lists.remove(0);
		}
		
		// loop through location lists
		for (ArrayList<Locale> location_list : location_lists)
		{
			// loop through incoming locations
			for (Locale incoming_location : location_list)
			{
				boolean merged = false;
				
				// compare each incoming location against each existing locations
				for (Locale existing_location : locations)
				{
					// if their names match, merge them
					if (existing_location.getName().equals(incoming_location.getName()))
					{
						merged = true;
						HashMap<Integer, String> mappings = incoming_location.getServiceIdToLocationIdMap();
						Set<Integer> keys = mappings.keySet();
						
						for (int key : keys)
						{
							existing_location.mapServiceIdToLocationId(key, mappings.get(key));
						}
					}
				}
				
				// if the location wasn't merged, add it to the list
				if (!merged)
					locations.add(incoming_location);
			}
		}
		
		return locations;
	}
	
	/**
	 * atLeastOneConnected
	 * 
	 * @param prefs
	 * @return boolean
	 */
	public boolean atLeastOneConnected(SharedPreferences prefs)
	{
		boolean result = false;
		
		for (Service service : services)
		{
			if (service.connected(prefs))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
}