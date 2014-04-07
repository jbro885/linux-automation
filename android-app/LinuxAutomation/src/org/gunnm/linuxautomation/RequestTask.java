package org.gunnm.linuxautomation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


class RequestTask extends AsyncTask<String, String, String>{
	private Activity relatedActivity = null;
	private RequestType requestType = null;
	private String errMessage = null;

	
	public void setActivity (Activity a)
	{
		this.relatedActivity = a;
	}
	
	public void setRequestType (RequestType rt)
	{
		this.requestType = rt;
	}
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
    protected String doInBackground(String... args) 
    {
    	
    	if (this.relatedActivity == null)
    	{
    		Log.d ("RequestTask" , "context is null, not trying to issue a request");
    	}
    	if (this.requestType == null)
    	{
    		Log.d ("RequestTask" , "requesttask is null");
    	}
    	
    	String serverString = PrefsUtils.getHostname (this.relatedActivity);
    	String serverPath = PrefsUtils.getServerPath (this.relatedActivity);
    	String serverUser = PrefsUtils.getServerUser (this.relatedActivity);
    	String serverPass = PrefsUtils.getServerPass (this.relatedActivity);

  		int port = PrefsUtils.getPort (this.relatedActivity);
  		String url = serverString + "/" + serverPath + "/autocontrol.pl?" + Utils.mapRequestTypeToHttpPost(this.requestType);
  		Log.d("[RequestTask]", "trying to get " + url);
  		Log.d("[RequestTask]", "username=" + serverUser);
  		Log.d("[RequestTask]", "userpass=" + serverPass);
  		DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            httpclient.getCredentialsProvider().setCredentials (new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), new UsernamePasswordCredentials(serverUser, serverPass));
            response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            Log.d ("[RequestTask]", "status code" + statusLine.getStatusCode());
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            }
            else
            {
                //Closes the connection.
                response.getEntity().getContent().close();

            	errMessage = " http error, code=" + statusLine.getStatusCode();
            }
        } catch (ClientProtocolException e) {
        	errMessage = e.getMessage();
        } catch (IOException e) {
        	errMessage = e.getMessage();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
    	Document xmldoc = null;
    	super.onPostExecute(result);

    	if (result == null)
    	{
    		String msg = "No answer when contacting the server";
    		if (errMessage != null)
    		{
    			msg = errMessage;
    		}
        	Utils.showError(relatedActivity, "Connection Error", msg);
        	return;
    	}

        try
        {
        	xmldoc = loadXMLFromString(result);

        }
        catch (Exception e)
        {
//        	Log.d("RequestTask","result=" + result);
        	Utils.showError(relatedActivity, "Answer error", "Error when parsing the answer");
        	return;
        }
        
    	
    	if (xmldoc.getChildNodes().getLength() != 1)
    	{
    		Utils.showError(relatedActivity, "Request error", "Inconsistent answer");
    		return;
    	}
    	
    	Node node = xmldoc.getChildNodes().item(0);
    	
    	
    	if (node.getNodeName().equalsIgnoreCase("error"))
    	{
    		Node attr = node.getAttributes().getNamedItem("type");
    		if (attr != null)
    		{
    			Log.d("RequestTask", "BLA" + attr.getNodeValue());
    			if (attr.getNodeValue().equalsIgnoreCase("noserver"))
    			{
    		  		Utils.showError(relatedActivity, "Unavailable", "Server is not available");
    	    		return;
    			}
    			if (attr.getNodeValue().equalsIgnoreCase("invalid-request"))
    			{
    		  		Utils.showError(relatedActivity, "Invalid Request", "Client issues an invalid request");
    	    		return;
    			}
    		}
	  		Utils.showError(relatedActivity, "Unknown Error", "Unknown error, please try again");
    		return;
    	}
    	
    	switch (this.requestType)
        {
        	case GET_GLOBAL_STATE:
        	{
        		Log.d ("RequestTask", "node name: " + node.getNodeName());
        		Node attr = node.getAttributes().getNamedItem("value");
        		if (attr != null)
        		{
        			if (attr.getNodeValue().equalsIgnoreCase("on"))
        			{
        				Utils.setActive(this.relatedActivity);
        			}
        			else
        			{
        				Utils.setInactive(this.relatedActivity);
        			}
        		}
        		break;
        	}
        	case SET_GLOBAL_STATE_ON:
        	{
        		Log.d ("RequestTask", "node name: " + node.getNodeName());
        		Node attr = node.getAttributes().getNamedItem("value");
        		if (attr != null)
        		{
        			if (attr.getNodeValue().equalsIgnoreCase("on"))
        			{
        				Utils.setActive(this.relatedActivity);
        			}
        			else
        			{
        				Utils.showError(relatedActivity, "Error", "Error when trying to activate the server");
        	    		
        				Utils.setInactive(this.relatedActivity);
        			}
        		}
        		break;
        	}
        	case SET_GLOBAL_STATE_OFF:
        	{
        		Log.d ("RequestTask", "node name: " + node.getNodeName());
        		Node attr = node.getAttributes().getNamedItem("value");
        		if (attr != null)
        		{
        			if (attr.getNodeValue().equalsIgnoreCase("off"))
        			{
        				Utils.setInactive(this.relatedActivity);
        			}
        			else
        			{
        				Utils.showError(relatedActivity, "Error", "Error when trying to deactivate the server");
        	    		
        				Utils.setInactive(this.relatedActivity);
        			}
        		}
        		break;
        	}
        }
    }
}