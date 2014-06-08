package org.gunnm.openhomeautomation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LoggerActivity extends Activity {
	
	List<Map<String, String>> planetsList = new ArrayList<Map<String,String>>();
		 
		 
		   private void initList() {
		    // We populate the planets
		 
		    planetsList.add(createPlanet("planet", "Mercury"));
		    planetsList.add(createPlanet("planet", "Venus"));
		    planetsList.add(createPlanet("planet", "Mars"));
		    planetsList.add(createPlanet("planet", "Jupiter"));
		    planetsList.add(createPlanet("planet", "Saturn"));
		    planetsList.add(createPlanet("planet", "Uranus"));
		    planetsList.add(createPlanet("planet", "Neptune"));
		 
		}
		   
		   private HashMap<String, String> createPlanet(String key, String name) {
			   	    HashMap<String, String> planet = new HashMap<String, String>();
			   	    planet.put(key, name);
			   	 
			   	    return planet;
			   	}
		 

	
	public void onStart()
	{
		super.onStart();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
//		Log.d("Logger", "creating the logger");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logger);

//		Log.d("Logger", "logger created");
		
		ListView myListView = (ListView) findViewById(R.id.allevents);
	
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, planetsList, android.R.layout.simple_list_item_1, new String[] {"planet"}, new int[] {android.R.id.text1});
		myListView.setAdapter(simpleAdpt);
		initList();
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.logger_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		return true;
	} 
}
