package com.geekspace.beyikyol.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Model.SearchItem;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<SearchItem> arrayList=new ArrayList<>();

    public ListAdapter(Context context, ArrayList<SearchItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.listitems,parent,false);
        TextView textView = view.findViewById(R.id.txt);
        textView.setText(arrayList.get(position).getName());
        return view;
    }
}
