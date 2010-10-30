package com.davidivins.checkin4me;

public class Service 
{
	private String key;
	private int drawable;
	private int id;
	
	Service(String key, int drawable, int id)
	{
		this.key = key;
		this.drawable = drawable;
		this.id = id;
	}
	
	String getKey()
	{
		return key;
	}
	
	int getDrawable()
	{
		return drawable;
	}
	
	int getId()
	{
		return id;
	}

}
