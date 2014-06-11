package org.gunnm.openhomeautomation.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.List;




import org.gunnm.openhomeautomation.R;
import org.gunnm.openhomeautomation.RequestTask;
import org.gunnm.openhomeautomation.RequestType;
import org.gunnm.openhomeautomation.R.id;
import org.gunnm.openhomeautomation.R.layout;
import org.gunnm.openhomeautomation.R.menu;

import org.gunnm.openhomeautomation.logger.Event;

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

public class Logger extends Activity {

	List<Event> allEvents = new ArrayList<Event>();
	List<Map<String, String>> allEventsList = new ArrayList<Map<String,String>>();
	List<Map<String, String>> summary       = new ArrayList<Map<String,String>>();
	private static boolean updateInProgress = false;
	private static Logger instance;
	
	public void setEvents (List<Event> list)
	{
		this.allEvents = list;
	}
	
	public void refreshEventList ()
	{
		HashMap <String,String> entry = new HashMap<String,String> ();
		entry.put("event", "bla1");
		this.allEventsList.add(entry);
		
		for (Event evt : allEvents)
		{
			entry = new HashMap<String,String> ();
			entry.put("event", "bla" + evt.getDetails());
			Log.d("Logger", "adding" + evt.getDetails());
			this.allEventsList.add(entry);
		}
		
  		ListView listView = (ListView) findViewById(R.id.logger_allevents_list);
  		SimpleAdapter simpleAdpt = new SimpleAdapter(this, this.allEventsList, android.R.layout.simple_list_item_1, new String[] {"event"}, new int[] {android.R.id.text1});
		listView.setAdapter(simpleAdpt);
  		updateInProgress = false;
		
	}
	
	public void refreshSummary()
	{
		HashMap <String,String> entry = new HashMap<String,String> ();
		entry.put("info", "summaryline1");

		summary.add (entry);
	}
	
	


	private synchronized void refreshEvents ()
    {
    	if (updateInProgress)
    	{
    		return;
    	}
    	updateInProgress = true;
    
    	
  		RequestTask rt = new RequestTask();
  		ProgressBar pb = (ProgressBar) this.findViewById(R.id.progressBarLogger);
  		rt.setProgressBar(pb);
  		rt.setActivity(instance);
  		rt.setRequestType (RequestType.GET_EVENTS);
  		rt.execute();


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

		
		instance = this;
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
	
		
		refreshEvents();
		
		

		refreshSummary();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.logger_menu, menu);
		return true;
	}

 	public boolean onOptionsItemSelected(MenuItem item) {
  	  switch (item.getItemId())
  	  {
  	  	case R.id.openmain:
  	  	{
  	  		Intent i = new Intent(this, org.gunnm.openhomeautomation.activities.Main.class);
  	  		startActivity(i);
  	  		return true;
  	  	}

  	  }
  	  return true;
  	} 
}
