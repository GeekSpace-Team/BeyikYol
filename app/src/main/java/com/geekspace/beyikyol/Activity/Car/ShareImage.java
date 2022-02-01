package com.geekspace.beyikyol.Activity.Car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;

import java.io.UnsupportedEncodingException;

public class ShareImage extends AppCompatActivity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        image=findViewById(R.id.image);
        Bitmap bm = convertStringToBitmap(decodeBase64("Hello"));

        Bitmap bg = BitmapFactory.decodeResource(getResources(),R.drawable.about_icon);

        Bitmap combined = combineImages(bg,bm);
        image.setImageBitmap(combined);


    }

    public static Bitmap convertStringToBitmap(String string) {


        byte[] arr = Base64.decode(string, Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return b;
    }

    private String decodeBase64(String coded){
        byte[] valueDecoded= new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(valueDecoded);
    }

    public Bitmap combineImages(Bitmap background, Bitmap foreground) {

        int width = 0, height = 0;
        Bitmap cs;

        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        comboImage.drawBitmap(background, 0, 0, null);
        Matrix matrix = new Matrix();
        matrix.setScale(200,200);
        comboImage.drawBitmap(foreground, matrix, null);

        return cs;
    }
}