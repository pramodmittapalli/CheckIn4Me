package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * NearbyPlaces
 * 
 * @author david
 */
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
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener)this);

		ScrollView sv = new ScrollView(this);
		LinearLayout place = new LinearLayout(this);
		LinearLayout text_line = new LinearLayout(this);
		LinearLayout icon_line = new LinearLayout(this);
		place.setOrientation(LinearLayout.VERTICAL);
		text_line.setOrientation(LinearLayout.HORIZONTAL);
		icon_line.setOrientation(LinearLayout.HORIZONTAL);
		place.addView(text_line);
		place.addView(icon_line);
		sv.addView(place);
		
		TextView tv = new TextView(this);
		tv.setText("Some Place!!");
		text_line.addView(tv);
		
		ImageView fs = new ImageView(this);
        fs.setImageResource(R.drawable.foursquare20x20);        
        icon_line.addView(fs);
        
		ImageView g = new ImageView(this);
        g.setImageResource(R.drawable.gowalla20x20);
        icon_line.addView(g);
        
		ImageView bk = new ImageView(this);
        bk.setImageResource(R.drawable.brightkite20x20);
        icon_line.addView(bk);
        
		setContentView(sv);		
		//setContentView(R.layout.nearby_places);
		
		
//		SharedPreferences p = this.getPreferences(MODE_WORLD_WRITEABLE);
//		Log.i(TAG, "test! = " + p.getString("oauth_token", "-1")); // shared preferences not shared across activities :(
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