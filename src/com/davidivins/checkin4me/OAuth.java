package com.davidivins.checkin4me;

import java.io.Serializable;

import android.net.Uri;

abstract class OAuth implements Serializable
{
	abstract boolean beginHandshake();
	abstract String generateAuthorizationURL();
	abstract void processAuthorizationResponseURI(Uri uri);
	abstract boolean completeHandshake();
	abstract boolean hasAccessToken();
	abstract String getAccessToken();
}
