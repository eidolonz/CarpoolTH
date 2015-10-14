package com.parse.carpool;

/**
 * Created by JUMRUS on 21/9/2558.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.integratingfacebooktutorial.R;

public class
        ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Trip> triplist = null;
    private ArrayList<Trip> arraylist;

    public ListViewAdapter(Context context,
                           List<Trip> triplist) {
        this.context = context;
        this.triplist = triplist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Trip>();
        this.arraylist.addAll(triplist);
    }

    public class ViewHolder {
        TextView source;
        TextView destination;
        TextView population;
        ImageView flag;
    }

    @Override
    public int getCount() {
        return triplist.size();
    }

    @Override
    public Object getItem(int position) {
        return triplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview, null);
            // Locate the TextViews in listview_item.xml
            holder.source = (TextView) view.findViewById(R.id.source);
            holder.destination = (TextView) view.findViewById(R.id.destination);
            //holder.population = (TextView) view.findViewById(R.id.createBy);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.source.setText(triplist.get(position).getSource());
        holder.destination.setText(triplist.get(position).getDestination());
        //holder.population.setText(triplist.get(position).getCreateBy().getString("name"));
        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, TripDetail.class);
                // Pass all data rank
                intent.putExtra("TripId",
                        (triplist.get(position).getObjectId()));
                intent.putExtra("Name",
                        (triplist.get(position).getName()));
                // Start SingleItemView Class
                context.startActivity(intent);
            }
        });
        return view;
    }

}