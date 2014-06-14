package org.gunnm.openhomeautomation.logger;

import java.util.List;

import org.gunnm.openautomation.model.Device;
import org.gunnm.openautomation.model.DeviceStatus;
import org.gunnm.openautomation.model.Event;
import org.gunnm.openautomation.model.Summary;
import org.gunnm.openhomeautomation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        String textInfo;
        Device dev = summary.getDevices().get(position);
        
        if (vi == null)
            vi = inflater.inflate(R.layout.logger_summary_row, null);
        TextView text_primary = (TextView) vi.findViewById(R.id.logger_event_label_primary);
        TextView text_secondary = (TextView) vi.findViewById(R.id.logger_event_label_secondary);
        ImageView img = (ImageView) vi.findViewById(R.id.device_status);
        
        
        if (dev.getStatus() == DeviceStatus.ONLINE)
        {
        	img.setImageResource(R.drawable.offline);
        }
        else
        {
        	img.setImageResource(R.drawable.online);
        }
        
        textInfo = "great device";
        text_primary.setText(dev.getName());
        text_secondary.setText(textInfo);
        return vi;
    }
}