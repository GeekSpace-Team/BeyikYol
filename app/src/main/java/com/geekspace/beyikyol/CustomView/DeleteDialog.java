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
import android.widget.Toast;

import com.geekspace.beyikyol.Activity.Car.My_Costs;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CostsDB;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.RemontDB;

public class DeleteDialog {
    Dialog dialog;
    int image;
    String t1;
    String t2;
    Context context;
    String ok;
    int error_image;
    int st=0;
    int which;
    String delete_id;
    String costId;
    String carId;
    String type;

    public DeleteDialog(int image, String t1, String t2, Context context, String ok, int error_image,int which,String delete_id,String costId,String carId,String type) {
        this.image = image;
        this.t1 = t1;
        this.t2 = t2;
        this.context = context;
        this.ok = ok;
        this.error_image = error_image;
        this.which=which;
        this.delete_id=delete_id;
        this.costId=costId;
        this.carId=carId;
        this.type=type;
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
        if(ok.equals("yes")){
            status.setImageResource(image);
            tv1.setTextColor(Color.GREEN);


        } else if(ok.equals("no")){
            status.setImageResource(error_image);
            tv1.setTextColor(Color.RED);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(which==1){
                    CostsDB db=new CostsDB(context);
                    int s=db.deleteData(costId);

                    if(type.equals("1")){
                        BenzinDB benzinDB=new BenzinDB(context);
                        s=benzinDB.deleteData(delete_id);
                    }

                    if(type.equals("2")){
                        CalyshmakDB benzinDB=new CalyshmakDB(context);
                        s=benzinDB.deleteData(delete_id);
                    }

                    if(type.equals("3")){
                        RemontDB benzinDB=new RemontDB(context);
                        s=benzinDB.deleteData(delete_id);
                    }

                    if(s==1){
                        Toast.makeText(context, context.getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                        My_Costs my_garage=new My_Costs();
                        my_garage.doldur(context,carId,"");
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
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
}
