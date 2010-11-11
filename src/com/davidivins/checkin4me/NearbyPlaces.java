package com.davidivins.checkin4me;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NearbyPlaces extends Activity
{
	private static final String TAG = "NearbyPlaces";
	private static final String oauth_token = "LU0OG2KGC0DH3G4QOIJ3L2H0NMEB1S4PCCM44TNWHHJTFMGM";
	private static final String oauth_token_secret = "PY0OB1KB2CCFHMYDLZNZE2DILZUQSAQVGQSDZKOQOPZ1RX1D";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearbyplaces);
		
		Log.i(TAG, "requesting...");
		
	}
}
		
		/*		String services[] = {
		"Foursquare",
		"Gowalla",
		"BrightKite",
		"Yelp",
		"Facebook" };

		setListAdapter(new ArrayAdapter<String>(this, R.layout.serviceconnection, services));
				
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
						// When clicked, show a toast with the TextView text
							Toast.makeText(getApplicationContext(), ((TextView)view).getText(),
									Toast.LENGTH_SHORT).show();
			}
		});*/