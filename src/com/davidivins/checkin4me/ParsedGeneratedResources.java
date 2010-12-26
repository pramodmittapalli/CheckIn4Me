package com.davidivins.checkin4me;

import android.util.Log;

import com.davidivins.checkin4me.GeneratedResourcesInterface;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ParsedGeneratedResources implements GeneratedResourcesInterface
{
	private static final String TAG = "ParsedGeneratedResources";
	private HashMap<String, HashMap<String, Integer>> resources;
	
	public ParsedGeneratedResources(String class_name) 
	{
		resources = new HashMap<String, HashMap<String, Integer>>();
		
		try
		{                    
			Class<?> r_class = Class.forName(class_name);
			Class<?>[] r_classes = r_class.getClasses();
	            
			for (Class<?> current_class : r_classes)
			{
				HashMap<String, Integer> current_class_fields = new HashMap<String, Integer>();
				Field[] fields = current_class.getDeclaredFields();
				
				String[] current_class_name = current_class.getName().split("\\.");
				Log.i(TAG, "current_class = " + current_class_name[4]);
				
				// skip styleable
				if ("R$styleable".equals(current_class_name[4])) continue;
				
				for (Field current_field : fields)
				{
					current_class_fields.put(current_field.getName(), current_field.getInt(current_field));
					Log.i(TAG, "current_field = " + current_field.getName() + " = " + current_field.getInt(current_field));
				}
				
				resources.put(current_class_name[4], current_class_fields);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "Failed to load generated resources file");
			Log.e(TAG, e.getMessage());
		}
	}
	
	public int getAttr(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$attr").containsKey(name));
			value = resources.get("R$attr").get(name);
			
		return value;
	}
	
	public int getColor(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$color").containsKey(name));
			value = resources.get("R$color").get(name);
		
		return value;
	}
	
	public int getDrawable(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$drawable").containsKey(name));
			value = resources.get("R$drawable").get(name);
		
		return value;
	}
	
	public int getId(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$id").containsKey(name));
			value = resources.get("R$id").get(name);
		
		return value;
	}
	
	public int getLayout(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$layout").containsKey(name));
			value = resources.get("R$layout").get(name);
		
		return value;
	}

	public int getMenu(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$menu").containsKey(name));
			value = resources.get("R$menu").get(name);
		
		return value;
	}
	
	public int getRaw(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$raw").containsKey(name));
			value = resources.get("R$raw").get(name);
		
		return value;
	}
	
	public int getString(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$string").containsKey(name));
			value = resources.get("R$string").get(name);
		
		return value;
	}
	
	public int getXml(String name)
	{
		int value = 0x0;
		
		if (resources.get("R$xml").containsKey(name));
			value = resources.get("R$xml").get(name);
		
		return value;
	}
}
