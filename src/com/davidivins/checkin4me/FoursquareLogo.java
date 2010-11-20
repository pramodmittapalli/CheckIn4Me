package com.davidivins.checkin4me;

/**
 * FoursquareLogo
 * 
 * @author david
 */
public class FoursquareLogo implements DrawableListItem
{
	String key;
	int drawable;
	int id;
	
	/**
	 * FoursquareLogo
	 * 
	 * @param key
	 * @param drawable
	 * @param id
	 */
	public FoursquareLogo(String key, int drawable, int id)
	{
		this.key = key;
		this.drawable = drawable;
		this.id = id;
	}
	
	/**
	 * getKey
	 * 
	 * @return String
	 */
	public String getKey()
	{
		return key;
	}
	
	/**
	 * getDrawable
	 * 
	 * @return int
	 */
	public int getDrawable() 
	{
		return drawable;
	}
	
	/**
	 * getId
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}
}
