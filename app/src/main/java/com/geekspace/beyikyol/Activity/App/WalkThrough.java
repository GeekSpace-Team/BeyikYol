package com.geekspace.beyikyol.Activity.App;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cuberto.liquid_swipe.LiquidPager;
import com.geekspace.beyikyol.CustomView.ViewPager;
import com.geekspace.beyikyol.R;

public class WalkThrough extends AppCompatActivity {
     LiquidPager pager;
     ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_walk_through);
        pager=findViewById(R.id.pager);
        viewPager=new ViewPager(getSupportFragmentManager(),1);
        pager.setAdapter(viewPager);
    }
}