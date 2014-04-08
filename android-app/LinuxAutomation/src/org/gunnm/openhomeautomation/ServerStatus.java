package org.gunnm.openhomeautomation;

public class ServerStatus {
	private static boolean running = false;
	
	public static void setRunning (boolean b)
	{
		running = b;
	}
	
	public static boolean isRunning ()
	{
		return running;
	}
}
