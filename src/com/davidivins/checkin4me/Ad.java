package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.Properties;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class Ad 
{
	private static final String TAG = "Ad";
	private Activity activity;
	private Bundle meta_data;
	private static Properties config = null;
	
	/**
	 * Ad
	 * 
	 * @param activity
	 */
	public Ad(Activity activity)
	{
		this.activity = activity;
		
		try
		{
			ApplicationInfo app_info = 
				activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
			meta_data = app_info.metaData;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Failed to get app info");
			meta_data = null;
		}
		
		if (null == meta_data || !meta_data.getBoolean("IS_PRO_VERSION", false))
		{
			initializeConfig();
		
			AdManager.setTestDevices(new String[] { 
					AdManager.TEST_EMULATOR,
					config.getProperty("test_phone_id", "")
				});
		
			AdManager.setPublisherId(config.getProperty("publisher_id", "-1"));
		}
		else
		{
			Log.i(TAG, "No ADMOB config file read. This is CheckIn4Me pro");
		}
	}
	
	/**
	 * initializeConfig
	 */
	private void initializeConfig()
	{
		if (null == config)
		{
			config = new Properties();
			
			try 
			{
				InputStream config_file = activity.getResources().openRawResource(R.raw.admob);
				config.load(config_file);
			} 
			catch (Exception e) 
			{
				Log.e(TAG, "Failed to open config file");
			}
		}
	}
	
	/**
	 * refreshAd
	 */
	public void refreshAd()
	{
		if (null == meta_data || !meta_data.getBoolean("IS_PRO_VERSION", false))
		{
			AdView ad = new AdView(activity);
			LinearLayout main_layout = (LinearLayout)activity.findViewById(R.id.main_layout);
			
			if (null != main_layout)
			{
				main_layout.addView(ad, 0);
				ad.setVisibility(View.VISIBLE);
				ad.setAdListener(new AdListener());
				ad.requestFreshAd();
			}
		}
		else
		{
			Log.i(TAG, "No AD added. This is CheckIn4Me pro");
		}
	}
}
