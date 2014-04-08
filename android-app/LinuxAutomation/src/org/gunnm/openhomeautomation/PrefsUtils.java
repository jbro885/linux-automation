package org.gunnm.openhomeautomation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtils {

	private static SharedPreferences prefs = null;
	
	private static void checkSharedPreferences (Context c)
	{
		if (prefs == null)
		{
			prefs = PreferenceManager.getDefaultSharedPreferences(c);
		}
	}
	
	public static String getHostname (Context context)
	{
		checkSharedPreferences (context);
		return prefs.getString ("server_addr", "none");
	}
	
	public static String getServerUser (Context context)
	{
		checkSharedPreferences (context);
		return prefs.getString ("server_user", "none");
	}
	
	public static String getServerPass (Context context)
	{
		checkSharedPreferences (context);
		return prefs.getString ("server_pass", "none");
	}
	
	public static String getServerPath (Context context)
	{
		checkSharedPreferences (context);
		return prefs.getString ("server_path", "httpcontrol");
	}
	
	public static int getPort (Context context)
	{
		checkSharedPreferences (context);
		try
		{
			String tmp = prefs.getString ("server_port", "none");
			return Integer.parseInt(tmp);
		}
		catch (NumberFormatException nfe)
		{
			return 1234;
		}
	}
}
