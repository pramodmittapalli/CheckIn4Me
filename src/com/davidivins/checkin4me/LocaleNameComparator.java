package com.davidivins.checkin4me;

import java.util.Comparator;

class LocaleNameComparator implements Comparator<Locale> 
{
	public int compare(Locale location_1, Locale location_2)
	{
		return location_1.getName().compareToIgnoreCase(location_2.getName());
	}
}