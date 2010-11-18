package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ServiceConnection extends ListActivity implements OnItemClickListener
{
	private static final String TAG = "ServiceConnection";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// initialize services and get list
		ArrayList<Service> services_list = 
			Services.getInstance(this).getServicesAsArrayList();
		
		for (Service service : services_list)
		{
			if (service.connected(PreferenceManager.getDefaultSharedPreferences(this)))
			{
				Intent i = new Intent(this, NearbyPlaces.class);
				startActivity(i);
			}
		}

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
			R.layout.serviceconnection, Services.getInstance(this).getLogoKeys(), 
			Services.getInstance(this).getLogoIds());

        setListAdapter(service_images);

        ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(this);
		lv.setBackgroundColor(Color.WHITE); 
	}
	
	public void onItemClick(AdapterView<?> arg0, View view, int position, long row) 
	{
		Intent i = new Intent(this, Authorization.class);
		
		// save position as service id for service connection activity
		Log.i(TAG, "clicked service id = " + position);
		i.putExtra("service_id", position);
		startActivity(i);
	}
}
