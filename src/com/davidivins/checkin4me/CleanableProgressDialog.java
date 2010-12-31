package com.davidivins.checkin4me;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * CleanableProgressDialog
 * 
 * @author david
 */
public class CleanableProgressDialog extends ProgressDialog
{
	CleanableProgressDialogListener listener;
	
	/**
	 * CleanableProgressDialog
	 * @param listener
	 * @param activity
	 */
	public CleanableProgressDialog(CleanableProgressDialogListener listener, Activity activity, String title, String msg, boolean indeterminate)
	{
		super(activity);
		this.listener = listener;
		setTitle(title);
		setMessage(msg);
		setIndeterminate(indeterminate);
	}
	
	/**
	 * onSearchRequested
	 * 
	 * @return boolean
	 */
	@Override
	public boolean onSearchRequested()
	{
		boolean result = super.onSearchRequested();
		listener.onDialogInterruptedBySearchButton();
		return result;
	}
	
	/**
	 * onBackPressed
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		listener.onDialogInterruptedByBackButton();
	}
}
