package com.davidivins.checkin4me;

import android.app.Activity;
import android.os.Bundle;

public class NearbyPlaces extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearbyplaces);
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
	}
}