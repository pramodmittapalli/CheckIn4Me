package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * LocationDetails
 * 
 * @author david
 */
public class LocationDetails extends MapActivity implements OnClickListener, DialogInterface.OnClickListener
{
	private static final String TAG = "LocationDetails";
	Locale current_location = new Locale();
	
	/**
	 * onCreate
	 * 
	 * @param saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		setContentView(R.layout.location_details);
		
		// load current location from preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		current_location.load(settings);
		
		// load current longitude and latitude from preferences
		Double current_longitude = new Double(settings.getString("current_longitude", "0"));
		Double current_latitude = new Double(settings.getString("current_latitude", "0"));
		
		// 
		// location name and address
		//
		TextView location_name = (TextView)findViewById(R.id.location_name);
		location_name.setText(current_location.getName());
		
		TextView location_description = (TextView)findViewById(R.id.location_description);
		location_description.setText(current_location.getAddress());
		
		//
		// map stuff
		//
		MapView location_map = (MapView) findViewById(R.id.location_map);
		List<Overlay> map_overlays = location_map.getOverlays();
		Drawable drawable = this.getResources().getDrawable(android.R.drawable.star_on);
		
		LocationOverlay location_overlay1 = new LocationOverlay(drawable, this);
		LocationOverlay location_overlay2 = new LocationOverlay(drawable, this);
		
		GeoPoint location_point = current_location.getCoordinatesAsGeoPoint();
		GeoPoint user_point = new GeoPoint((int)(current_latitude.doubleValue() * 1E6), (int)(current_longitude.doubleValue() * 1E6));
		
		OverlayItem overlayitem1 = new OverlayItem(location_point, current_location.getName(), current_location.getAddress());
		OverlayItem overlayitem2 = new OverlayItem(user_point, "You Are Here.", "Lat: " + current_latitude + "\nLong: " + current_longitude);
		
		location_overlay1.addOverlay(overlayitem1);
		location_overlay2.addOverlay(overlayitem2);
		
		map_overlays.add(location_overlay1);
		map_overlays.add(location_overlay2);
		
		MapController map_controller = location_map.getController();
		map_controller.setCenter(location_point);
		map_controller.setZoom(15);
		
		//
		// list stuff
		//
		ListView list_view = (ListView)findViewById(R.id.location_service_list);
		list_view.setTextFilterEnabled(true);
		list_view.setBackgroundColor(Color.WHITE);
		list_view.setCacheColorHint(Color.WHITE);

		// add services to list
		ArrayList<Service> services = Services.getInstance(this).getConnectedServicesAsArrayList(settings);
		ServiceCheckListAdapter adapter = new ServiceCheckListAdapter(this, R.layout.location_details_row, services);
		list_view.setAdapter(adapter);
		
		// 
		// button stuff
		//
		Button button = (Button)findViewById(R.id.check_in_button);
		button.setOnClickListener(this);
	}

	/**
	 * onClick
	 * 
	 * @param View view
	 */
	public void onClick(View view) 
	{
		ListView list_view = (ListView)findViewById(R.id.location_service_list);
		ServiceCheckListAdapter adapter = (ServiceCheckListAdapter)list_view.getAdapter();
		
		HashMap<Integer, Boolean> services_checked = adapter.getServicesChecked();
		Set<Integer> keys = services_checked.keySet();
		
		for(int key : keys)
		{
			Log.i(TAG, "service connected id = " + key + " and checked state = " + services_checked.get(key));
		}
		
		
		HashMap<Integer, Boolean> checkin_statuses = new HashMap<Integer, Boolean>();
		checkin_statuses.put(0, true);
		checkin_statuses.put(1, true);
		displayCheckInStatus(checkin_statuses);
	}
	
	/**
	 * displayCheckInStatus
	 * 
	 * @param HashMap<Integer, Boolean> checkin_statuses
	 */
	public void displayCheckInStatus(HashMap<Integer, Boolean> checkin_statuses)
	{
		// retrieve layout inflater
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.checkin_dialog, (ViewGroup)findViewById(R.id.checkin_root));

		// set text for layout
		TextView text = (TextView) layout.findViewById(R.id.checkin_text);
		text.setText("We have you at " + current_location.getName() + " on:");
		
		// add service icons to layout
		LinearLayout images = (LinearLayout)layout.findViewById(R.id.service_checkin_icons);
		Set<Integer> keys = checkin_statuses.keySet();
		for (int key : keys)
		{
			// if the check-in for this particular service was successful
			if (checkin_statuses.get(key))
			{
				ImageView image = new ImageView(this);
				image.setImageResource(Services.getInstance(this).getServiceById(key).getIconDrawable());
				image.setPadding(0, 5, 5, 0);
				images.addView(image);
			}
		}

		// create alert dialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);		
		builder.setPositiveButton("OK", this);
		
		// create alert dialog
		AlertDialog alertDialog = builder.create();
		alertDialog.setIcon(R.drawable.check);
		alertDialog.setTitle("Check-in Successful!");
		alertDialog.show(); 
	}

	/**
	 * onClick
	 * 
	 * @param DialogInterface dialog
	 * @param int which
	 */
	public void onClick(DialogInterface dialog, int which) 
	{
		// return to nearby places
		Intent i = new Intent(this, NearbyPlaces.class);
		startActivity(i);
	}
		
	/**
	 * isRouteDisplayed
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean isRouteDisplayed() 
	{
		return false;
	}
}
