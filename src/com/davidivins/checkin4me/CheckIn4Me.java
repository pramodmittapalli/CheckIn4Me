package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * CheckIn4Me
 * 
 * @author david
 */
public class CheckIn4Me extends Activity
{
	/**
	 * onCreate
	 * 
	 * @param Bundle saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		Intent intent;
		
		// go straight to nearby places if atleast one service is connected
		if (Services.getInstance(this).atLeastOneConnected(PreferenceManager.getDefaultSharedPreferences(this)))
			intent = new Intent(this, NearbyPlaces.class);
		else
			intent = new Intent(this, ServiceConnection.class);
		
		startActivity(intent);
	}
}
