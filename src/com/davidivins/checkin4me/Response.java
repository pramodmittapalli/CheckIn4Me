package com.davidivins.checkin4me;

/**
 * Response
 * 
 * @author david
 */
public class Response 
{
	protected boolean success_status;
	protected String response_string;
	
	/**
	 * Response
	 */
	public Response()
	{
		this.success_status = false;
		this.response_string = "";
	}
	
	/**
	 * Response
	 * 
	 * @param success_status
	 * @param response_string
	 */
	public Response(boolean success_status, String response_string)
	{
		this.success_status = success_status;
		this.response_string = response_string;
	}
	
	/**
	 * setSuccessStatus
	 * 
	 * @param success_status
	 */
	public void setSuccessStatus(boolean success_status)
	{
		this.success_status = success_status;
	}

	/**
	 * setResponseString
	 * 
	 * @param response_string
	 */
	public void setResponseString(String response_string)
	{
		this.response_string = response_string;
	}
	
	/**
	 * appendResponseString
	 * 
	 * @param additional_response_string
	 */
	public void appendResponseString(String additional_response_string)
	{
		response_string += additional_response_string;
	}
	
	/**
	 * set
	 * 
	 * @param success_status
	 * @param response_string
	 */
	public void set(boolean success_status, String response_string)
	{
		this.success_status = success_status;
		this.response_string = response_string;
	}
	
	/**
	 * getSuccessStatus
	 * 
	 * @return boolean
	 */
	public boolean getSuccessStatus()
	{
		return success_status;
	}
	
	/**
	 * getResponseString
	 * 
	 * @return String
	 */
	public String getResponseString()
	{
		return response_string;
	}
}
