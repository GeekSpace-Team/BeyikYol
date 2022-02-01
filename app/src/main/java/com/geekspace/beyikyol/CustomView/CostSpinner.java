package com.geekspace.beyikyol.CustomView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Model.Year;

import java.util.ArrayList;

public class CostSpinner extends ArrayAdapter {
    Typeface trebu;
    public CostSpinner(@NonNull Context context, ArrayList<Year> customlist) {
        super(context, 0, customlist);
        trebu=Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Regular.otf");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_sp,parent,false);

        }
        Year item= (Year) getItem(position);

        TextView spinnerTv=convertView.findViewById(R.id.tvSpinnerLayout);
        spinnerTv.setTypeface(trebu);
        ImageView ivSpinnerLayout=convertView.findViewById(R.id.ivSpinnerLayout);
        if (item!=null) {

            spinnerTv.setText(item.getValue());
            ivSpinnerLayout.setImageResource(item.getImage());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);

        }
        Year item= (Year) getItem(position);

        TextView dropDownTV=convertView.findViewById(R.id.tvDropDownLayout);
        dropDownTV.setTypeface(trebu);
        ImageView ivDropDownLayout=convertView.findViewById(R.id.ivDropDownLayout);
        if (item!=null) {

            dropDownTV.setText(item.getValue());
            ivDropDownLayout.setImageResource(item.getImage());
        }
        return convertView;
    }
}
