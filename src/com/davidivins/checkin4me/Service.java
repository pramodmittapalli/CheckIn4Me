package com.davidivins.checkin4me;

public interface Service
{
	abstract public String getName();
	abstract public DrawableListItem getLogo();
	abstract public OAuthConnector getOAuthConnector();
	abstract public Request getNewOAuthRequest();
}
