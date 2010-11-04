package com.davidivins.checkin4me;

import android.net.Uri;

interface OAuth
{
	abstract boolean beginHandshake();
	abstract String generateAuthorizationURL();
	abstract void processAuthorizationResponseURI(String x, String y);//Uri uri);
	abstract boolean completeHandshake();
	abstract boolean hasAccessToken();
	abstract String getAccessToken();
}
