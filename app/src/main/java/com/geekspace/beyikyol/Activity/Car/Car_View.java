package com.geekspace.beyikyol.Activity.Car;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.BuildConfig;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Utils.Utils;
import com.geekspace.beyikyol.Model.Year;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Car_View extends AppCompatActivity {
    ArrayList<Year> type_arr = new ArrayList<>();
    ArrayList<Year> transmission_arr = new ArrayList<>();
    ArrayList<Year> engine_type_arr = new ArrayList<>();
    ImageView imageView;
    int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    String languages = "";
    Context context = this;
    String newLang = "";
    File image;
    String path = "";
    ScrollView scroll;
    TextView marka, model, type, engine_type, engine_power, year, transmission, miles, vin, phone_number, car_name;
    TextView marka_vl, model_vl, type_vl, engine_type_vl, engine_power_vl, year_vl, transmission_vl, miles_vl, vin_vl, phone_number_vl;
    String selectMarka, selectModel, selectYear, selectEnginePower, selectEngineType, selectTransmission, selectMiles, selectImage = "", selectVin, selectPhoneNumber, selectType, select_temp_image, id;
    FloatingActionButton fab;
    LinearLayout remont_btn, update_btn, benzin_btn;

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;
    private boolean clicked = false;
    TextView t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_car__view);
        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Regular.otf");
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");

        rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        initengine_type();
        initTransmission();
        initType();


        scroll = findViewById(R.id.scroll);
        marka = findViewById(R.id.marka);
        model = findViewById(R.id.model);
        type = findViewById(R.id.type);
        engine_type = findViewById(R.id.engine_type);
        engine_power = findViewById(R.id.engine_power);
        year = findViewById(R.id.year);
        transmission = findViewById(R.id.transmission);
        miles = findViewById(R.id.miles);
        vin = findViewById(R.id.vin);
        phone_number = findViewById(R.id.phone_number);
        imageView = findViewById(R.id.car_image);
        car_name = findViewById(R.id.car_name);
        fab = findViewById(R.id.fab);
        benzin_btn = findViewById(R.id.benzin_btn);
        update_btn = findViewById(R.id.update_btn);
        remont_btn = findViewById(R.id.remont_btn);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);

        marka_vl = findViewById(R.id.marka_vl);
        model_vl = findViewById(R.id.model_vl);
        type_vl = findViewById(R.id.type_vl);
        engine_type_vl = findViewById(R.id.engine_type_vl);
        engine_power_vl = findViewById(R.id.engine_power_vl);
        year_vl = findViewById(R.id.year_vl);
        transmission_vl = findViewById(R.id.transmission_vl);
        miles_vl = findViewById(R.id.miles_vl);
        vin_vl = findViewById(R.id.vin_vl);
        phone_number_vl = findViewById(R.id.phone_number_vl);

        t1.setTypeface(Ping_medium);
        t2.setTypeface(Ping_medium);
        t3.setTypeface(Ping_medium);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisiblity(clicked);
                setAnimation(clicked);
                clicked = !clicked;
            }
        });


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        CarDB carDB = new CarDB(this);
        Cursor cursor = carDB.getSelect(id);
        if (cursor.getCount() == 0) {
            finish();
            return;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(0).equals(id)) {
                    selectMarka = cursor.getString(1);
                    selectModel = cursor.getString(2);
                    selectYear = cursor.getString(7);
                    selectEnginePower = cursor.getString(3);
                    selectEngineType = cursor.getString(4);
                    selectTransmission = cursor.getString(6);
                    selectMiles = cursor.getString(8);
                    selectImage = "";
                    selectVin = cursor.getString(9);
                    selectPhoneNumber = cursor.getString(10);
                    selectType = cursor.getString(5);
                }
            }
        }
        marka_vl.setText(selectMarka);
        model_vl.setText(selectModel);
        type_vl.setText(type_arr.get(Integer.parseInt(selectType)).getValue());
        engine_type_vl.setText(engine_type_arr.get(Integer.parseInt(selectEngineType)).getValue());
        engine_power_vl.setText(selectEnginePower);
        transmission_vl.setText(transmission_arr.get(Integer.parseInt(selectTransmission)).getValue());
        year_vl.setText(selectYear);
        phone_number_vl.setText(selectPhoneNumber);
        miles_vl.setText(selectMiles);
        vin_vl.setText(selectVin);

        marka.setTypeface(Ping_regular);
        model.setTypeface(Ping_regular);
        type.setTypeface(Ping_regular);
        engine_type.setTypeface(Ping_regular);
        engine_power.setTypeface(Ping_regular);
        transmission.setTypeface(Ping_regular);
        year.setTypeface(Ping_regular);
        phone_number.setTypeface(Ping_regular);
        miles.setTypeface(Ping_regular);
        vin.setTypeface(Ping_regular);


        marka_vl.setTypeface(Ping_medium);
        model_vl.setTypeface(Ping_medium);
        type_vl.setTypeface(Ping_medium);
        engine_type_vl.setTypeface(Ping_medium);
        engine_power_vl.setTypeface(Ping_medium);
        transmission_vl.setTypeface(Ping_medium);
        year_vl.setTypeface(Ping_medium);
        phone_number_vl.setTypeface(Ping_medium);
        miles_vl.setTypeface(Ping_medium);
        vin_vl.setTypeface(Ping_medium);


        CarDB db = new CarDB(this);
        byte[] data = carDB.getImage(id);
        if (data != null) {
            Bitmap bitmap = Utils.getImage(data);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.unnamed);
        }

        //Typeface trebu = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        car_name.setText(selectMarka);
        car_name.setTypeface(Ping_medium);
        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Car_View.this, Sazlamalar.class));
                // Animatoo.animateSwipeLeft(Car_View.this);
            }
        });

        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Car_View.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Car_View.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            11);
                } else {
                    gotoShare();
                }

                //  shareIntent();
            }
        });


        //   Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Toast.makeText(context, ""+uri.toString(), Toast.LENGTH_SHORT).show();
        // shareIntent();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoShare();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void gotoShare() {
        Bitmap bitmap = getBitmapFromView(scroll, scroll.getChildAt(0).getHeight(), scroll.getChildAt(0).getWidth());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/TRAFFIC_JAM");

        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = selectMarka + "_" + selectModel + "_" + selectVin + ".jpg";
        File file = new File(myDir, fname);
        Log.i("IMAGE", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            Intent sendIntent;
            sendIntent = new Intent();
            // uri =
            Uri imageUri = Uri.fromFile(file);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            }
