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










import org.gunnm.openautomation.model.Event;
import org.gunnm.openautomation.model.EventType;
import org.gunnm.openautomation.model.Summary;
import org.gunnm.openhomeautomation.R;
import org.gunnm.openhomeautomation.RequestTask;
import org.gunnm.openhomeautomation.RequestType;
import org.gunnm.openhomeautomation.R.id;
import org.gunnm.openhomeautomation.R.layout;
import org.gunnm.openhomeautomation.R.menu;
import org.gunnm.openhomeautomation.ServerStatus;
import org.gunnm.openhomeautomation.logger.EventAdapter;




import org.gunnm.openhomeautomation.logger.SummaryAdapter;

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
	Summary summary = new Summary();
	private static boolean updateEventsInProgress = false;
	private static boolean updateSummaryInProgress = false;
	private static Logger instance;
	
	public void setEvents (List<Event> list)
	{
		this.allEvents = list;
	}
	
	public void setSummary (Summary p_summary)
	{
		this.summary = p_summary;
	}
	
	public void refreshEventList ()
	{	
  		ListView listView = (ListView) findViewById(R.id.logger_allevents_list); 
        listView.setAdapter(new EventAdapter(this, allEvents));
  	
	}

	public void refreshSummaryList ()
	{	
		
  		ListView listView = (ListView) findViewById(R.id.logger_summary); 
        listView.setAdapter(new SummaryAdapter(this, summary));
  	
	}

	
	
	public void refreshSummary()
	{
		if (! ServerStatus.isRunning())
		{
			return;
		}
		
    	if (updateSummaryInProgress)
    	{
    		return;
    	}
    	
    	updateSummaryInProgress = true;
    	
  		RequestTask rt = new RequestTask();
  		ProgressBar pb = (ProgressBar) this.findViewById(R.id.progressBarLogger);
  		rt.setProgressBar(pb);
  		rt.setActivity(instance);
  		rt.setRequestType (RequestType.GET_SUMMARY);
  		rt.execute();
  		updateSummaryInProgress = false;
	}
	
	


	private synchronized void refreshEvents ()
    {
		if (! ServerStatus.isRunning())
		{
			return;
		}
		
    	if (updateEventsInProgress)
    	{
    		return;
    	}
    	updateEventsInProgress = true;
    
    	
  		RequestTask rt = new RequestTask();
  		ProgressBar pb = (ProgressBar) this.findViewById(R.id.progressBarLogger);
  		rt.setProgressBar(pb);
  		rt.setActivity(instance);
  		rt.setRequestType (RequestType.GET_EVENTS);
  		rt.execute();
  		
  		updateEventsInProgress = false;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logger);

		instance = this;
		
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
