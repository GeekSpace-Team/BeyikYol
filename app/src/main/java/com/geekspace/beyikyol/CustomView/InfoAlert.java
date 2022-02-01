package com.geekspace.beyikyol.CustomView;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekspace.beyikyol.R;

public class InfoAlert {
    Dialog dialog;
    Context context;
    String yes,t1,t2;

    public InfoAlert(Context context, String yes, String t1, String t2) {
        this.context = context;
        this.yes = yes;
        this.t1 = t1;
        this.t2 = t2;
    }

    public void ShowDialog() {
        Button back;
        TextView tv1,tv2;
        ImageView status;
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");


        dialog=new Dialog(context);
        dialog.setContentView(R.layout.info_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations=R.style.DialogAnimation;

        back=dialog.findViewById(R.id.back);
        tv1=dialog.findViewById(R.id.t1);
        tv2=dialog.findViewById(R.id.t2);
        status=dialog.findViewById(R.id.status);
        tv2.setTypeface(Ping_medium);

        tv1.setText(t1);
        tv2.setText(t2);
        if(yes.equals("yes")){
            status.setImageResource(R.drawable.smile);
            tv1.setTextColor(Color.GREEN);
            back.setBackgroundResource(R.drawable.green);

        } else if(yes.equals("no")){
            status.setImageResource(R.drawable.sad_emoji);
            tv1.setTextColor(Color.RED);
            back.setBackgroundResource(R.drawable.r_s);
        }

        if(yes.equals("info")){
            status.setImageResource(R.drawable.about_icon);
            tv1.setTextColor(Color.GREEN);
            back.setBackgroundResource(R.drawable.green);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
}
