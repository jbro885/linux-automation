package org.gunnm.linuxautomation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu; 
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
    	  MenuInflater inflater = getMenuInflater();

    	    inflater.inflate(R.menu.main_menu, menu);
    	    return true;
    	}

    	// This method is called once the menu is selected
    	@Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    	  switch (item.getItemId())
    	  {
    	  	case R.id.settings:
    	  	{
    	  		Intent i = new Intent(this, org.gunnm.linuxautomation.SettingsActivity.class);
    	  		startActivity(i);
    	  		return true;
    	  	}
    	  	case R.id.refresh:
    	  	{
    	  		String serverString;
    	  		String portString;
    	  		int port;
    	  		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	  
    	  		serverString = prefs.getString ("server_addr", "none");
    	  		portString   = prefs.getString ("server_port", "1234");
    	  		Log.d("bla", "trying to connect on " + serverString + ":" + portString);
    	  		new RequestTask().execute("http://stackoverflow.com");
    	  		
    	  		return true;
    	  	}
    	  }
    	  return true;
    	} 
}
