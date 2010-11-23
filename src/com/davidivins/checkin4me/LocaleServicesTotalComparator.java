package com.davidivins.checkin4me;

import java.util.Comparator;

public class LocaleServicesTotalComparator implements Comparator<Locale>
{
	public int compare(Locale location_1, Locale location_2)
	{
		int location_1_size = location_1.getServiceIdToLocationIdMap().size();
		int location_2_size = location_2.getServiceIdToLocationIdMap().size();
		return location_2_size - location_1_size;
	} 
}