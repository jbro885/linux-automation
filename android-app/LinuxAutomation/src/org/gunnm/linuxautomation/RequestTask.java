package org.gunnm.linuxautomation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


class RequestTask extends AsyncTask<String, String, String>{
	private Context context = null;
	private RequestType requestType = null;
	
	public void setContext (Context c)
	{
		this.context = c;
	}
	
	public void setRequestType (RequestType rt)
	{
		this.requestType = rt;
	} 
	
    protected String doInBackground(String... uri) 
    {
    	if (context == null)
    	{
    		Log.d ("RequestTask" , "context is null, not trying to issue a request");
    	}
    	if (context == null)
    	{
    		Log.d ("RequestTask" , "requesttask is null");
    	}
    	String serverString = PrefsUtils.getHostname (this.context);
  		int port = PrefsUtils.getPort (this.context);

  		Log.d("bla", "trying to connect on " + serverString + ":" + port);
  	
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        	Log.d("RequestTask", "io exception when trying to connect");
        } catch (IOException e) {
        	Log.d("RequestTask", "io exception when trying to connect");
        }
       // Log.d("RequestTask", "return" + responseString);
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("RequestTask", "do something on post execute");
    }
}