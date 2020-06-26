package com.example.sharewave.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sharewave.MainActivity;
import com.example.sharewave.R;
import com.example.sharewave.classes.Location;
import com.example.sharewave.ui.home.SpecificFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    private List<Location> locationList = null;
    private ArrayList<Location> locationArrayList;

    public LocationAdapter(Context ctx,List<Location> locationList){

        this.ctx = ctx;
        this.locationList=locationList;
        inflater = LayoutInflater.from(ctx);
        this.locationArrayList = new ArrayList<Location>();
        this.locationArrayList.addAll(locationList);
    }

    public class ViewHolder{
        TextView name,location;
    }


    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Location getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();

            view = inflater.inflate(R.layout.list_view_items, null);

            // Locate the TextViews in listview_item.xml
            holder.name = view.findViewById(R.id.name);
            holder.location = view.findViewById(R.id.location);

            view.setTag(holder);

        } else {

            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.name.setText(locationList.get(position).getBeach_name());
        holder.location.setText(locationList.get(position).getLocation_name());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("id",locationList.get(position).getId());
                bundle.putString("beach_name",locationList.get(position).getBeach_name());
                bundle.putString("location_name",locationList.get(position).getLocation_name());
                bundle.putString("latitude",locationList.get(position).getLatitude());
                bundle.putString("longitude",locationList.get(position).getLongitude());


                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                SpecificFragment myFragment = new SpecificFragment();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        locationList.clear();

        if (charText.length() == 0) {

            locationList.addAll(locationArrayList);

        } else {

            for (Location location : locationArrayList) {
                if (location.getBeach_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    locationList.add(location);
                }
            }
        }

        notifyDataSetChanged();

    }
}
