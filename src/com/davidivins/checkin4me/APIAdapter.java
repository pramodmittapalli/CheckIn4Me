package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.content.SharedPreferences;

public interface APIAdapter 
{
	abstract public ArrayList<Locale> getLocations(String longitude, String latitude, SharedPreferences settings);
}
