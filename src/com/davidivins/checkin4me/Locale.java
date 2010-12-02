package com.davidivins.checkin4me;

import java.util.HashMap;
import java.util.Set;

import com.google.android.maps.GeoPoint;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Location
 * 
 * @author david ivins
 */
public class Locale 
{
	private static final String TAG = "Locale";
	private String name;
	private String description;

	private String longitude;
	private String latitude;
	
	private String address;
	private String city;
	private String state;
	private String zip;
	
	HashMap<Integer, String> service_location_ids;
	
	/**
	 * Location
	 */
	public Locale()
	{
		name = "";
		description = "";
		
		longitude = "0.0";
		latitude = "0.0";
		
		address = "";
		city = "";
		state = "";
		zip = "";
		
		service_location_ids = new HashMap<Integer, String>();
	}
	
	/**
	 * Location
	 * 
	 * @param name
	 * @param description
	 * @param longitude
	 * @param latitude
	 */
	public Locale(String name, String description, String longitude, String latitude,
			String address, String city, String state, String zip)
	{
		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		
		service_location_ids = new HashMap<Integer, String>();
	}
	
	/**
	 * getName 
	 * 
	 * @return String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * getDescription
	 * 
	 * @return String
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * setDescription
	 * 
	 * @param String description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * getLongitude
	 * 
	 * @return String
	 */
	public String getLongitude()
	{
		return longitude;
	}
	
	/**
	 * getLatitude
	 * 
	 * @return String
	 */
	public String getLatitude()
	{
		return latitude;
	}
	
	/**
	 * getAddress
	 * 
	 * @return String
	 */
	public String getAddress()
	{
		String out_address = "";
		
		if (!address.equals(""))
			out_address += address;
		
		if (!city.equals(""))
			out_address += ". " + city;
		
		if (!state.equals(""))
			out_address += ", " + state;

		if (!zip.equals(""))
			out_address += " " + zip;

		return out_address;
	}
	
	/**
	 * getCoordinatesAsGeoPoint
	 * 
	 * @return GeoPoint
	 */
	public GeoPoint getCoordinatesAsGeoPoint()
	{
		Double longitude = new Double(this.longitude);
		Double latitude = new Double(this.latitude);
		return new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
	}
	
	/**
	 * mapServiceIdToLocationId
	 * 
	 * @param Integer service_id
	 * @param String location_id
	 */
	public void mapServiceIdToLocationId(int service_id, String location_id)
	{
		service_location_ids.put(service_id, location_id);
	}
	
	/**
	 * getServiceIdToLocationIdMap
	 * 
	 * @return HashMap<Integer, String>
	 */
	public HashMap<Integer, String> getServiceIdToLocationIdMap()
	{
		return service_location_ids;
	}
	
	/**
	 * store
	 * 
	 * @param SharedPreferences settings
	 */
	public void store(SharedPreferences settings)
	{
		Editor settings_editor = settings.edit();
		int last_saved_xref_count = settings.getInt("last_saved_xref_count", 0);
		
		settings_editor.putString("current_location_name", name);
		settings_editor.putString("current_location_description", description);
		settings_editor.putString("current_location_longitude", longitude);
		settings_editor.putString("current_location_latitude", latitude);
		
		settings_editor.putString("current_location_address", address);
		settings_editor.putString("current_location_city", city);
		settings_editor.putString("current_location_state", state);
		settings_editor.putString("current_location_zip", zip);
		
		Set<Integer> keys = service_location_ids.keySet();
		int count = 0;
		
		for (int i = 0; i < last_saved_xref_count; i++)
		{
			settings_editor.remove("current_location_xref_key_" + i);
			settings_editor.remove("current_location_xref_value_" + i);
		}
		
		for (Integer key : keys)
		{
			String value = service_location_ids.get(key);
			settings_editor.putString("current_location_xref_key_" + count, key.toString());
			settings_editor.putString("current_location_xref_value_" + count, value);
			count++;
		}
		
		Log.i(TAG, "Saved " + count + " mappings");
		settings_editor.commit();
	}
	
	/**
	 * load
	 * 
	 * @param SharedPreferences settings
	 */
	public void load(SharedPreferences settings)
	{
		Editor settings_editor = settings.edit();
		
		name = settings.getString("current_location_name", "");
		description = settings.getString("current_location_description", "");
		longitude = settings.getString("current_location_longitude", "");
		latitude = settings.getString("current_location_latitude", "");
		
		address = settings.getString("current_location_address", "");
		city = settings.getString("current_location_city", "");
		state = settings.getString("current_location_state", "");
		zip = settings.getString("current_location_zip", "");
		
		for (int i = 0; i != -1; i++) // <---  i know... :(
		{
			String key_string = settings.getString("current_location_xref_key_" + i, "");
			String value = settings.getString("current_location_xref_value_" + i, "");
			
			if (!key_string.equals("") && !value.equals(""))
			{
				Integer key = new Integer(key_string);
				service_location_ids.put(key, value);
			}
			else
			{
				Log.i(TAG, "Loaded " + i + " mappings");
				settings_editor.putInt("last_saved_xref_count", i);
				settings_editor.commit();
				break;
			}
		}
	}
}