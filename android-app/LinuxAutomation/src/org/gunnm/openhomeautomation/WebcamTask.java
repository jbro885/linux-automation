package org.gunnm.openhomeautomation;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class WebcamTask extends AsyncTask<String, String, Bitmap>{
	private Activity relatedActivity = null;

	private ProgressBar progressBar = null;
	private TextView    textView    = null;
	
	public void setActivity (Activity a)
	{
		this.relatedActivity = a;
	}
	
	public void setProgressBar (ProgressBar pb)
	{
		this.progressBar = pb;
	}
	
	
	public void setTextView (TextView tv)
	{
		this.textView = tv;
	}
	
	protected void onPreExecute()
	{
		if (progressBar != null)
		{
	        progressBar.setVisibility(View.VISIBLE);
		}
		
		if (this.textView != null)
		{
			this.textView.setText("Updating picture ...");
		}
	}

	private Bitmap loadBitMap (String url)
	{
		Bitmap image = null;
		InputStream in = null;
		
		try {

	    	final String serverUser = PrefsUtils.getServerUser (this.relatedActivity);
	    	final String serverPass = PrefsUtils.getServerPass (this.relatedActivity);
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication (serverUser, serverPass.toCharArray());
			    }
			});
			java.net.URL javaURL = new java.net.URL(url);
			in = javaURL.openStream();
			image = BitmapFactory.decodeStream(in);
			in.close();
		} catch (Exception e) {
//			Log.e("WebcamTask", "Error when trying to get the pic " + e.getMessage());

			try {
				in.close();
			} catch (IOException e1) {
//				Log.e("WebcamTask", "cannot close " + e.getMessage());
			}

		}
		return image;
	}

	protected Bitmap doInBackground(String... args) 
	{
		Bitmap pic = null;
		
		//	      img.setImageResource(R.drawable.ko);

		if (this.relatedActivity == null)
		{
//			Log.d ("WebcamTask" , "context is null, not trying to issue a request");
			return null;
		}

		if (ServerStatus.isRunning())
		{
//			Log.d ("WebcamTask" , "update pic");
	  		String url = PrefsUtils.getHostname (this.relatedActivity) + ":" + PrefsUtils.getPort(this.relatedActivity) +"/" + PrefsUtils.getServerPath (this.relatedActivity) + "/autocontrol.pl?request=get-webcam-picture";
	  		

			pic = loadBitMap(url);
		}
		else
		{
//			Log.d ("WebcamTask" , "NOT update pic");

		}
		return pic;
	}
	
    protected void onPostExecute(Bitmap result)
    {
    	if (progressBar != null)
    	{
    		progressBar.setVisibility(View.GONE);
    	}
		
    	if (this.textView != null)
		{
    		DateFormat dateFormat = DateFormat.getDateTimeInstance();
    		Calendar cal = Calendar.getInstance();
    		String str = "Last picture updated at " + dateFormat.format(cal.getTime());
			this.textView.setText(str);
		}
		
    	ImageView webcam = (ImageView) this.relatedActivity.findViewById(R.id.webcam_view);
        webcam.setImageBitmap(result);
    }
}