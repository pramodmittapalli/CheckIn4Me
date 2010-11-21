package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
	//private static final String TAG = "NearbyPlaces";

	private static ArrayList<Locale> locations = new ArrayList<Locale>();
	
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
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)this);
		
		setContentView(R.layout.nearby_places);
		
		locations.clear();
		getMockLocations();
		
		LocaleAdapter adapter = new LocaleAdapter(this, R.layout.nearby_place_row, locations);
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.WHITE);
		getListView().setOnItemClickListener(this);
	}

	/**
	 * onLocationChanged
	 * 
	 * @param Locale location
	 */
    public void onLocationChanged(Location location) 
    {
    	String latitude = Double.toString(location.getLatitude());
    	String longitude = Double.toString(location.getLongitude());
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