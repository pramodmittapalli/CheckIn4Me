package com.davidivins.checkin4me;
import android.util.Log;

import com.admob.android.ads.AdView;
import com.admob.android.ads.SimpleAdListener;

public class AdListener extends SimpleAdListener
{
	private static final String TAG = "AdListener";
	
	@Override
	public void onFailedToReceiveAd(AdView ad) 
	{
		Log.w (TAG, "failed to receive ad");
		super.onFailedToReceiveAd(ad);
	}

	@Override
	public void onFailedToReceiveRefreshedAd(AdView ad) 
	{
		Log.w (TAG, "failed to receive refreshed ad");
		super.onFailedToReceiveRefreshedAd(ad);
	}

	@Override
	public void onReceiveAd(AdView ad) 
	{
		Log.w (TAG, "receive ad");
		super.onReceiveAd(ad);
	}

	@Override
	public void onReceiveRefreshedAd(AdView ad) 
	{
		Log.w (TAG, "receive refreshed ad");
		super.onReceiveRefreshedAd(ad);
	}
}