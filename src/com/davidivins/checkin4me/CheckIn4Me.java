package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class CheckIn4Me extends Activity 
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Intent i = new Intent(Intent.ACTION_VIEW);

		//i.setData(Uri.parse("https://gowalla.com/api/oauth/new" +
		//		"?redirect_uri=checkin4me:///"
		//		+ "&client_id=12f4a2ac2b4a4afb9aa87b7ee4a16f0a"
		//		+ "&scope=read-write"));
        
		//i.setData(Uri.parse("https://gowalla.com/api/oauth/token?grant_type=refresh_token&client_id=12f4a2ac2b4a4afb9aa87b7ee4a16f0a&client_secret=abd5672efe21467b81dabc78f1f1f849&refresh_token=f0560374b3c4d29db0baaa15540a9fb5&scope=read-write"));
		
		//Intent i = new Intent(this, Authorization.class);
		//Intent i = new Intent(this, ServiceSelection.class);
		//startActivity(i);
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
			
			Intent i = new Intent(this, CI4M_TEST.class);
			startActivity(i);
			
		}
	}
}