package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * ServiceConnection
 * 
 * @author david
 */
public class ServiceConnection extends ListActivity implements OnItemClickListener
{
	private static final String TAG = "ServiceConnection";
	private static int latest_service_id_selected = 0;

	/**
	 * onCreate
	 * 
	 * @param Bundle savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// initialize services and get list
		ArrayList<Service> services_list = 
			Services.getInstance(this).getServicesAsArrayList();

		// define the list which holds the information of the list
		List<Map<String, Object>> resource_names =
			new ArrayList<Map<String, Object>>();

		// define the map which will hold the information for each row
		Map<String, Object> data;

		for (Service service : services_list)
		{
			data = new HashMap<String, Object>();
			data.put(service.getLogo().getKey(), service.getLogo().getDrawable()); 
			resource_names.add(data);
		}

		SimpleAdapter service_images = new SimpleAdapter(this, resource_names,
			R.layout.service_connection, Services.getInstance(this).getLogoKeys(), 
			Services.getInstance(this).getLogoIds());

        setListAdapter(service_images);
        getListView().setTextFilterEnabled(true);
		getListView().setOnItemClickListener(this);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setCacheColorHint(Color.WHITE);
	}
	
	/**
	 * onItemClick
	 * 
	 * @param AdapterView<?> arg0
	 * @param View view
	 * @param int position
	 * @param long row
	 */
	public void onItemClick(AdapterView<?> arg0, View view, int position, long row) 
	{
		// save position as service id for service connection activity
		Log.i(TAG, "clicked service id = " + position);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		latest_service_id_selected = position;
		
		if (Services.getInstance(this).getServiceById(position).getOAuthConnector() != null)
		{
			if (Services.getInstance(this).getServiceById(position).connected(settings))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(Services.getInstance(this).getServiceById(position).getName() + 
					" is already connected. Do you wish to reconnect it?")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							loadAuthorizationActivity();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
				
				AlertDialog alert = builder.create();
				alert.show();
			}
			else
			{
				loadAuthorizationActivity();
			}
		}
		else
		{
			CharSequence msg = Services.getInstance(this).getServiceById(position).getName()
				+ " doesn't work yet :(";
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			Log.e(TAG, Services.getInstance(this).getServiceById(position).getName() + " service doesn't work yet :(");
		}
	}
	
	/**
	 * loadAuthorizationActivity
	 */
	public void loadAuthorizationActivity()
	{
		Intent i = new Intent(this, Authorization.class);
		i.putExtra("service_id", latest_service_id_selected);
		startActivity(i);
	}
	
	/**
	 * onCreateOptionsMenu
	 * 
	 * @param Menu menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		boolean result = false;
		
		if (Services.getInstance(this).atLeastOneConnected(PreferenceManager.getDefaultSharedPreferences(this)))
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.service_connection, menu);
			result = true;
		}
		
		return result;
	}
	
	/**
	 * onOptionsItemSelected
	 * 
	 * @param MenuItem item
	 * @return boolean
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		boolean result = false;
		
		// Handle item selection
		switch (item.getItemId()) 
		{
			case R.id.nearby_places:
				Intent i = new Intent(this, NearbyPlaces.class);
				startActivity(i);
				result = true;
			default:
				result = false;
		}
		
		return result;
	}

	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
