package com.davidivins.checkin4me;

public class FoursquareLogo implements DrawableListItem
{
	String key;
	int drawable;
	int id;
	
	public FoursquareLogo(String key, int drawable, int id)
	{
		this.key = key;
		this.drawable = drawable;
		this.id = id;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public int getDrawable() 
	{
		return drawable;
	}
	
	public int getId()
	{
		return id;
	}
}
