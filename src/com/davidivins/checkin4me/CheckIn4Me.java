package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

/**
 * CheckIn4Me
 * 
 * @author david ivins
 */
public class CheckIn4Me extends Activity
{
	private Handler handler = new Handler();
	private StartProgram program = null;
	
	/**
	 * onCreate
	 * 
	 * @param Bundle saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		setContentView(R.layout.checkin4me);
		runProgram();
	}
	
	/**
	 * onResume
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		runProgram();
	}
	
	/**
	 * onStop
	 */
	@Override
	public void onStop()
	{
		super.onStop();
		
		if (program != null)
		{
			handler.removeCallbacks(program);
			program = null;
		}
	}
	
	/**
	 * runProgram
	 */
	private void runProgram()
	{
		if (program == null)
			program = new StartProgram(this);
		
		handler.postDelayed(program, 2000);
	}
	
	/**
	 * StartProgram
	 * 
	 * @author david
	 */
	class StartProgram implements Runnable
	{
		private Activity activity;
		
		/**
		 * StartProgram
		 * 
		 * @param activity
		 */
		StartProgram(Activity activity)
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
			
			startActivity(intent);
		}
	}
}
