package com.davidivins.checkin4me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class NearbyPlaces extends Activity implements LocationListener
{
	private static final String TAG = "NearbyPlaces";

	/**
	 * onCreate
	 * 
	 * @param Bundle savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_places);
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)this);
		
		SharedPreferences p = this.getPreferences(MODE_WORLD_WRITEABLE);
		Log.i(TAG, "test! = " + p.getString("oauth_token", "-1")); // shared preferences not shared across activities :(
	}

	/**
	 * onLocationChanged
	 * 
	 * @param Location location
	 */
    public void onLocationChanged(Location location) 
    {
    	String latitude = Double.toString(location.getLatitude());
    	String longitude = Double.toString(location.getLongitude());
    	Log.i(TAG, "here");
    	Toast.makeText(getApplicationContext(), "Lat = " + latitude + ", Long = " + longitude, Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}
	
	/**
	 * onCreateOptionsMenu
	 * 
	 * @param Menu menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nearby_places, menu);
		return true;
	}
	
	/**
	 * onOptionsItemSelected
	 * 
	 * @param MenuItem item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle item selection
		switch (item.getItemId()) 
		{
			case R.id.connect_services:
				Intent i = new Intent(this, ServiceConnection.class);
				i.putExtra("force", true);
				startActivity(i);
				return true;
			default:
				return false;
		}
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