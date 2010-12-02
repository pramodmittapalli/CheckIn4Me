package com.davidivins.checkin4me;

import java.util.Comparator;

/**
 * LocaleNameComparator
 * 
 * @author david ivins
 */
class LocaleNameComparator implements Comparator<Locale> 
{
	/**
	 * compare
	 * 
	 * @param Locale location_1
	 * @param Locale location_2
	 * @return int
	 */
	public int compare(Locale location_1, Locale location_2)
	{
		return location_1.getName().compareToIgnoreCase(location_2.getName());
	}
}