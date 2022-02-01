package com.geekspace.beyikyol.Activity.Car;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.skyfishjy.library.RippleBackground;

public class ReciveCar extends AppCompatActivity {
    Animation rotate;
    Context context=this;
    ImageView anchor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive_car);

        rotate=AnimationUtils.loadAnimation(context, R.anim.rotate);


        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}