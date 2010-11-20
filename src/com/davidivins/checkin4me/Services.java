package com.davidivins.checkin4me;

import java.util.ArrayList;

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