//                    sendBroadcast(new Intent(
//                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            //getImageContentUri(context,new File(root + "/req_images/"+fname));
            if (!imageUri.toString().isEmpty()) {


                Log.d("URI", imageUri.toString());

                sendIntent.setAction(Intent.ACTION_SEND);
                //   sendIntent.putExtra(Intent.EXTRA_TEXT, cars3.getName()+" / "+cars3.getModel());
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                sendIntent.setType("image/jpg");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoRemont(View view) {
        if (clicked) {

            Intent remont_intent = new Intent(Car_View.this, Remont.class);
            remont_intent.putExtra("id", id);
            remont_intent.putExtra("type", "1");
            startActivity(remont_intent);
            //  Animatoo.animateSwipeLeft(Car_View.this);
            finish();
        }
    }

    public void gotoUpdate(View view) {
        if (clicked) {
            Intent calyshamak_intent = new Intent(Car_View.this, Calyshmak.class);
            calyshamak_intent.putExtra("id", id);
            calyshamak_intent.putExtra("type", "1");
            startActivity(calyshamak_intent);
            //  Animatoo.animateSwipeLeft(Car_View.this);
            finish();
        }
    }

    public void gotoBenzin(View view) {
        if (clicked) {
            Intent benzin_intent = new Intent(Car_View.this, Benzin.class);
            benzin_intent.putExtra("id", id);
            benzin_intent.putExtra("type", "1");
            startActivity(benzin_intent);
            //  Animatoo.animateSwipeLeft(Car_View.this);
            finish();
        }
    }


    private void setVisiblity(boolean clicked) {
        if (!clicked) {
            update_btn.setVisibility(View.VISIBLE);
            benzin_btn.setVisibility(View.VISIBLE);
            remont_btn.setVisibility(View.VISIBLE);
        } else {
            update_btn.setVisibility(View.GONE);
            benzin_btn.setVisibility(View.GONE);
            remont_btn.setVisibility(View.GONE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            update_btn.startAnimation(fromBottom);
            benzin_btn.startAnimation(fromBottom);
            remont_btn.startAnimation(fromBottom);
            fab.startAnimation(rotateOpen);
        } else {
            update_btn.startAnimation(toBottom);
            benzin_btn.startAnimation(toBottom);
            remont_btn.startAnimation(toBottom);
            fab.startAnimation(rotateClose);
        }
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    Uri picCollection = MediaStore.Images.Media
                            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    ContentValues picDetail = new ContentValues();
                    picDetail.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
                    picDetail.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    picDetail.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/" + UUID.randomUUID().toString());
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 1);
                    Uri finaluri = resolver.insert(picCollection, picDetail);
                    picDetail.clear();
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 0);
                    resolver.update(picCollection, picDetail, null, null);
                    return finaluri;
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

            } else {
                return null;
            }
        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    //create bitmap from the ScrollView
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }


    public void shareIntent() {
        Intent sendIntent;
        Uri imageUri;
        Bitmap bitmap = getBitmapFromView(scroll, scroll.getChildAt(0).getHeight(), scroll.getChildAt(0).getWidth());
        CarDB carDB = new CarDB(context);
//        byte[] data=carDB.getImage(id);
//        if(data != null){
//            bitmap  = Utils.getImage(data);
//        } else{
//            bitmap= BitmapFactory.decodeResource(context.getResources(),
//                    R.drawable.unnamed);
//        }
        imageUri = getImageUri(context, bitmap);
        sendIntent = new Intent();
        if (!imageUri.toString().isEmpty()) {


            sendIntent.setAction(Intent.ACTION_SEND);
            //   sendIntent.putExtra(Intent.EXTRA_TEXT, cars3.getName()+" / "+cars3.getModel());
            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            sendIntent.setType("image/*");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            context.startActivity(shareIntent);
            //context.startActivity(sendIntent);
        } else {
            Dexter.withActivity((Activity) context)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {


                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
        }

    }


    private Uri getImageUri(final Context inContext, final Bitmap inImage) {
        path = "";
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + selectMarka+"_"+selectModel+"_"+selectVin+".jpg"); //here's the path where i'm saving my file
//
//                        if (file.exists()){
//
//                            file.delete(); // here i'm checking if file exists and if yes then i'm deleting it but its not working
//                            Toast.makeText(inContext, "", Toast.LENGTH_SHORT).show();
//                        }
                        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, selectMarka + "_" + selectModel + "_" + selectVin, null);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        //  Toast.makeText(context, "Siz bu soragnamany tassyklamaly", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "title", null);
        return Uri.parse(path);
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
        //
        //  Animatoo.animateSwipeRight(Car_View.this);
    }


    private void initengine_type() {
        engine_type_arr.clear();
        //  engine_type.add(new Year(selectEngineType,R.drawable.c));
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(getAssets().open("engine_types_" + languages + ".txt")));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                engine_type_arr.add(new Year(mLine, R.drawable.about_icon));
            }

        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

        }
    }

    private void initType() {
        type_arr.clear();
        //  type.add(new Year(selectType,R.drawable.f));
        if (languages.equals("en")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_en));
            for (int i = 0; i < temp.size(); i++) {
                type_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if (languages.equals("tm")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_tm));
            for (int i = 0; i < temp.size(); i++) {
                type_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if (languages.equals("ru")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.types_ru));
            for (int i = 0; i < temp.size(); i++) {
                type_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }

    }

    private void initTransmission() {
        transmission_arr.clear();
        //  transmission.add(new Year(selectTransmission,R.drawable.g));
        if (languages.equals("en")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_en));
            for (int i = 0; i < temp.size(); i++) {
                transmission_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if (languages.equals("tm")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_tm));
            for (int i = 0; i < temp.size(); i++) {
                transmission_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
        if (languages.equals("ru")) {
            List<String> temp = Arrays.asList(getResources().getStringArray(R.array.transmission_ru));
            for (int i = 0; i < temp.size(); i++) {
                transmission_arr.add(new Year(temp.get(i), R.drawable.about_icon));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");
        if (!newLang.equals(languages)) {
            recreate();
        }
    }
}