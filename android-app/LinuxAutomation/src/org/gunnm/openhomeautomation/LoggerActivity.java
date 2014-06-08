package org.gunnm.openhomeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoggerActivity extends Activity {
	public static LoggerActivity instance;


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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logger);

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
