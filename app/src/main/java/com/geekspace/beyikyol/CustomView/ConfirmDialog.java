package com.geekspace.beyikyol.CustomView;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geekspace.beyikyol.Activity.Car.My_Garage;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;

public class ConfirmDialog {
    Dialog dialog;
    int image;
    String t1;
    String t2;
    Context context;
    String ok;
    int error_image;
    int st=0;
    int which;
    int delete_id;

    public ConfirmDialog(int image, String t1, String t2, Context context, String ok, int error_image,int which,int delete_id) {
        this.image = image;
        this.t1 = t1;
        this.t2 = t2;
        this.context = context;
        this.ok = ok;
        this.error_image = error_image;
        this.which=which;
        this.delete_id=delete_id;
    }

    public void showDialog(){
        Button back,cancel;
        TextView tv1,tv2;
        ImageView status;
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.remove_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations=R.style.DialogAnimation;
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        back=dialog.findViewById(R.id.back);
        tv1=dialog.findViewById(R.id.t1);
        tv2=dialog.findViewById(R.id.t2);
        status=dialog.findViewById(R.id.status);
        cancel=dialog.findViewById(R.id.cancel);


        tv1.setTypeface(Ping_medium);
        tv2.setTypeface(Ping_medium);
        cancel.setTypeface(Ping_medium);
        back.setTypeface(Ping_medium);
        tv1.setText(t1);
        tv2.setText(t2);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(which==1){
                    CarDB db=new CarDB(context);
                    int s=db.deleteData(String.valueOf(delete_id));
                    if(s==1){
                        Toast.makeText(context, context.getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                        My_Garage my_garage=new My_Garage();
                        my_garage.select(context);
                    } else if(s==0){
                        Toast.makeText(context, context.getResources().getString(R.string.not_deleted),Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
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
