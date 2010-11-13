package com.davidivins.checkin4me;

public class Response 
{
	protected boolean success_status;
	protected String response_string;
	
	public Response()
	{
		this.success_status = false;
		this.response_string = "";
	}
	
	public Response(boolean success_status, String response_string)
	{
		this.success_status = success_status;
		this.response_string = response_string;
	}
	
	public void setSuccessStatus(boolean success_status)
	{
		this.success_status = success_status;
	}

	public void setResponseString(String response_string)
	{
		this.response_string = response_string;
	}
	
	public void appendResponseString(String additional_response_string)
	{
		response_string += additional_response_string;
	}
	
	public void set(boolean success_status, String response_string)
	{
		this.success_status = success_status;
		this.response_string = response_string;
	}
	
	public boolean getSuccessStatus()
	{
		return success_status;
	}
	
	public String getResponseString()
	{
		return response_string;
	}
}
