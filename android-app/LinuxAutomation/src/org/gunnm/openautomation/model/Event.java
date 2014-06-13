package org.gunnm.openautomation.model;

import java.util.Calendar;
import java.util.Date;

import android.text.method.DateTimeKeyListener;
import android.util.Log;

public class Event {
	private EventType type;
	private String details;
	private Date date;
	
	public Event (String p_type, String p_date, String p_details)
	{
		this.type = stringToEventType(p_type);
		this.date = parseDate(p_date);
		this.details = p_details;
	}
	
	public EventType getType ()
	{
		return this.type;
	}
	
	public String getDetails ()
	{
		return this.details;
	}
	
	public Date getDate ()
	{
		return this.date;
	}
	
	public void setDate (String s)
	{
		this.date = parseDate(s);
	}
	
	public void setDate (Date d)
	{
		this.date = d;
	}
	
	public void setDetails (String s)
	{
		this.details = s;
	}
	
	public void setType (String t)
	{
		this.type = stringToEventType(t);
	}
	
	
	public static EventType stringToEventType (String s)
	{
		if (s.equalsIgnoreCase("camera"))
		{
			return EventType.CAMERA;
		}
		
		if (s.equalsIgnoreCase("door"))
		{
			return EventType.DOOR;
		}
		return EventType.NONE;
	}
	
	public static String eventTypeToString (EventType ev)
	{
		switch (ev)
		{
			case CAMERA:
				return "camera";
			case DOOR:
				return "door";
			default:
				return "none";
		}
	}
	
	public static Date parseDate (String eventDate)
	{
		String year;
		String month;
		String day;
		String hour;
		String min;
		String sec;
		Date   retDate;
		
		year 	= eventDate.substring(0, 4);
		month 	= eventDate.substring(4, 6);
		day 	= eventDate.substring(6, 8);
		hour 	= eventDate.substring(8, 10);
		min 	= eventDate.substring(10, 12);
		sec 	= eventDate.substring(12, 14);
		
		Log.d ("Event" , "year=" + year);
		Log.d ("Event" , "month=" + month);
		Log.d ("Event" , "day=" + day);
		retDate = new Date (Integer.parseInt(year),
							Integer.parseInt(month),
							Integer.parseInt(day),
							Integer.parseInt(hour),
							Integer.parseInt(min),
							Integer.parseInt(sec));
		return retDate;
	}
	
	public String getDateString ()
	{
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(this.date);
		return date.getYear() + "-" + date.getMonth() + "-" + cal.get(Calendar.DAY_OF_MONTH)  + " " + this.date.getHours() +":" + this.date.getMinutes() +":" + this.date.getSeconds();
	}
}
