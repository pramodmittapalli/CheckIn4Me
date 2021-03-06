package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.content.SharedPreferences;

/**
 * APIAdapter
 * 
 * @author david ivins
 */
public interface APIAdapter 
{
	abstract public Runnable getLocationThread(String query, String longitude, String latitude, SharedPreferences settings);
	abstract public ArrayList<Locale> getLatestLocations();
	abstract public Runnable getCheckInThread(Locale location, SharedPreferences settings);
	abstract public boolean getLatestCheckInStatus();
}
