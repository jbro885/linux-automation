package org.gunnm.linuxautomation;

public class ServerStatus {
	private static boolean running = false;
	
	public void setRunning (boolean b)
	{
		this.running = b;
	}
	
	public boolean isRunning ()
	{
		return this.running;
	}
}
