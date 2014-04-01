package org.gunnm.linuxautomation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


class WebcamTask extends AsyncTask<String, String, Bitmap>{
	private Activity relatedActivity = null;
	private static String IMG_INACTIVE = "";
	private static String IMG_ACTIVE   = "http://www.hazecam.net/images/main/newark.jpg";
	

	public void setActivity (Activity a)
	{
		this.relatedActivity = a;
	}

	private Bitmap loadBitMap (String url)
	{
		Bitmap image = null;
		try {
			InputStream in = new java.net.URL(url).openStream();
			image = BitmapFactory.decodeStream(in);
			in.close();
		} catch (Exception e) {
			Log.e("WebcamTask", "Error when trying to get the pic " + e.getMessage());
		}
		return image;
	}

	protected Bitmap doInBackground(String... args) 
	{
		Bitmap pic = null;
		
		//	      img.setImageResource(R.drawable.ko);

		if (this.relatedActivity == null)
		{
			Log.d ("WebcamTask" , "context is null, not trying to issue a request");
			return null;
		}

		if (ServerStatus.isRunning())
		{
			Log.d ("WebcamTask" , "update pic");
	  		String url = PrefsUtils.getHostname (this.relatedActivity) + "/" + PrefsUtils.getServerPath (this.relatedActivity) + "/autocontrol.pl?request=get-webcam-picture";

			pic = loadBitMap(url);
		}
		else
		{
			Log.d ("WebcamTask" , "NOT update pic");

		}
		return pic;
	}
	
    protected void onPostExecute(Bitmap result) {
    	ImageView webcam = (ImageView) this.relatedActivity.findViewById(R.id.webcam_view);
        webcam.setImageBitmap(result);
    }
}