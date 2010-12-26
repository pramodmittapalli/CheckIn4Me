//package com.davidivins.checkin4me;
//
//import com.google.android.apps.analytics.GoogleAnalyticsTracker;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//
///**
// * CheckIn4Me
// * 
// * @author david ivins
// */
//public class CheckIn4Me extends Activity
//{
//	private Handler handler = new Handler();
//	private StartProgramDelayer program = null;
//	
//	/**
//	 * onCreate
//	 * 
//	 * @param Bundle saved_instance_state
//	 */
//	@Override
//	public void onCreate(Bundle saved_instance_state)
//	{
//		super.onCreate(saved_instance_state);
//		setContentView(GeneratedResources.getLayout("checkin4me"));
//		
//		Analytics analytics = new Analytics(this);
//		GoogleAnalyticsTracker tracker = analytics.getTracker();
//        tracker.trackPageView("/checkin4me");
//        tracker.dispatch();
//        tracker.stop();
//		
//		runProgram();
//	}
//	
//	/**
//	 * onResume
//	 */
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		runProgram();
//	}
//	
//	/**
//	 * onStop
//	 */
//	@Override
//	public void onStop()
//	{
//		super.onStop();
//		
//		if (program != null)
//		{
//			handler.removeCallbacks(program);
//			program = null;
//		}		
//	}
//	
//	/**
//	 * runProgram
//	 */
//	private void runProgram()
//	{
//		if (program == null)
//			program = new StartProgramDelayer(this);
//		
//		handler.postDelayed(program, 2000);
//	}
//}
