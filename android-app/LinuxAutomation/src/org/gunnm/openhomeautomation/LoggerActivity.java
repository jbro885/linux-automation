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

	List<Map<String, String>> allEventsList = new ArrayList<Map<String,String>>();
	List<Map<String, String>> summary       = new ArrayList<Map<String,String>>();

	public void refreshEventList ()
	{
		HashMap <String,String> entry = new HashMap<String,String> ();
		entry.put("event", "myevent");

		allEventsList.add (entry);
	}
	
	public void refreshSummary()
	{
		HashMap <String,String> entry = new HashMap<String,String> ();
		entry.put("info", "summaryline1");

		summary.add (entry);
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
		ListView 		listView;
		SimpleAdapter 	simpleAdpt;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logger);

		//		Log.d("Logger", "logger created");

		listView = (ListView) findViewById(R.id.logger_allevents_list);

		simpleAdpt = new SimpleAdapter(this, this.allEventsList, android.R.layout.simple_list_item_1, new String[] {"event"}, new int[] {android.R.id.text1});
		listView.setAdapter(simpleAdpt);
	
		listView = (ListView) findViewById(R.id.logger_summary);

		simpleAdpt = new SimpleAdapter(this, this.summary, android.R.layout.simple_list_item_1, new String[] {"info"}, new int[] {android.R.id.text1});
		listView.setAdapter(simpleAdpt);
		
		
		refreshEventList ();

		refreshSummary();

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
