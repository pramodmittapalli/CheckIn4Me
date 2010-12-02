package com.davidivins.checkin4me;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * LocationOverlay
 * 
 * @author david ivins
 */
public class LocationOverlay extends ItemizedOverlay<OverlayItem>
{
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context context;
	
	/**
	 * LocationOverlay
	 * 
	 * @param defaultMarker
	 * @param context
	 */
	public LocationOverlay(Drawable defaultMarker, Context context) 
	{
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}
	
	/**
	 * addOverlay
	 * 
	 * @param overlay
	 */
	public void addOverlay(OverlayItem overlay) 
	{
		overlays.add(overlay);
		populate();
	}
	
	/**
	 * onTap
	 * 
	 * @param int index
	 * @return boolean
	 */
	@Override
	protected boolean onTap(int index) 
	{
		OverlayItem item = overlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
	/**
	 * createItem
	 * 
	 * @param int i
	 * @return OverlayItem
	 */
	@Override
	protected OverlayItem createItem(int i) 
	{
		return overlays.get(i);
	}

	/**
	 * size
	 * 
	 * @return int
	 */
	@Override
	public int size() 
	{
		return overlays.size();
	}
}
