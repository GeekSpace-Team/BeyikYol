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

import com.geekspace.beyikyol.Model.LangClass;
import com.geekspace.beyikyol.R;

import java.util.ArrayList;

public class Custom_Spinner2 extends ArrayAdapter {
    Typeface trebu;
    public Custom_Spinner2(@NonNull Context context, ArrayList<LangClass> customlist) {
        super(context, 0, customlist);
        trebu=Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout,parent,false);

        }
        LangClass item= (LangClass) getItem(position);

        TextView spinnerTv=convertView.findViewById(R.id.tvSpinnerLayout);
        spinnerTv.setTypeface(trebu);
        ImageView ivSpinnerLayout=convertView.findViewById(R.id.ivSpinnerLayout);
        if (item!=null) {

            spinnerTv.setText(item.getLanguage());
            ivSpinnerLayout.setImageResource(item.getImage());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);

        }
        LangClass item= (LangClass) getItem(position);

        TextView dropDownTV=convertView.findViewById(R.id.tvDropDownLayout);
        dropDownTV.setTypeface(trebu);
        ImageView ivDropDownLayout=convertView.findViewById(R.id.ivDropDownLayout);
        if (item!=null) {

            dropDownTV.setText(item.getLanguage());
            ivDropDownLayout.setImageResource(item.getImage());
        }
        return convertView;
    }
}
