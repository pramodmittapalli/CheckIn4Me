package com.davidivins.checkin4me;

import android.content.SharedPreferences;

/**
 * Service
 * 
 * @author david ivins
 */
public interface Service
{
	abstract public int getId();
	abstract public String getName();
	abstract public DrawableListItem getLogo();
	abstract public int getIconDrawable();
	abstract public OAuthConnector getOAuthConnector();
	abstract public APIAdapter getAPIAdapter();
	abstract public boolean connected(SharedPreferences settings);
}
