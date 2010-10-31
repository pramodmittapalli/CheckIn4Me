package com.davidivins.checkin4me;

import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class Authorization extends Activity
{	
	private static final String TAG = "Authorization";
	private int service_id;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorization);
		
		// retrieve service id, default to -1 for none/error
		service_id = getIntent().getIntExtra("service_id", -1);
		Log.i(TAG, "service_id = " + service_id);
		
		//Services services = new Services();  
		
		Intent i = new Intent(Intent.ACTION_VIEW);

		switch (service_id)
		{
			case 0:

//				int timestamp = (int)(System.currentTimeMillis() / 1000L);
				Random random = new Random();
//				String nonce = Long.toString(Math.abs(random.nextLong()), 60000);
//				
//				//String signature = "";
//				String signature_method = "HMAC-SHA1";
//				
//				String url = "https://foursquare.com/oauth/request_token"; 
//				//"?"//redirect_uri=checkin4me:///"
//				String uri = 
//					"oauth_consumer_key=1AOTUF0OB4UQQWVS1DFDQWD3LKEBPWOCQPRDP4NYATJCUEAW"
//					+ "&oauth_nonce=" + nonce
//					+ "&oauth_signature_method=" + signature_method
//					+ "&oauth_timestamp=" + timestamp
//					+ "&oauth_version=1.0";
//				String tmp = "";
//				try {tmp = URLEncoder.encode(url, "ISO-8859-1") + "&" + URLEncoder.encode(uri, "ISO-8859-1");}catch(Exception e){Log.wtf(TAG, "wut");}
//				String signature = sign("GET&" + tmp, "ID1ALZP5XUA4YMJ3GSB3Q3GC2NZZFT0VPVYN5JMEBBG0QMEK");
//				i.setData(Uri.parse(url + "?" + uri + "&oauth_signature=" + signature));
//				Log.i(TAG, "here4");
				
//			    try
//			    {
//			      HashMap<String, String> parameters = new HashMap<String, String>();
//
//			      String host = "https://api.twitter.com/oauth/request_token";
//			      String oauth_consumer_secret = "MCD8BKwGdgPHvAuvgvz4EQpqDAtx89grbuNMRd7Eh98&";
//
//			      parameters.put("oauth_callback", URLEncoder.encode("http://localhost:3005/the_dance/process_callback?service_provider_id=11", "ISO-8859-1"));
//			      parameters.put("oauth_consumer_key", "GDdmIQH6jhtmLUypg82g");
//			      parameters.put("oauth_nonce", "QP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk");
//			      parameters.put("oauth_signature_method", "HMAC-SHA1");
//			      parameters.put("oauth_timestamp", "1272323042");
//			      parameters.put("oauth_version", "1.0");
//
//			      String uri = "";
//
//			      uri +=       "oauth_callback"         + "=" + parameters.get("oauth_callback");
//			      uri += "&" + "oauth_consumer_key"     + "=" + parameters.get("oauth_consumer_key");
//			      uri += "&" + "oauth_nonce"            + "=" + parameters.get("oauth_nonce");
//			      uri += "&" + "oauth_signature_method" + "=" + parameters.get("oauth_signature_method");
//			      uri += "&" + "oauth_timestamp"        + "=" + parameters.get("oauth_timestamp");
//			      uri += "&" + "oauth_version"          + "=" + parameters.get("oauth_version");
//
//			      String url = host + "?" + uri;
//			      String base_string = "POST&" + URLEncoder.encode(host, "ISO-8859-1") + "&" + URLEncoder.encode(uri, "ISO-8859-1");
//			      String should_be = "POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&oauth_callback%3Dhttp%253A%252F%252Flocalhost%253A3005%252Fthe_dance%252Fprocess_callback%253Fservice_provider_id%253D11%26oauth_consumer_key%3DGDdmIQH6jhtmLUypg82g%26oauth_nonce%3DQP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1272323042%26oauth_version%3D1.0";
//
//			      Log.i(TAG, "url = " + url);
//			      Log.i(TAG, "");
//			      Log.i(TAG, "base_string = " + base_string);
//			      Log.i(TAG, "should be   = " + should_be);
//			      if (base_string.equals(should_be)) System.out.println("they are!"); else System.out.println("they aren't! :(");
//			      Log.i(TAG, "");
//			      Log.i(TAG, "");
//
//			      String signature = sign(base_string, oauth_consumer_secret);
//			      Log.i(TAG, "output should be = 8wUi7m5HFQy76nowoCThusfgB+Q=");
//			      Log.i(TAG, "output actually  = " + signature);
//			    }
//			    catch(Exception e)
//			    {
//			    	Log.e(TAG, e.getMessage());
//			    }

			    try
			    {
			      HashMap<String, String> parameters = new HashMap<String, String>();

			      String host = "https://foursquare.com/oauth/request_token";
			      String oauth_consumer_secret = "ID1ALZP5XUA4YMJ3GSB3Q3GC2NZZFT0VPVYN5JMEBBG0QMEK"+"&";

			      parameters.put("oauth_callback", URLEncoder.encode("checkin4me:///", "ISO-8859-1"));
			      parameters.put("oauth_consumer_key", "1AOTUF0OB4UQQWVS1DFDQWD3LKEBPWOCQPRDP4NYATJCUEAW");
			      parameters.put("oauth_nonce", Long.toString(Math.abs(random.nextLong()), 60000));
			      parameters.put("oauth_signature_method", "HMAC-SHA1");
			      parameters.put("oauth_timestamp", Integer.toString((int)(System.currentTimeMillis() / 1000L)));
			      parameters.put("oauth_version", "1.0");

			      String uri = "";

			      uri +=       "oauth_callback"         + "=" + parameters.get("oauth_callback");
			      uri += "&" + "oauth_consumer_key"     + "=" + parameters.get("oauth_consumer_key");
			      uri += "&" + "oauth_nonce"            + "=" + parameters.get("oauth_nonce");
			      uri += "&" + "oauth_signature_method" + "=" + parameters.get("oauth_signature_method");
			      uri += "&" + "oauth_timestamp"        + "=" + parameters.get("oauth_timestamp");
			      uri += "&" + "oauth_version"          + "=" + parameters.get("oauth_version");

			      String url = host + "?" + uri;
			      String base_string = "GET&" + URLEncoder.encode(host, "ISO-8859-1") + "&" + URLEncoder.encode(uri, "ISO-8859-1");

			      Log.i(TAG, "url = " + url);
			      Log.i(TAG, "base_string = " + base_string);

			      String signature = sign(base_string, oauth_consumer_secret);
			      Log.i(TAG, "output actually  = " + signature);
			      
			      i.setData(Uri.parse(url + "&oauth_signature=" + signature));
			    }
			    catch(Exception e)
			    {
			    	Log.e(TAG, e.getMessage());
			    }
				
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
		}
		
		Log.i(TAG, "here5");

		startActivity(i);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		Uri uri = this.getIntent().getData();
		if (null != uri)
		{
			String access_token = uri.getQueryParameter("code");
			Log.e(TAG, "code=" + access_token);
			
			Intent i = new Intent(this, NearbyPlaces.class);
			startActivity(i);
			
		}
	}
	
	private String sign(String in_str, String in_key)
	{
		String out_str = "";

		try
		{
		      SecretKey key = new SecretKeySpec(in_key.getBytes(), "HmacSHA1");

		      Mac m = Mac.getInstance("HmacSHA1");
		      m.init(key);

		      byte[] mac = m.doFinal(in_str.getBytes());

		      //out_str = Base64.encodeToString(mac, Base64.DEFAULT); // worked for twitter example
		      out_str = Base64.encodeToString(mac, Base64.NO_WRAP);
		      out_str = URLEncoder.encode(out_str, "ISO-8859-1");
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
		
		Log.i(TAG, "out_sig=" + out_str);
		return out_str;
	}
}
