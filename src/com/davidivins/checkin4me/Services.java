package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;

public class Services
{
	private static Services instance;	
	private ArrayList<Service> services;
	
	private Services(Resources resources)
	{
		services = new ArrayList<Service>();
		services.add(new FoursquareService(resources));
		services.add(new GowallaService(resources));
		services.add(new BrightkiteService(resources));
	}
	
	static public Services getInstance(Activity activity)
	{
		if (null == instance)
			instance = new Services(activity.getResources());
		
		return instance;
	}
	
	public ArrayList<Service> getServicesAsArrayList()
	{
		return services;
	}
	
	public String[] getLogoKeys()
	{
		String[] keys = new String[services.size()];
		
		for (int i = 0; i < keys.length; i++)
		{
			keys[i] = services.get(i).getLogo().getKey();
		}
		
		return keys;
	}
	
	public int[] getLogoDrawables()
	{
		int[] drawables = new int[services.size()];
		
		for (int i = 0; i < drawables.length; i++)
		{
			drawables[i] = services.get(i).getLogo().getDrawable();
		}
		
		return drawables;
	}
	
	public int[] getLogoIds()
	{
		int[] ids = new int[services.size()];
		
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = services.get(i).getLogo().getId();
		}
		
		return ids;
	}
}

//public class Services 
//{
//	private ArrayList<Service> services;
//	
//	public Services()
//	{
//		services = new ArrayList<Service>();
//		services.add(new Service("servicelogo", R.drawable.foursquare_logo_resized, R.id.servicelogo));
//		services.add(new Service("servicelogo", R.drawable.gowalla_logo_resized, R.id.servicelogo));
//		services.add(new Service("servicelogo", R.drawable.brightkite_logo_resized, R.id.servicelogo));
//	}
//	
//	public ArrayList<Service> getServicesAsList()
//	{
//		return services;
//	}
//
//	public String[] getKeys()
//	{
//		String[] keys = new String[services.size()];
//		
//		for (int i = 0; i < keys.length; i++)
//		{
//			keys[i] = services.get(i).getKey();
//		}
//		
//		return keys;
//	}
//	
//	public int[] getIds()
//	{
//		int[] ids = new int[services.size()];
//		
//		for (int i = 0; i < ids.length; i++)
//		{
//			ids[i] = services.get(i).getId();
//		}
//		
//		return ids;
//	}
//}