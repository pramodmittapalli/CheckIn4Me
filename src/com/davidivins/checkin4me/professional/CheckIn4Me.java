package com.davidivins.checkin4me.professional;

import com.davidivins.checkin4me.*;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

/**
 * CheckIn4Me **PRO**
 * 
 * @author david ivins
 */
public class CheckIn4Me extends Activity
{
	private Handler handler = new Handler();
	private StartProgramDelayer program = null;
	
	/**
	 * onCreate
	 * 
	 * @param Bundle saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		
		GeneratedResources.generate(this);
		
		setContentView(GeneratedResources.getLayout("checkin4me"));
				
		Analytics analytics = new Analytics(this);
		GoogleAnalyticsTracker tracker = analytics.getTracker();
        tracker.trackPageView("/checkin4me_pro");
        tracker.dispatch();
        tracker.stop();
        
        // clear settings for this version
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//        String clear_settings_tag = "VERSION_1.6_HAS_CLEARED_SETTINGS";
//        
//        if (settings.getBoolean(clear_settings_tag, false) == false)
//        {
//        	Editor settings_editor = settings.edit();
//        	settings_editor.clear();
//        	settings_editor.putBoolean(clear_settings_tag, true);
//        	settings_editor.commit();
//        }
        
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
			program = new StartProgramDelayer(this);
		
		handler.postDelayed(program, 2000);
	}
}
