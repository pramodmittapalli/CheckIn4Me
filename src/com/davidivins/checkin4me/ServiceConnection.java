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

public class ServiceConnection extends ListActivity implements OnItemClickListener
{
	private static final String TAG = "ServiceConnection";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// define the list which holds the information of the list
		List<Map<String, Object>> resourceNames =
			new ArrayList<Map<String, Object>>();

		// define the map which will hold the information for each row
		Map<String, Object> data;
		
		Services services = new Services();
		ArrayList<Service> services_list = services.getServicesAsList();
		
		for (Service service : services_list)
		{
			data = new HashMap<String, Object>();
			data.put(service.getKey(), service.getDrawable()); 
			resourceNames.add(data);
		}
        
		SimpleAdapter service_images = new SimpleAdapter(this, resourceNames,
			R.layout.serviceconnection, services.getKeys(), services.getIds());
		
        setListAdapter(service_images);
        
        ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(this);
		lv.setBackgroundColor(Color.WHITE); 
	}
	
	public void onItemClick(AdapterView<?> arg0, View view, int position, long row) 
	{
		Intent i = new Intent(this, Authorization.class);
		
		// log position and row info
		Log.i(TAG, "position clicked = " + position);
		Log.i(TAG, "row clicked = " + row);
		
		// save position as service id for service connection activity
		i.putExtra("service_id", position);
		startActivity(i);
	}
}
