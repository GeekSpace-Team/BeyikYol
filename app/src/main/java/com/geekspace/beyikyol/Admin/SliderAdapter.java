package com.geekspace.beyikyol.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.geekspace.beyikyol.R;


public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context) {
        this.context = context;

    }

    int images [] = {

    };

    int headings [] = {
            R.string.first_header,
            R.string.second_header,
            R.string.thrid_header
    };
    int description [] = {
      R.string.first_desq,
      R.string.second_desq,
      R.string.thrid_desq
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);
        container.addView(view);
        return view;

    }

}


