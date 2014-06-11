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
import org.gunnm.openhomeautomation.ServerStatus;
import org.gunnm.openhomeautomation.logger.Event;
import org.gunnm.openhomeautomation.logger.EventAdapter;
import org.gunnm.openhomeautomation.logger.EventType;


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

		for (Event evt : allEvents)
		{
			HashMap <String,String> entry = new HashMap<String,String> ();
			entry.put("event", evt.getDateString() + " " + Event.eventTypeToString(evt.getType()) + evt.getDetails());
			Log.d("Logger", "adding" + evt.getDetails());
			this.allEventsList.add(entry);
		}
		
  		ListView listView = (ListView) findViewById(R.id.logger_allevents_list); 
        listView.setAdapter(new EventAdapter(this, allEvents));
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
		if (! ServerStatus.isRunning())
		{
			return;
		}
		
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


		listView = (ListView) findViewById(R.id.logger_summary);

		simpleAdpt = new SimpleAdapter(this, this.summary, android.R.layout.simple_list_item_2, new String[] {"info"}, new int[] {android.R.id.text1});
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
