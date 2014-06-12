package org.gunnm.openhomeautomation.logger;

import java.util.List;

import org.gunnm.openautomation.model.Event;
import org.gunnm.openautomation.model.Summary;
import org.gunnm.openhomeautomation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SummaryAdapter extends BaseAdapter 
{
    Context context;
    Summary summary;
    private static LayoutInflater inflater = null;

    public SummaryAdapter(Context context, Summary p_sum) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.summary = p_sum;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() 
    {
        return summary.getDevices().size();
    }


    public Object getItem (int position) 
    {	
        return summary.getDevices().get(position);
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
        text_primary.setText(summary.getDevices().get(position).getName());
        return vi;
    }
}