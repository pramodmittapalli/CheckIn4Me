package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class Authorization extends Activity
{	
	private static final String TAG = "Authorization";
	private int service_id;
	private OAuth oauth_interface;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		SharedPreferences settings = this.getPreferences(MODE_WORLD_WRITEABLE);
//		if (saved_instance_state !=null)
//		{
//		Log.i(TAG, "loading interface");
//		 oauth_interface = (FoursquareOAuth)saved_instance_state.getSerializable("oauth_interface");
//		}
//		else
//		{
//			Log.i(TAG, "Creating new bundle.");
//			//saved_instance_state = new Bundle();
//		}

		setContentView(R.layout.authorization);
		
		// retrieve service id, default to -1 for none/error
		service_id = getIntent().getIntExtra("service_id", -1);
		//Log.i(TAG, "service_id = " + service_id);
		
		//Services services = new Services();  
		
		Intent i = new Intent(Intent.ACTION_VIEW);

		switch (service_id)
		{
			case 0:
				try
				{
					oauth_interface = new FoursquareOAuth(settings);
					
					if (oauth_interface.beginHandshake())
					{
						i.setData(Uri.parse(oauth_interface.generateAuthorizationURL()));
					}
					else
					{
						Log.e(TAG, "failed to load Authorization intent.");
					}
				}
				catch(Exception e)
				{
					Log.e(TAG, "EXCEPTION: " + e.getMessage());
				}
				
//				if (null == saved_instance_state)
//					Log.i(TAG, "null1");
//				if (null == oauth_interface)
//					Log.i(TAG, "null2");
				//Log.i(TAG, "saving interface");
				//saved_instance_state.putSerializable("oauth_interface", oauth_interface);
				//Log.i(TAG, "saving interface success");
				//this.onSaveInstanceState(saved_instance_state);
				break;
				
			case 1:
				i.setData(Uri.parse("https://gowalla.com/api/oauth/new" 
						+ "?redirect_uri=checkin4me:///"
						+ "&client_id=12f4a2ac2b4a4afb9aa87b7ee4a16f0a"
						+ "&scope=read-write"));
				break;
				
			case 3:
				Log.e(TAG, "Brightkite doesn't work yet :(");
				break;
				
			default:
				Log.e(TAG, "Unrecognized service id = " + service_id);
				
				Log.i(TAG, "resumed");
				Log.i(TAG, "past super's resume");
				oauth_interface = new FoursquareOAuth(settings);
				Uri uri = this.getIntent().getData();
				Log.i(TAG, "got uri");
				if (null != uri)
				{
					Log.i(TAG, "uri is not null");
					Log.i(TAG, "oauth_token=" + uri.getQueryParameter("oauth_token"));
					Log.i(TAG, "oauth_verifier=" + uri.getQueryParameter("oauth_verifier"));
					
					//String oauth_token = uri.getQueryParameter("oauth_token");
					//String oauth_verifier = uri.getQueryParameter("oauth_verifier");
										
					if (oauth_interface == null)
						Log.i(TAG, "balls");
					else
						Log.i(TAG, "AWESOME");
					oauth_interface.processAuthorizationResponseURI(uri);//oauth_token, oauth_verifier);
					Log.i(TAG, "uri processed");
					if (oauth_interface.completeHandshake())
					{
						Log.i(TAG, "got true from completing handshake");
						i = new Intent(this, NearbyPlaces.class);
						//startActivity(i);
					}
				}
		}
		
		startActivity(i);
		//this.startActivityForResult(i, 1);
	}

//	@Override
//	protected void onResume()
//	{
		
