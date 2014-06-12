package org.gunnm.openhomeautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ImageView;

public class Utils
{
	
	
	/**
	 * Function to easily show and report an error
	 * @param relatedActivity - the activity where the error/pop-up appears
	 * @param title           - title of the pop-up
	 * @param message         - message shown
	 */
	public static void showError   (Activity relatedActivity,
									String title,
									String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(relatedActivity);
		// Add the buttons
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		           }
		       });
		builder.setMessage(message);
		builder.setTitle(title);

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	/**
	 * Map a RequestType into a string that will be issued
	 * to the server.
	 * @param requestType
	 * @return
	 */
	public static String mapRequestTypeToHttpPost (RequestType requestType)
	{
		String ret = "none";
		
		if (requestType == RequestType.GET_GLOBAL_STATE)
		{
			ret = "request=get-global-status";
		}
		if (requestType == RequestType.SET_GLOBAL_STATE_ON)
		{
			ret = "request=set-global-status&value=on";
		}
		if (requestType == RequestType.SET_GLOBAL_STATE_OFF)
		{
			ret = "request=set-global-status&value=off";
		}
		
		if (requestType == RequestType.GET_SUMMARY)
		{
			ret = "request=get-summary";
		}
		
		if (requestType == RequestType.GET_EVENTS)
		{
			ret = "request=get-events";
		}
		return ret;
	}
	
	/**
	 * Declare the server as active and update the GUI and
	 * all the rest of the architecture.
	 * @param act
	 */
	public static void setActive (Activity act)
	{
      ImageView img = (ImageView) act.findViewById(R.id.Status);
      img.setImageResource(R.drawable.ok);
      
      Button button = (Button) act.findViewById(R.id.button_activate);
      button.setText ("Click to deactivate");
      ServerStatus.setRunning(true);
	}
	
	/**
	 * Declare the server as inactive and update the GUI
	 * accordingly.
	 * @param act
	 */
	public static void setInactive (Activity act)
	{
      ImageView img = (ImageView) act.findViewById(R.id.Status);
      img.setImageResource(R.drawable.ko);
      
      Button button = (Button) act.findViewById(R.id.button_activate);
      button.setText ("Click to activate");
      ServerStatus.setRunning(false);
	}
}
