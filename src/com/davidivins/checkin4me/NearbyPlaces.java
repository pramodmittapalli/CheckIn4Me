package com.davidivins.checkin4me;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
 * @author david ivins
 */
public class NearbyPlaces extends ListActivity implements LocationListener, OnItemClickListener, LocationsRetrieverListener, GPSTimeoutListener, NetworkTimeoutListener
{
	private static final String TAG = "NearbyPlaces";
	
	private static LocationManager location_manager = null;
	private static ProgressDialog loading_dialog = null;
	
	private String current_longitude;
	private String current_latitude;
	
	private String current_query = null;
	
	private final Handler handler = new Handler(); 
	
	private LocationsRetriever locations_runnable = null;
	
	boolean timeouts_cancelled = false;
	
	// acts as callback from locations thread
	private final Runnable locations_retrieved_callback = new Runnable() 
	{
		public void run() 
		{
			newLocationsAvailable();
		}
	};
	
	// acts as callback from gps thread
	private final Runnable gps_timeout_callback = new Runnable()
	{
		public void run()
		{
			if (!timeouts_cancelled)
				GPSTimeout();
		}
	};
	
	// acts as callback from network thread
	private final Runnable network_timeout_callback = new Runnable()
	{
		public void run()
		{
			if (!timeouts_cancelled)
				NetworkTimeout();
		}
	};
	
	/**
	 * onCreate
	 * 
	 * @param Bundle savedInstanceState
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		
		// handle the current intent of this activity
		handleIntent(getIntent());
		
		// display the add if this is not the pro version
		Ad ad = new Ad(this);
		ad.refreshAd();
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
	 * onSearchRequested
	 * 
	 * @return boolean
	 */
	@Override
	public boolean onSearchRequested() 
	{
		return super.onSearchRequested();
	}
	
	/**
	 * onNewIntent
	 * 
	 * @param Intent intent
	 */
	@Override
	protected void onNewIntent(Intent intent) 
	{
		setIntent(intent);
		handleIntent(intent);
	}
	
	/**
	 * handleIntent
	 * 
	 * @param intent
	 */
	private void handleIntent(Intent intent) 
	{
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
		{
			current_query = intent.getStringExtra(SearchManager.QUERY);
			this.setTitle("Results");
		}
		else
		{
			current_query = null;
			this.setTitle("Nearby Places");
		}
		
		Log.i(TAG, "current_query = " + current_query);
		
		requestCoordinates();

		setContentView(R.layout.nearby_places);
		
		getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.WHITE);
	}

	/**
	 * onLocationChanged
	 * 
	 * @param Locale location
	 */
	public void onLocationChanged(Location location) 
	{
		timeouts_cancelled = true;
		
		// cancel further updates
		location_manager.removeUpdates(this);
    	
		// cancel acquiring location dialog
		if (loading_dialog.isShowing())
			loading_dialog.cancel();

		// start retrieving locations dialog
		loading_dialog = ProgressDialog.show(this, "", "Retrieving Service Locations...", true);
    	
		// get longitude and latitude
		current_longitude = Double.toString(location.getLongitude());
		current_latitude = Double.toString(location.getLatitude());
		
		// get preferences for retrieving locations
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Editor settings_editor = settings.edit();
		
		// store user's current longitude and latitude
		settings_editor.putString("current_longitude", current_longitude);
		settings_editor.putString("current_latitude", current_latitude);
		settings_editor.commit();
		
		locations_runnable = new LocationsRetriever(this, this, handler, current_query, current_longitude, current_latitude, settings);
		Thread locations_thread = new Thread(locations_runnable, "LocationThread");
		locations_thread.start();
	}

	/**
	 * unused methods of GPS
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	public void onProviderEnabled(String provider) {}
	public void onProviderDisabled(String provider) {}
    
	/**
	 * requestCoordinates
	 */
	public void requestCoordinates()
	{
		timeouts_cancelled = false;
		
		// cancel acquiring location dialog
		if (loading_dialog != null && loading_dialog.isShowing())
			loading_dialog.cancel();
		
		// Acquire a reference to the system Location Manager
		if (null == location_manager)
			location_manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

		// start timeout thread for gps coordinates
		Thread gps_timeout_monitor = new Thread(new GPSTimeoutMonitor(this, handler), "GPSTimeoutMonitor");
		gps_timeout_monitor.start();
				
		// Register the listener with the Location Manager to receive location updates
		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)this);
		loading_dialog = ProgressDialog.show(this, "", "Acquiring GPS Location...", true);
	}

	
	/**
	 * GPSTimeout
	 */
	public void GPSTimeout()
	{
		Log.i(TAG, "GPSTimeout");
		
		// cancel acquiring location dialog
		if (loading_dialog != null && loading_dialog.isShowing())
		{
			loading_dialog.cancel();
			loading_dialog = null;
		}
		
		// Acquire a reference to the system Location Manager
		if (null == location_manager)
			location_manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		
		// start network location timeout
		Thread network_timeout_monitor = new Thread(new NetworkTimeoutMonitor(this, handler), "NetworkTimeoutMonitor");
		network_timeout_monitor.start();
		
		// Register the listener with the Location Manager to receive location updates
		location_manager.removeUpdates(this);
		location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener)this);
		
		// show loading dialog
		loading_dialog = ProgressDialog.show(this, "", "Acquiring Network Location...", true);
	}
	
	/**
	 * NetworkTimeout
	 */
	public void NetworkTimeout()
	{
		Log.i(TAG, "NetworkTimeout");
		
		// cancel acquiring location dialog
		if (loading_dialog.isShowing())
			loading_dialog.cancel();
		
		// remove location updates
		location_manager.removeUpdates(this);
		
		// show error
		Toast.makeText(this, "Location Services Unavailable.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * newLocationsAvailable
	 */
	public void newLocationsAvailable()
	{
		Log.i(TAG, "received new location data.");
		
		// setup list for retrieved locations
		LocaleAdapter adapter = new LocaleAdapter(this, R.layout.nearby_place_row, locations_runnable.getLocationsRetrieved());
		setListAdapter(adapter);
		
		// cancel loading dialog
		loading_dialog.cancel();
	}
	
	/**
	 * getLocationsRetrievedCallback
	 */
	public Runnable getLocationsRetrievedCallback()
	{
		return locations_retrieved_callback;
	}
	
	/**
	 * getGPSTimeoutCallback
	 */
	public Runnable getGPSTimeoutCallback()
	{
		return gps_timeout_callback;
	}
	
	/**
	 * getNetworkTimeoutCallback
	 */
	public Runnable getNetworkTimeoutCallback()
	{
		return network_timeout_callback;
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
		boolean result = false;
		
		// Handle item selection
		switch (item.getItemId()) 
		{
			case R.id.connect_services:
				startActivity(new Intent(this, ServiceConnection.class));
				result = true;
				break;
			case R.id.refresh:
				requestCoordinates();
				break;
			case R.id.search:
				onSearchRequested();
				break;
			default:
				// do nothing
		}
		
		return result;
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
		// get the settings and editor
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		// store selected location
		Locale location = locations_runnable.getLocationsRetrieved().get(position);
		location.store(settings);
		
		// load location details activity
		startActivity(new Intent(this, LocationDetails.class));
	}
}