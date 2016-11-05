package tech.team17;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ryu on 5/11/16.
 */

public class BusAdapter extends ArrayAdapter<Bus> {
    // View lookup cache
    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvNoPass;
        public TextView tvTime;
    }

    public BusAdapter(Context context, ArrayList<Bus> aBus) {
        super(context, 0, aBus);
    }

    // Translates a particular `Book` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Bus bus = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_bus, parent, false);
            viewHolder.ivCover = (ImageView)convertView.findViewById(R.id.ivBusCover);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            viewHolder.tvNoPass = (TextView)convertView.findViewById(R.id.tvNoPass);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvTitle.setText(bus.getTitle());
        viewHolder.tvNoPass.setText(bus.getNoPass());
        Picasso.with(getContext()).load(Uri.parse(bus.getCoverUrl())).error(R.drawable.cast_ic_notification_disconnect).into(viewHolder.ivCover);
        // Return the completed view to render on screen
        return convertView;
    }
}
