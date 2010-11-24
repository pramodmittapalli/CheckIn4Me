package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LocationDetails extends MapActivity implements OnItemClickListener
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_details);
		
		// 
		// location name
		//
		TextView location_name = (TextView)findViewById(R.id.location_name);
		location_name.setText("Drexel University");
		TextView location_description = (TextView)findViewById(R.id.location_description);
		location_description.setText("The bestest college everz!");
		
		//
		// map stuff
		//
		MapView location_map = (MapView) findViewById(R.id.location_map);
		List<Overlay> map_overlays = location_map.getOverlays();
		Drawable drawable = this.getResources().getDrawable(android.R.drawable.star_on);
		LocationOverlay location_overlay = new LocationOverlay(drawable, this);
		//GeoPoint point = new GeoPoint(19240000,-99120000);
		GeoPoint point = new GeoPoint((int)(39.957113*1E6), (int)(-75.190292*1E6));
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
		location_overlay.addOverlay(overlayitem);
		map_overlays.add(location_overlay);
		MapController map_controller = location_map.getController();
		map_controller.setCenter(point);
		map_controller.setZoom(12);
		
		//
		// list stuff
		//
		ListView list_view = (ListView)findViewById(R.id.location_service_list);
		list_view.setTextFilterEnabled(true);
		list_view.setOnItemClickListener(this);
		list_view.setBackgroundColor(Color.WHITE);
		list_view.setCacheColorHint(Color.WHITE);
		ArrayList<Service> services = Services.getInstance(this).getServicesAsArrayList();
		ServiceCheckListAdapter adapter = new ServiceCheckListAdapter(this, R.layout.location_details_row, services);
		list_view.setAdapter(adapter);
	}
	
	@Override
	protected boolean isRouteDisplayed() 
	{
		return false;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
	}
}