//		Log.i(TAG, "resumed");
//		super.onResume();
//		Log.i(TAG, "past super's resume");
//		Uri uri = this.getIntent().getData();
//		Log.i(TAG, "got uri");
//		if (null != uri)
//		{
//			Log.i(TAG, "uri is not null");
//			Log.i(TAG, "oauth_token=" + uri.getQueryParameter("oauth_token"));
//			Log.i(TAG, "oauth_verifier=" + uri.getQueryParameter("oauth_verifier"));
//			
//			String oauth_token = uri.getQueryParameter("oauth_token");
//			String oauth_verifier = uri.getQueryParameter("oauth_verifier");
//			
//			if (oauth_interface == null)
//				Log.i(TAG, "balls");
//			oauth_interface.processAuthorizationResponseURI(oauth_token, oauth_verifier);
//			Log.i(TAG, "uri processed");
//			if (oauth_interface.completeHandshake())
//			{
//				Log.i(TAG, "got true from completing handshake");
//				Intent i = new Intent(this, NearbyPlaces.class);
//				startActivity(i);
//			}
//		}
//		Uri uri = this.getIntent().getData();
//		if (null != uri)
//		{
//			String access_token = uri.getQueryParameter("code");
//			Log.e(TAG, "code=" + access_token);
//			
//			Intent i = new Intent(this, NearbyPlaces.class);
//			startActivity(i);
//			
//		}
//	}
	
//	@Override
//	public void onSaveInstanceState(Bundle saved_instance_state) {
//		Log.i(TAG, "Saving state");
//	  saved_instance_state.putSerializable("oauth_interface", oauth_interface);
//	  super.onSaveInstanceState(saved_instance_state);
//	}
//	
//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//	  super.onRestoreInstanceState(savedInstanceState);
//	  Log.i(TAG, "Restoring state...");
//	  oauth_interface = (FoursquareOAuth)savedInstanceState.getSerializable("oauth_interface");
//	}
}

//HashMap<String, String> parameters = new HashMap<String, String>();
//Random random = new Random();
//
//String host = "https://foursquare.com/oauth/request_token";
//String oauth_consumer_secret = "ID1ALZP5XUA4YMJ3GSB3Q3GC2NZZFT0VPVYN5JMEBBG0QMEK"+"&";
//
//parameters.put("oauth_callback", URLEncoder.encode("checkin4me:///", "ISO-8859-1"));
//parameters.put("oauth_consumer_key", "1AOTUF0OB4UQQWVS1DFDQWD3LKEBPWOCQPRDP4NYATJCUEAW");
//parameters.put("oauth_nonce", Long.toString(Math.abs(random.nextLong()), 60000));
//parameters.put("oauth_signature_method", "HMAC-SHA1");
//parameters.put("oauth_timestamp", Integer.toString((int)(System.currentTimeMillis() / 1000L)));
//parameters.put("oauth_version", "1.0");
//
//String uri = "";
//uri +=       "oauth_callback"         + "=" + parameters.get("oauth_callback");
//uri += "&" + "oauth_consumer_key"     + "=" + parameters.get("oauth_consumer_key");
//uri += "&" + "oauth_nonce"            + "=" + parameters.get("oauth_nonce");
//uri += "&" + "oauth_signature_method" + "=" + parameters.get("oauth_signature_method");
//uri += "&" + "oauth_timestamp"        + "=" + parameters.get("oauth_timestamp");
//uri += "&" + "oauth_version"          + "=" + parameters.get("oauth_version");
//
//String url = host + "?" + uri;
//String base_string = "GET&" + URLEncoder.encode(host, "ISO-8859-1") + "&" + URLEncoder.encode(uri, "ISO-8859-1");
//
//Log.i(TAG, "url = " + url);
//Log.i(TAG, "base_string = " + base_string);
//
//String signature = sign(base_string, oauth_consumer_secret);
////String signature = oa.getSignedRequestTokenUrl();
//Log.i(TAG, "output actually  = " + signature);
//
//i.setData(Uri.parse(url + "&oauth_signature=" + signature));
//private String sign(String in_str, String in_key)
//{
//	String out_str = "";
//
//	try
//	{
//		SecretKey key = new SecretKeySpec(in_key.getBytes(), "HmacSHA1");
//
//		Mac m = Mac.getInstance("HmacSHA1");
//		m.init(key);
//
//		byte[] mac = m.doFinal(in_str.getBytes());
//
//		out_str = Base64.encodeToString(mac, Base64.NO_WRAP);
//		out_str = URLEncoder.encode(out_str, "ISO-8859-1");
//	}
//	catch(Exception e)
//	{
//		Log.e(TAG, e.getMessage());
//	}
//	
//	Log.i(TAG, "signature calculated = " + out_str);
//	return out_str;
//}