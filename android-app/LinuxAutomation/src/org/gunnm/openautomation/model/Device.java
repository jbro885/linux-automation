package org.gunnm.openautomation.model;

public class Device {
	private DeviceType 		type;
	private DeviceStatus 	status;
	private String 			name;
	
	public Device(String p_name)
	{
		this.name = p_name;
	}
	
	public void setStatus (DeviceStatus ds)
	{
		this.status = ds;
	}
	
	public void setType (DeviceType dt)
	{
		this.type = dt;
	}
	
	public DeviceType getType ()
	{
		return this.type;
	}
	
	public DeviceStatus getStatus ()
	{
		return this.status;
	}
	
	public String getName ()
	{
		return this.name;
	}
	
}
