package com.example.sharewave.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sharewave.R;
import com.example.sharewave.classes.Location;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter extends ArrayAdapter<Location> {

    public SpinnerAdapter (Context context, ArrayList<Location> locationArrayList){

        super(context,0 , locationArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView (int position, View convertView, ViewGroup parent){

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_items, parent, false
            );
        }

        TextView txtBeach = convertView.findViewById(R.id.name);
        TextView txtLocation = convertView.findViewById(R.id.location);

        Location location = (Location) getItem(position);

        if (location != null){
            txtBeach.setText(location.getBeach_name());
            txtLocation.setText(location.getLocation_name());
        }

        return convertView;

    }
}
