package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ServiceConnectionAdapter
 * 
 * @author david ivins
 */
class ServiceConnectionAdapter extends ArrayAdapter<Integer>
{
	//private static final String TAG = "LocaleAdapter";
	private Context context;
	private int row_resource_id;
	private ArrayList<Integer> logos;

	/**
	 * ServiceConnectionAdapter
	 * 
	 * @param activity
	 * @param context
	 * @param row_resource_id
	 * @param items
	 */
	public ServiceConnectionAdapter(Context context, int row_resource_id, ArrayList<Integer> logos) 
	{
		super(context, row_resource_id, logos);
		this.context = context;
		this.row_resource_id = row_resource_id;
		this.logos = logos;
    }

	/**
	 * getView
	 * 
	 * @param int position
	 * @param View convert_view
	 * @param ViewGroup parent
	 * @return View
	 */
	@Override
	public View getView(int position, View convert_view, ViewGroup parent) 
	{
		View view = convert_view;
		
		if (view == null)
		{
			LayoutInflater layout_inflater = 
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout_inflater.inflate(row_resource_id, null);
		}
		
		Integer logo = logos.get(position);
		
		// items set blocks list from getting messed up on scrolling
		if (logo != null) 
		{
			LinearLayout row = (LinearLayout)view.findViewById(GeneratedResources.getId("service_connection_logo"));
			row.removeAllViews(); // <-- this thing is pretty effing important...wasted hours on this...
			
			ImageView icon = new ImageView(parent.getContext());
			icon.setImageResource(logo.intValue());
			icon.setPadding(10, 10, 10, 10);
			row.addView(icon);
		}
		
		return view;
    }
}