package org.gunnm.openhomeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	public static MainActivity instance;
	public static final int ONE_SECOND = 1000;
	
	private boolean updateInProgress = false;
	
	private Handler mHandler = new Handler();
	
		
	private Runnable periodicTask = new Runnable()
	{
        public void run()
        {
        	int refresh = PrefsUtils.getRefreshPeriod (instance);
        	
	        if ((refresh != 0) && (ServerStatus.isRunning()))
	        {
	            Log.d("PeriodicTimerService","Awake");
	        	refreshWebcam();
        	}
	        
	        if (refresh == 0)
	        {
	        	refresh = 10;
	        }
	        
	        mHandler.postDelayed(periodicTask, refresh * ONE_SECOND);
        }
    };
	
    public void onStart()
    {
       super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(periodicTask);
//        Toast.makeText(this, "Service onDestroy() ", Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	instance = this;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /**
         * The button can activate/deactivate the webcam.
         */
        final Button button = (Button) findViewById(R.id.button_activate);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	RequestTask rt = new RequestTask();
          		ProgressBar pb = (ProgressBar) instance.findViewById(R.id.progressBar);
          		rt.setProgressBar(pb);
    	  		rt.setActivity(MainActivity.instance);
    	  		
    	  		/**
    	  		 * If the server is active, the user pushes the button
    	  		 * to turn it off.
    	  		 */
                if (ServerStatus.isRunning())
                {
                	rt.setRequestType (RequestType.SET_GLOBAL_STATE_OFF);
                }
                
                /**
                 * If the server is currently inactive, the user
                 * push the button to make it active.
                 */
                if (! ServerStatus.isRunning())
                {
                	rt.setRequestType (RequestType.SET_GLOBAL_STATE_ON);
                }
                
               rt.execute();
            }
        });
        
        /**
         * When we click on the webcam area on the phone, it updates
         * the picture in the application.
         */
    	ImageView webcam = (ImageView) findViewById(R.id.webcam_view);
    	webcam.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (ServerStatus.isRunning())
                {
                	refreshWebcam();
                }          
            }
        });
        
    	/**
    	 * Refresh the actual status of the remote server.
    	 */
    	refreshStatus ();
    	
    	/*
    	 * Create the handler that update the webcam view
    	 */
    	mHandler.postDelayed(periodicTask, ONE_SECOND);
    }
    
    private synchronized void refreshStatus ()
    {
    	if (updateInProgress)
    	{
    		return;
    	}
    	updateInProgress = true;
    
    	
  		RequestTask rt = new RequestTask();
  		ProgressBar pb = (ProgressBar) this.findViewById(R.id.progressBar);
  		rt.setProgressBar(pb);
  		rt.setActivity(instance);
  		rt.setRequestType (RequestType.GET_GLOBAL_STATE);
  		rt.execute();

  		updateInProgress = false;

    }
    
    
    private synchronized void refreshWebcam ()
    {
    	/**
    	 * If the remote server is not running,
    	 * we do not update the webcam picture.
    	 */
    	if ( ! ServerStatus.isRunning())
    	{
    		return;
    	}
    	
    	/**
    	 * If an update is already in progress,
    	 * we do not run the code and return.
    	 */
    	if (updateInProgress)
    	{
    		return;
    	}
    	
    	updateInProgress = true;
  		WebcamTask webcamTask;
        webcamTask = new WebcamTask();
		ProgressBar pb = (ProgressBar) this.findViewById(R.id.progressBar);
		webcamTask.setProgressBar(pb);
        webcamTask.setActivity(instance);
        webcamTask.execute("");
        
        updateInProgress = false;
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
    	  		Intent i = new Intent(this, org.gunnm.openhomeautomation.SettingsActivity.class);
    	  		startActivity(i);
    	  		return true;
    	  	}
    	  	case R.id.refresh:
    	  	{
    	  		refreshStatus();
    	  		refreshWebcam();

    	  		return true;
    	  	}
    	  }
    	  return true;
    	} 
}
