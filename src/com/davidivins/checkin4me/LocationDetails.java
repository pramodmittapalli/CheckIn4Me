package com.davidivins.checkin4me;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

/**
 * LocationDetails
 * 
 * @author david ivins
 */
public class LocationDetails extends MapActivity 
	implements OnClickListener, DialogInterface.OnClickListener, CleanableProgressDialogListener
{
	private static final String TAG = "LocationDetails";
	private static Properties config = null;
	
	Locale current_location = new Locale();
	HashMap<Integer, Boolean> checkin_statuses = new HashMap<Integer, Boolean>();
	private static ProgressDialog checking_in_dialog = null;
	
	private final Handler handler = new Handler(); 
	private Thread checkin_thread;
	
	// acts as callback from thread
	private Runnable process_check_in = new Runnable() 
	{
		public void run() 
		{
			checkInComplete();
		}
	};
	
	/**
	 * onCreate
	 * 
	 * @param saved_instance_state
	 */
	@Override
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		
		// set the layout for the current activity
		setContentView(GeneratedResources.getLayout("location_details"));
		
		// display ad if this is not the pro version
		Ad ad = new Ad(this);
		ad.refreshAd();
		
		// get map config file if necessary
		if (config == null)
		{
			config = new Properties();
		
			try 
			{
				InputStream config_file = getResources().openRawResource(GeneratedResources.getRaw("google_maps"));
				config.load(config_file);
			} 
			catch (Exception e) 
			{
				Log.e(TAG, "Failed to open config file");
			}
		}
		
		// load current location from preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		current_location.load(settings);
		
		// load current longitude and latitude from preferences
		Double current_longitude = new Double(settings.getString("current_longitude", "0"));
		Double current_latitude = new Double(settings.getString("current_latitude", "0"));
		
		// 
		// location name and address
		//
		TextView location_name = (TextView)findViewById(GeneratedResources.getId("location_name"));
		location_name.setText(current_location.getName());
		
		TextView location_description = (TextView)findViewById(GeneratedResources.getId("location_description"));
		location_description.setText(current_location.getAddress());
		
		//
		// map stuff
		//
		MapView location_map = new MapView(this, config.getProperty(GeneratedResources.getVersion() + "_api_key"));
		location_map.setClickable(true);
		
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
		
		LinearLayout map_layout = (LinearLayout)findViewById(GeneratedResources.getId("location_map"));
		map_layout.addView(location_map);
		
		//
		// list stuff
		//
		ListView list_view = (ListView)findViewById(GeneratedResources.getId("location_service_list"));
		list_view.setTextFilterEnabled(true);
		list_view.setBackgroundColor(Color.WHITE);
		list_view.setCacheColorHint(Color.WHITE);

		// add services to list
		HashMap<Integer, String> service_id_location_id_xref = current_location.getServiceIdToLocationIdMap();
		ArrayList<Service> services = new ArrayList<Service>();
		Set<Integer> service_ids = service_id_location_id_xref.keySet();
		
		for(int service_id : service_ids)
		{
			services.add(Services.getInstance(this).getServiceById(service_id));
		}
		
		ServiceCheckListAdapter adapter = new ServiceCheckListAdapter(this, GeneratedResources.getLayout("location_details_row"), services);
		list_view.setAdapter(adapter);
		
		// 
		// button stuff
		//
		Button button = (Button)findViewById(GeneratedResources.getId("check_in_button"));
		button.setOnClickListener(this);
	}
	
	/**
	 * onStop
	 */
	@Override
	public void onStop()
	{
		super.onStop();
		cleanUp();
	}
	
	/**
	 * onClick
	 * 
	 * @param View view
	 */
	public void onClick(View view) 
	{
		// cancel acquiring location dialog
		if (null != checking_in_dialog && checking_in_dialog.isShowing())
			checking_in_dialog.cancel();
		
		// get list and adapter
		ListView list_view = (ListView)findViewById(GeneratedResources.getId("location_service_list"));
		ServiceCheckListAdapter adapter = (ServiceCheckListAdapter)list_view.getAdapter();
		
		// retrieve services that were checked
		HashMap<Integer, Boolean> services_checked = adapter.getServicesChecked();
		ArrayList<Integer> service_ids = new ArrayList<Integer>();
		
		// pull out services checked
		Set<Integer> keys = services_checked.keySet();
		for(int key : keys)
		{
			Log.i(TAG, "service connected id = " + key + " and checked state = " + services_checked.get(key));
			if (services_checked.get(key))
				service_ids.add(key);
		}
		
		if (service_ids.isEmpty())
		{
			Toast.makeText(this, "No services checked", Toast.LENGTH_SHORT).show();
		}
		else
		{		
			checking_in_dialog = new CleanableProgressDialog(this, this, "", "Checking in...", true);
			checking_in_dialog.show();
			
			// create and start check-in thread
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			checkin_thread = new Thread(new CheckInThread(this, service_ids, current_location, settings), "CheckinThread");
			checkin_thread.start();
		}
	}
	
	/**
	 * onDialogInterruptedByBackButton
	 */
	public void onDialogInterruptedByBackButton() 
	{
		cleanUp();
	}

	/**
	 * onDialogInterruptedBySearchButton
	 */
	public void onDialogInterruptedBySearchButton() 
	{
		cleanUp();
	}
	
	/**
	 * cleanUp
	 */
	private void cleanUp()
	{
		// cancel thread
		if (null != checkin_thread)
		{
			handler.removeCallbacks(process_check_in);
			process_check_in = null;
		}	
		
		// cancel any dialogs showing
		if (checking_in_dialog != null && checking_in_dialog.isShowing())
			checking_in_dialog.cancel();
	}
	
	/**
	 * displayCheckInStatus
	 * 
	 * @param HashMap<Integer, Boolean> checkin_statuses
	 */
	public void displayCheckInStatus(HashMap<Integer, Boolean> checkin_statuses)
	{
		boolean some_succeeded = false;
		boolean some_failed = false;
		
		// retrieve layout inflater
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(GeneratedResources.getLayout("checkin_dialog"), 
				(ViewGroup)findViewById(GeneratedResources.getId("checkin_root")));
		LinearLayout checkin_root_dialog = (LinearLayout)layout.findViewById(GeneratedResources.getId("checkin_root"));

		// add service icons to layout
		LinearLayout successful_images = new LinearLayout(this);
		LinearLayout failure_images = new LinearLayout(this);
		
		// center icons
		successful_images.setGravity(0x11);
		failure_images.setGravity(0x11);
		
		// get icons for successful and failed check-ins
		Set<Integer> keys = checkin_statuses.keySet();
		for (int key : keys)
		{
			ImageView image = new ImageView(this);
			image.setImageResource(Services.getInstance(this).getServiceById(key).getIconDrawable());
			image.setPadding(0, 5, 5, 0);
			
			// if the check-in for this particular service was successful
			if (checkin_statuses.get(key))
			{
				successful_images.addView(image);
				some_succeeded= true;
			}
			else
			{
				failure_images.addView(image);
				some_failed = true;
			}
		}
		
		// successful checkins
		if (some_succeeded)
		{
			TextView successful_text = new TextView(this);
			successful_text.setText("We have you at " + current_location.getName() + " on:");
			successful_text.setGravity(0x11); // center text
			checkin_root_dialog.addView(successful_text);
			checkin_root_dialog.addView(successful_images);
		}
		
		// failed check-ins
		if (some_failed)
		{
			TextView failure_text = new TextView(this);
			failure_text.setText("Check-in failed at " + current_location.getName() + " on:");
			failure_text.setGravity(0x11); // center text

			if (some_succeeded) // spread them out
				failure_text.setPadding(0, 10, 0, 0);
			
			checkin_root_dialog.addView(failure_text);
			checkin_root_dialog.addView(failure_images);
		}

		// create alert dialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);		
		builder.setPositiveButton("OK", this);
		
		// create alert dialog
		AlertDialog alertDialog = builder.create();
		
		// set title/icon based on success or failure
		if (some_succeeded && some_failed) // some check-ins failed and some check-ins succeeded
		{
			alertDialog.setIcon(GeneratedResources.getDrawable("warning"));
			alertDialog.setTitle("Check-in Results Mixed!");
		}
		else if (some_failed) // all check-ins failed
		{
			alertDialog.setIcon(GeneratedResources.getDrawable("x"));
			alertDialog.setTitle("Check-in Failed!");
		}
		else // all check-ins succeeded
		{
			alertDialog.setIcon(GeneratedResources.getDrawable("check"));
			alertDialog.setTitle("Check-in Successful!");
		}
		
		// show check-in dialog box
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
	
	/**
	 * checkInComplete
	 */
	protected void checkInComplete()
	{
		Log.i(TAG, "received check-in completed.");
    	
		// join thread even though we know it already completed
		try 
		{
			if (checkin_thread != null)
				checkin_thread.join();
		} 
		catch (InterruptedException e) 
		{
			Log.i(TAG, "Thread interrupted already");
		}
		
		// cancel acquiring location dialog
		if (null != checking_in_dialog && checking_in_dialog.isShowing())
			checking_in_dialog.cancel();
		
		// display check in dialog
		displayCheckInStatus(checkin_statuses);
	}
	
	/**
	 * CheckInThread
	 * 
	 * @author david
	 */
	class CheckInThread implements Runnable
	{
		Activity activity;
		ArrayList<Integer> service_ids;
		Locale location;
		SharedPreferences settings;
		
		/**
		 * CheckInThread
		 * 
		 * @param activity
		 * @param service_ids
		 * @param settings
		 */
		CheckInThread(Activity activity, ArrayList<Integer> service_ids, Locale location, SharedPreferences settings)
		{
			this.activity = activity;
			this.service_ids = service_ids;
			this.location = location;
			this.settings = settings;
		}
		
		/**
		 * run
		 */
		public void run() 
		{
			checkin_statuses.clear();
			
			// mock return statuses
			//checkin_statuses.put(0, false);
			//checkin_statuses.put(1, true);
			
			checkin_statuses = Services.getInstance(activity).checkIn(service_ids, location, settings);
			
			handler.post(process_check_in);
		}
	}
}
