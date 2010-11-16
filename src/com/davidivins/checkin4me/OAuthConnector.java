package com.davidivins.checkin4me;

abstract class OAuthConnector
{
	abstract Response beginHandshake();
	abstract String generateAuthorizationURL(String oauth_token);
	abstract Response completeHandshake(String oauth_token_secret, String oauth_token, String oauth_verifier);
}
