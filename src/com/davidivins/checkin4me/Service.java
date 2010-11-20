package com.davidivins.checkin4me;

import android.content.SharedPreferences;

/**
 * Service
 * 
 * @author david
 */
public interface Service
{
	abstract public String getName();
	abstract public DrawableListItem getLogo();
	abstract public OAuthConnector getOAuthConnector();
	abstract public Request getNewOAuthRequest();
	abstract public boolean connected(SharedPreferences settings);
}
