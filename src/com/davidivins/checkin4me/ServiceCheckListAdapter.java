package com.davidivins.checkin4me;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * ServiceCheckListAdapter
 * 
 * @author david ivins
 */
class ServiceCheckListAdapter extends ArrayAdapter<Service> implements OnCheckedChangeListener
{
	//private static final String TAG = "ServiceCheckListAdapter";
	private Context context;
	private int row_resource_id;
	private ArrayList<Service> items;
	
	private HashMap<Integer, Boolean> services_checked;

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
		
		services_checked = new HashMap<Integer, Boolean>();
		for(Service service : items)
		{
			services_checked.put(service.getId(), true);
		}
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
		Service service = items.get(position);
		
		if (view == null)
		{
			LayoutInflater layout_inflater = 
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout_inflater.inflate(row_resource_id, null);
		}
		
		if (service != null) 
		{
			LinearLayout icon_and_name_layout = (LinearLayout)view.findViewById(GeneratedResources.getId("service_icon_and_name"));
			LinearLayout check_box_layout = (LinearLayout)view.findViewById(GeneratedResources.getId("service_checkbox"));
			
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
			check_box.setId(service.getId());
			
			check_box.setOnCheckedChangeListener(this);

			icon_and_name_layout.addView(icon);
			icon_and_name_layout.addView(name);
			check_box_layout.addView(check_box);
		}
			
		return view;
    }

	/**
	 * onCheckedChanged
	 * 
	 * @param CompountButton button_view
	 * @param boolean is_checked
	 */
	public void onCheckedChanged(CompoundButton button_view, boolean is_checked) 
	{
		services_checked.put(button_view.getId(), is_checked);
	}
	
	/**
	 * getServicesChecked
	 * 
	 * @return HashMap<Integer, Boolean> services_checked
	 */
	public HashMap<Integer, Boolean> getServicesChecked()
	{
		return services_checked;
	}
}
