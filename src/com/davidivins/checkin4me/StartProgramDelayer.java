package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;

/**
 * StartProgram
 * 
 * @author david
 */
public class StartProgramDelayer implements Runnable
{
	private Activity activity;
	
	/**
	 * StartProgramDelayer
	 * 
	 * @param activity
	 */
	public StartProgramDelayer(Activity activity)
	{
		this.activity = activity;
	}

	/**
	 * run
	 */
	public void run() 
	{
		Intent intent;
		
		// go straight to nearby places if atleast one service is connected
		if (Services.getInstance(activity).atLeastOneConnected(PreferenceManager.getDefaultSharedPreferences(activity)))
			intent = new Intent(activity, NearbyPlaces.class);
		else
			intent = new Intent(activity, ServiceConnection.class);
		
		activity.startActivity(intent);
	}
}