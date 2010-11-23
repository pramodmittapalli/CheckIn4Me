package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * NearbyPlaces
 * 
 * @author david
 */
public class NearbyPlaces extends ListActivity implements LocationListener, OnItemClickListener
{
	private static final String TAG = "NearbyPlaces";

	private static ArrayList<Locale> locations = new ArrayList<Locale>();
	private static LocationManager location_manager = null;
	private static ProgressDialog loading_dialog = null;
	
	private final Handler handler = new Handler(); 
	private Thread locations_thread;
	
	// acts as callback from thread
	final Runnable updateLocations = new Runnable() 
	{
		public void run() 
		{
			newLocationsAvailable();
		}
	};
	
	/**
	 * onCreate
	 * 
	 * @param Bundle savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Acquire a reference to the system Location Manager
		location_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)this);
	
		loading_dialog = ProgressDialog.show(this, "", "Acquiring GPS Location...", true);
		setContentView(R.layout.nearby_places);
	
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.WHITE);
	}
	
	/**
	 * onStop
	 */
	public void onStop()
	{
		super.onStop();
		
		// stop the gps when pausing the activity
		if (location_manager != null)
			location_manager.removeUpdates(this);
		
		// cancel any dialogs showing
		if (loading_dialog != null && loading_dialog.isShowing())
			loading_dialog.cancel();
	}

	/**
	 * onLocationChanged
	 * 
	 * @param Locale location
	 */
    public void onLocationChanged(Location location) 
    {
    	// cancel further updates
    	location_manager.removeUpdates(this);
    	
    	// cancel acquiring location dialog
    	if (loading_dialog.isShowing())
    		loading_dialog.cancel();

    	// start retrieving locations dialog
    	loading_dialog = ProgressDialog.show(this, "", "Retrieving Service Locations...", true);
    	
    	// get longitude and latitude
    	String longitude = Double.toString(location.getLongitude());
    	String latitude = Double.toString(location.getLatitude());
		
    	// get preferences for retrieving locations
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		locations_thread = new Thread(new LocationThread(this, longitude, latitude, settings), "LocationThread");
		locations_thread.start();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}
	
    public void newLocationsAvailable()
    {
    	Log.i(TAG, "received new location data.");
    	
    	// join thread even though we know it already completed
    	try 
    	{
    		if (locations_thread != null)
    			locations_thread.join();
		} 
    	catch (InterruptedException e) 
		{
			Log.i(TAG, "Thread interrupted already");
		}

    	if (locations.isEmpty())
			Log.i(TAG, "service locations retrieved successfully and are empty as expected.");
    	else
    		Log.i(TAG, "WE HAVE LOCATIONS!!");
    	
    	//getMockLocations();
		
		// setup list for retrieved locations
		LocaleAdapter adapter = new LocaleAdapter(this, R.layout.nearby_place_row, locations);
		setListAdapter(adapter);
		
		// cancel loading dialog
		loading_dialog.cancel();
    }
    
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
				startActivity(new Intent(this, ServiceConnection.class));
				return true;
			default:
				return false;
		}
	}

	/**
	 * onItemClick
	 * 
	 * @param AdapterView<?> adapter_view
	 * @param View view
	 * @param int  
	 * @param long arg3
	 */
	public void onItemClick(AdapterView<?> adapter_view, View view, int position, long arg3) 
	{
		Locale location = locations.get(position);
		
		Toast.makeText(this, "Location Name: " + location.getName() + 
				"\n Longitude: " + location.getLongitude() + 
				"\n Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
	}
	
	
	class LocationThread implements Runnable
	{
		Activity activity;
		String longitude;
		String latitude;
		SharedPreferences settings;
		
		LocationThread(Activity activity, String longitude, String latitude, SharedPreferences settings)
		{
			this.activity = activity;
			this.longitude = longitude;
			this.latitude = latitude;
			this.settings = settings;
		}
		
		public void run() 
		{
			locations.clear();
			locations = Services.getInstance(activity).getAllLocations(longitude, latitude, settings);
			handler.post(updateLocations);
		}
	}
	
	private void getMockLocations()
	{
		Locale location = new Locale("Dave's House", "", "666", "6666");
		location.mapServiceIdToLocationId(0, "");
		location.mapServiceIdToLocationId(1, "");
		location.mapServiceIdToLocationId(2, "");
		locations.add(location);
		
		Locale location0 = new Locale("Dave's Gay Neighbors' House", "", "000", "0000");
		location0.mapServiceIdToLocationId(0, "");
		location0.mapServiceIdToLocationId(1, "");
		location0.mapServiceIdToLocationId(2, "");
		locations.add(location0);
		
		Locale location1 = new Locale("Genise's House", "", "13", "1313");
		location1.mapServiceIdToLocationId(0, "");
		location1.mapServiceIdToLocationId(2, "");
		locations.add(location1);
		
		Locale location2 = new Locale("Taco Bell", "", "111", "1111");
		location2.mapServiceIdToLocationId(0, "");
		location2.mapServiceIdToLocationId(1, "");
		locations.add(location2);
		
		Locale location3 = new Locale("Wawa", "", "222", "2222");
		location3.mapServiceIdToLocationId(0, "");
		location3.mapServiceIdToLocationId(1, "");
		location3.mapServiceIdToLocationId(2, "");
		locations.add(location3);
		
		Locale location4 = new Locale("Applebees", "", "999", "9999");
		location4.mapServiceIdToLocationId(1, "");
		location4.mapServiceIdToLocationId(2, "");
		locations.add(location4);
		
		for (int i = 0; i < 5; i++)
		{
			Locale location5 = new Locale("Party Central", "", Integer.toString(i), Integer.toString(i * 5));
			location5.mapServiceIdToLocationId(2, "");
			locations.add(location5);
		}
	}
}