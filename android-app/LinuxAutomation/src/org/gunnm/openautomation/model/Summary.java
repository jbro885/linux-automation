package org.gunnm.openautomation.model;

import java.util.ArrayList;
import java.util.List;

public class Summary {
	private List<Device> devices;
	
	public Summary()
	{
		this.devices = new ArrayList<Device>();
	}
	
	public void addDevice (Device d)
	{
		this.devices.add(d);
	}
	
	public List<Device> getDevices ()
	{
		return this.devices;
	}
	
}
