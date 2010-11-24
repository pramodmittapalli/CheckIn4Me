package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ServiceCheckListAdapter
 * 
 * @author david
 */
class ServiceCheckListAdapter extends ArrayAdapter<Service>
{
	//private static final String TAG = "ServiceCheckListAdapter";
	private Context context;
	private int row_resource_id;
	private ArrayList<Service> items;

	/**
	 * ServiceCheckListAdapter
	 * 
	 * @param activity
	 * @param context
	 * @param row_resource_id
	 * @param items
	 */
	public ServiceCheckListAdapter(Context context, int row_resource_id, ArrayList<Service> items) 
	{
		super(context, row_resource_id, items);
		this.context = context;
		this.row_resource_id = row_resource_id;
		this.items = items;
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
		
		Service service = items.get(position);
		
		if (service != null) 
		{
			LinearLayout icon_and_name_layout = (LinearLayout)view.findViewById(R.id.service_icon_and_name);
			LinearLayout check_box_layout = (LinearLayout)view.findViewById(R.id.service_checkbox);
			
			icon_and_name_layout.removeAllViews();
			check_box_layout.removeAllViews();
			
			ImageView icon = new ImageView(parent.getContext());
			icon.setImageResource(service.getIconDrawable());
			icon.setPadding(0, 0, 5, 0);
			
			TextView name = new TextView(parent.getContext());
			name.setText(service.getName());
			name.setTextColor(Color.BLACK);

			CheckBox check_box = new CheckBox(parent.getContext());
			check_box.setChecked(true);			

			icon_and_name_layout.addView(icon);
			icon_and_name_layout.addView(name);
			check_box_layout.addView(check_box);
		}
		
		return view;
    }
}
