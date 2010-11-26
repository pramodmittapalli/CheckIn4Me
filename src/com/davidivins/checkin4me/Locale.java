package com.davidivins.checkin4me;

import java.util.HashMap;
import java.util.Set;

import com.google.android.maps.GeoPoint;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Location
 * 
 * @author david
 */
public class Locale 
{
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
	 * @param Editor settings_editor
	 */
	public void store(Editor settings_editor)
	{		
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
		
		for (Integer key : keys)
		{
			String value = service_location_ids.get(key);
			settings_editor.putString("current_location_xref_key_" + count, key.toString());
			settings_editor.putString("current_location_xref_value_" + count, value);
			count++;
		}
		
		settings_editor.commit();
	}
	
	/**
	 * load
	 * 
	 * @param SharedPreferences settings
	 */
	public void load(SharedPreferences settings)
	{
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
				break;
		}
	}
}

//
// Gowalla Location
//
//"checkins_url": "/checkins?spot_id=10526",
//"trending_level": 1,
//"url": "/spots/10526",
//"lat": 30.2836404612,
//"_image_url_50": "http://static.gowalla.com/spots/10526-c5170efc635d92275be62e2354f7123c-100.png?1",
//"spot_categories": [
//  {
//    "url": "/categories/79",
//    "name": "Stadium"
//  }
//],
//"description": "Home of the UT Longhorn football team and a capacity of 94,113; it's the largest non-professional football venue in the state of Texas, in the Big 12 Conference and is the 5th largest on-campus stadium in the NCAA.",
//"checkins_count": 1469,
//"radius_meters": 300,
//"photos_count": 76,
//"image_url": "http://static.gowalla.com/spots/10526-c5170efc635d92275be62e2354f7123c-100.png?1",
//"lng": -97.7325296402,
//"address": {
//  "locality": "Austin",
//  "region": "TX"
//},
//"strict_radius": false,
//"users_count": 780,
//"items_count": 10,
//"name": "Darrell K. Royal Stadium",
//"items_url": "/spots/10526/items",
//"activity_url": "/spots/10526/events",
//"highlights_url": "/spots/10526/highlights",
//"_image_url_200": "http://static.gowalla.com/spots/10526-053f156359609e0deb077157007a4352-200.png?1"
//},

//
// Foursquare Location
//
//groups:[
//        {
//            type:'Nearby favorites',
//            venue:{
//                id:257,
//                name:'Bowery Ballroom',
//                primarycategory:{
//                    id:{
//                        value:79167,
//                        id:{
//                            fullpathname:'Nightlife:Music Venue:Rock Club',
//                            nodename:'Rock Club',
//                            iconurl:'http://foursquare.com/img/categories/nightlife/default.png',
//                            distance:10,
//                            address:'6 Delancey St',
//                            crossstreet:'at Bowery',
//                            city:'New York',
//                            state:'NY',
//                            zip:10002,
//                            geolat:40.7204,
//                            geolong:-73.9933,
//                            phone:2120000000,
//                            twitter:'BoweryBallroom',
//                            stats:{
//                                herenow:36
//                            },
//                            venue:{
//                                id:5055,
//                                name:'Hiro Ballroom'