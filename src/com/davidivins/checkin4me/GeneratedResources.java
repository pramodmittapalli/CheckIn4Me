package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class GeneratedResources
{
	private static final String TAG = "GeneratedResources";
	private static GeneratedResourcesInterface generated_resources;
	private static String version;
	
	private GeneratedResources() { }
	
	public static void generate(Activity activity)
	{
		Bundle meta_data = null;
		
		try
		{
			ApplicationInfo app_info = 
				activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
			meta_data = app_info.metaData;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Failed to get app info");
		}
		
		if (null != meta_data)
		{
			// assume pro, don't want to screw up paying customers
			version = (meta_data.getString("VERSION") == null) ? "professional" : meta_data.getString("VERSION");			
			String class_name = "com.davidivins.checkin4me." + version + ".R";
			
			generated_resources = new ParsedGeneratedResources(class_name);
		}
		else // assume pro, don't want to screw up paying customers
		{
			generated_resources = new ParsedGeneratedResources("com.davidivins.checkin4me.pro.R");
		}
	}
	
	public static boolean areNotGenerated()
	{
		return (null == generated_resources) ? true : false;
	}
	
	public static String getVersion()
	{
		return version;
	}
	
	public static final int getAttr(String name)
	{
		return generated_resources.getAttr(name);
	}
	
	public static final int getColor(String name)
	{
		return generated_resources.getColor(name);
	}
	
	public static final int getDrawable(String name)
	{
		return generated_resources.getDrawable(name);
	}
	
	public static final int getId(String name)
	{
		return generated_resources.getId(name);
	}
	
	public static final int getLayout(String name)
	{
		return generated_resources.getLayout(name);
	}

	public static final int getMenu(String name)
	{
		return generated_resources.getMenu(name);
	}
	
	public static final int getRaw(String name)
	{
		return generated_resources.getRaw(name);
	}
	
	public static final int getString(String name)
	{
		return generated_resources.getString(name);
	}
	
	public static final int getXml(String name)
	{
		return generated_resources.getXml(name);
	}
}
