package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Authorization
 * 
 * @author david
 */
public class Authorization extends Activity
{	
	private static final String TAG = "Authorization";

	/**
	 * onCreate
	 * 
	 * @param Bundle saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		setContentView(R.layout.authorization);
		Intent i = new Intent(Intent.ACTION_VIEW);
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Editor settings_editor = settings.edit();

		// check if a service was clicked to get here
		if (getIntent().getIntExtra("service_id", -1) != -1)
		{
			// retrieve service id, default to -1 for none/error
			int service_id = getIntent().getIntExtra("service_id", -1);
			Log.i(TAG, "service_id = " + service_id);
			
			// get oauth connector
			OAuthConnector oauth_connector = 
				Services.getInstance(this).getServiceById(service_id).getOAuthConnector();
			
			// begin handshake process
			OAuthResponse response = (OAuthResponse)oauth_connector.beginHandshake();
			
			// check if successful initial response is received
			// if so, store necessary data and generate authorization url
			if (oauth_connector.isSuccessfulInitialResponse(response))
			{
				settings_editor.putBoolean("handshake_in_progress", true);
				settings_editor.putInt("handshake_service_id", service_id);
				
				oauth_connector.storeNecessaryInitialResponseData(settings_editor, response);
				i.setData(Uri.parse(oauth_connector.generateAuthorizationURL(settings)));
			}
		}
		// check if we are returning here from the middle of an oauth handshake
		else if (settings.getBoolean("handshake_in_progress", false) && 
				settings.getInt("handshake_service_id", -1) != -1)
		{
			// get the oauth connector for the service currently in the middle of a handshake
			int service_id = settings.getInt("handshake_service_id", -1);
			OAuthConnector oauth_connector = 
				Services.getInstance(this).getServiceById(service_id).getOAuthConnector();
			
			// get the response for the authorization request
			Uri uri = this.getIntent().getData();
			
			// check if the authorization request was successful
			if (oauth_connector.isSuccessfulAuthorizationResponse(uri))
			{				
				// store necessary response data
				oauth_connector.storeNecessaryAuthorizationResponseData(settings_editor, uri);
				
				// attempt to complete the handshake
				OAuthResponse response = (OAuthResponse)oauth_connector.completeHandshake(settings, uri);
				
				// check if the completion response is valid
				if (oauth_connector.isSuccessfulCompletionResponse(response))
				{
					settings_editor.putBoolean("handshake_in_progress", false);
					settings_editor.putInt("handshake_service_id", -1);
					
					// store necessary response data
					oauth_connector.storeNecessaryCompletionResponseData(settings_editor, response);
					
					// start nearby places event
					i = new Intent(this, NearbyPlaces.class);
				}
				else
				{
					Log.e(TAG, "Failed to complete handshake: " + response.getResponseString());
					Toast.makeText(getApplicationContext(), "Failed to complete handshake.", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Log.e(TAG, "Failed to authorize app: " + uri.toString());
				Toast.makeText(getApplicationContext(), "Failed to authorize app.", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Log.i(TAG, "No service clicked and no handshake in progress");
			Toast.makeText(getApplicationContext(), "No service clicked and no handshake in progress.", 
					Toast.LENGTH_SHORT).show();
		}
		
		settings_editor.commit();
		startActivity(i);
	}
}