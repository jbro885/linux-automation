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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button button = (Button) findViewById(R.id.button_activate);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Log.d ("MainActivity", "click on button");
//                ImageView img= (ImageView) findViewById(R.id.Status);
//                img.setImageResource(R.drawable.ok);
            }
        });
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
    	  		RequestTask rt = new RequestTask();
    	  		rt.setContext(this);
    	  		rt.setRequestType (RequestType.GET_GLOBAL_STATE);
    	  		return true;
    	  	}
    	  }
    	  return true;
    	} 
}
