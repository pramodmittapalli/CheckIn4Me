package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * NearbyPlaces
 * 
 * @author david
 */
public class NearbyPlaces extends ListActivity implements LocationListener
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

//		ScrollView sv = new ScrollView(this);
//		LinearLayout lv = new LinearLayout(this);
//		lv.setOrientation(LinearLayout.VERTICAL);
//		
//		for (int i = 0; i < 20; i++)
//		{
//			lv.addView(getRow());
//		}
//		sv.addView(lv);
//		
//		setContentView(sv);

		class LinearLayoutAdapter extends ArrayAdapter<LinearLayout>
		{
			private ArrayList<LinearLayout> items;

			public LinearLayoutAdapter(Context context, int textViewResourceId, ArrayList<LinearLayout> items) 
			{
				super(context, textViewResourceId, items);
				this.items = items;
	        }

			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				View v = convertView;
				if (v == null) 
				{
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.nearby_place_row, null);
				}
				
				LinearLayout row = items.get(position);
				if (row != null) 
				{
					LinearLayout lv = (LinearLayout)v.findViewById(R.id.nearby_place);
					lv.setOrientation(LinearLayout.VERTICAL);
					
					LinearLayout text_line = new LinearLayout(parent.getContext());
					LinearLayout icon_line = new LinearLayout(parent.getContext());
					text_line.setPadding(5, 5, 5, 5);
					icon_line.setPadding(5, 5, 5, 5);

					
					TextView tv = new TextView(parent.getContext());
					tv.setText("Some Place!!");
					tv.setTextColor(Color.BLACK);
					text_line.addView(tv);
					
					ImageView fs = new ImageView(parent.getContext());
			        fs.setImageResource(R.drawable.foursquare20x20);      
					fs.setPadding(0, 0, 5, 0);
					icon_line.addView(fs);
			        
					ImageView g = new ImageView(parent.getContext());
			        g.setImageResource(R.drawable.gowalla20x20);
					g.setPadding(0, 0, 5, 0);
					icon_line.addView(g);

					ImageView bk = new ImageView(parent.getContext());
			        bk.setImageResource(R.drawable.brightkite20x20);
					bk.setPadding(0, 0, 5, 0);
					icon_line.addView(bk);

					lv.addView(text_line);
					lv.addView(icon_line);
				}
				
				return v;
	        }
		}
		
		setContentView(R.layout.nearby_places);
		ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();
		
		for (int i = 0; i < 20; i++)
		{
		rows.add(getRow());
		}
		LinearLayoutAdapter adapter = new LinearLayoutAdapter(this, R.layout.nearby_place_row, rows);
		setListAdapter(adapter);
		
		getListView().setTextFilterEnabled(true);
		getListView().setBackgroundColor(Color.WHITE); 
		
	}
	
	LinearLayout getRow()
	{
		LinearLayout row = new LinearLayout(this);
		row.setOrientation(LinearLayout.VERTICAL);

		LinearLayout text_line = new LinearLayout(this);
		LinearLayout icon_line = new LinearLayout(this);
		
		text_line.setOrientation(LinearLayout.HORIZONTAL);
		icon_line.setOrientation(LinearLayout.HORIZONTAL);
		
		row.addView(text_line);
		row.addView(icon_line);
		
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
        
        return row;
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