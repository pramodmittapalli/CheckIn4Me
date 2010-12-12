package com.davidivins.checkin4me;

public interface NetworkTimeoutListener 
{
	abstract public Runnable getNetworkTimeoutCallback();
}
