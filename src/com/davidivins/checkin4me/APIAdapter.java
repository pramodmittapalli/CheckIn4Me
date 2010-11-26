package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.content.SharedPreferences;

public interface APIAdapter 
{
	abstract public Runnable getLocationThread(String longitude, String latitude, SharedPreferences settings);
	abstract public ArrayList<Locale> getLatestLocations();
	abstract public Runnable getCheckInThread(Locale location, SharedPreferences settings);
	abstract public boolean getLatestCheckInStatus();
}
