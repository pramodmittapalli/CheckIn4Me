package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class Authorization extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorization);
		
		//Services services = new Services();  
		long service_id = this.getIntent().getLongExtra("service id", 666);
		Log.e("CheckIn4Me", "service_id=" + service_id);
		
		Intent i = new Intent(Intent.ACTION_VIEW);

		i.setData(Uri.parse("https://gowalla.com/api/oauth/new" +
				"?redirect_uri=checkin4me:///"
				+ "&client_id=12f4a2ac2b4a4afb9aa87b7ee4a16f0a"
				+ "&scope=read-write"));
		
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
			Log.e("CheckIn4Me", "code=" + access_token);
			
			Intent i = new Intent(this, NearbyPlaces.class);
			startActivity(i);
			
		}
	}
}
