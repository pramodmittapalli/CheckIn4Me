package com.davidivins.checkin4me;

import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class Authorization extends Activity
{	
	private static final String TAG = "Authorization";
	private int service_id;
	private OAuthConnector oauth_interface;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		setContentView(R.layout.authorization);

		Intent i = new Intent(Intent.ACTION_VIEW);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Editor settings_editor = settings.edit();
		
		// retrieve service id, default to -1 for none/error
		service_id = getIntent().getIntExtra("service_id", -1);
		Log.i(TAG, "service_id = " + service_id);
		
		//Services services = new Services();  

		switch (service_id)
		{
			//
			// FOURSQUARE SELECTED
			//
			case 0:
				try
				{
					oauth_interface = new FoursquareOAuthConnector(this.getResources());//settings);
					
					OAuthResponse response = (OAuthResponse)oauth_interface.beginHandshake();
					TreeMap<String, String> parameters = response.getQueryParameters();
					
					if (response.getSuccessStatus() && parameters.containsKey("oauth_token_secret") &&
							parameters.containsKey("oauth_token") && 
							parameters.containsKey("oauth_callback_confirmed") &&
							parameters.get("oauth_callback_confirmed").equals("true"))
					{
						settings_editor.putString("oauth_token_secret", parameters.get("oauth_token_secret"));
						settings_editor.putBoolean("handshake_in_progress", true);
						settings_editor.putInt("handshake_service_id", service_id);
						Log.i(TAG, "url = " + oauth_interface.generateAuthorizationURL(
								parameters.get("oauth_token")));
						i.setData(Uri.parse(oauth_interface.generateAuthorizationURL(
								parameters.get("oauth_token"))));
					}
					else
						Log.e(TAG, "failed to load Authorization intent.");
				}
				catch(Exception e)
				{
					Log.e(TAG, "EXCEPTION: " + e.getMessage());
				}
				
				break;
			
			//
			// GOWALLA SELECTED
			//
			case 1:
				Log.e(TAG, "Gowalla doesn't work yet :(");
				break;
				
			//
			// BRIGHTKITE SELECTED
			//
			case 3:
				Log.e(TAG, "Brightkite doesn't work yet :(");
				break;
			
			//
			// RE-ENTERING FROM AUTHORIZATION
			//
			default:
				
				if (settings.getBoolean("handshake_in_progress", false) && 
						settings.getInt("handshake_service_id", -1) == 0)
				{
					oauth_interface = new FoursquareOAuthConnector(this.getResources());
					Uri uri = this.getIntent().getData();
					
					if (null != uri)
					{
						Log.i(TAG, "uri is not null");
						Log.i(TAG, "oauth_token=" + uri.getQueryParameter("oauth_token"));
						Log.i(TAG, "oauth_verifier=" + uri.getQueryParameter("oauth_verifier"));
						
						if (uri.getQueryParameter("oauth_token") != null &&
								uri.getQueryParameter("oauth_verifier") != null)
						{
							String oauth_token = uri.getQueryParameter("oauth_token");
							String oauth_verifier = uri.getQueryParameter("oauth_verifier");
																			
							OAuthResponse response = (OAuthResponse)oauth_interface.completeHandshake(
									settings.getString("oauth_token_secret", null), oauth_token, oauth_verifier);
							
							if (response.getSuccessStatus())
							{
								Log.i(TAG, "got true from completing handshake");
								i = new Intent(this, NearbyPlaces.class);
							}
							else
							{
								Log.e(TAG, "request failed: " + response.getResponseString());
							}
						}
						else
						{
							Log.i(TAG, "query parameters not present");
						}
					}
					else
					{
						Log.i(TAG, "URI is null");
					}
				}
				else
				{
					Log.i(TAG, "handshake_in_progress = " + settings.getBoolean("handshake_in_progress", false));
					Log.i(TAG, "handshake_service_id = " + settings.getInt("handshake_service_id", -1));
				}
		}
		
		settings_editor.commit();
		startActivity(i);
	}
}