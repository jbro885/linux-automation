package org.gunnm.openhomeautomation.logger;

import java.util.List;

import org.gunnm.openhomeautomation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter 
{
    Context context;
    List<Event> events;
    private static LayoutInflater inflater = null;

    public EventAdapter(Context context, List<Event> p_events) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.events = p_events;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() 
    {
        return events.size();
    }


    public Object getItem (int position) 
    {	
        return events.get(position);
    }


    public long getItemId (int position)
    {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.logger_event_row, null);
        TextView text_primary = (TextView) vi.findViewById(R.id.logger_event_label_primary);
        TextView text_secondary = (TextView) vi.findViewById(R.id.logger_event_label_secondary);
        text_primary.setText(events.get(position).getDateString() + "-" + Event.eventTypeToString (events.get(position).getType()));
        text_secondary.setText(events.get(position).getDetails());
        return vi;
    }
}