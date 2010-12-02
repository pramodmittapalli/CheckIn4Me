package com.davidivins.checkin4me;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;

/**
 * OAuthConnector
 * 
 * @author david ivins
 */
abstract interface OAuthConnector
{
	abstract OAuthResponse beginHandshake();
	abstract boolean isSuccessfulInitialResponse(OAuthResponse response);
	abstract void storeNecessaryInitialResponseData(Editor settings_editor, OAuthResponse response);
	abstract String generateAuthorizationURL(SharedPreferences settings);
	abstract boolean isSuccessfulAuthorizationResponse(Uri response);
	abstract void storeNecessaryAuthorizationResponseData(Editor settings_editor, Uri response);
	abstract OAuthResponse completeHandshake(SharedPreferences settings, Uri previous_response);
	abstract boolean isSuccessfulCompletionResponse(OAuthResponse response);
	abstract void storeNecessaryCompletionResponseData(Editor settings_editor, OAuthResponse response);
	abstract void clearTemporaryData(Editor settings_editor);
}
