package com.geekspace.beyikyol.Activity.App;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Activity.Car.Ewakuator;
import com.geekspace.beyikyol.Receiver.ExampleJobService;
import com.geekspace.beyikyol.Receiver.ExampleService;
import com.geekspace.beyikyol.Activity.Map.MapActivity;
import com.geekspace.beyikyol.Model.MyCars;
import com.geekspace.beyikyol.Adapter.MyCarsAdapter;
import com.geekspace.beyikyol.Activity.Car.My_Costs;
import com.geekspace.beyikyol.Activity.Car.My_Garage;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.RemontDB;
import com.geekspace.beyikyol.Common.ServerAddress;
import com.geekspace.beyikyol.Receiver.ServiceNoDelay;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;


public class MainMenu extends AppCompatActivity {
    private static final String TAG = "MainMenu";
    ArrayList<MyCars> cars1 = new ArrayList<>();
    TextView btn_1_title, btn_2_title, btn_3_title, btn_4_title, btn_5_title, btn_6_title;
    CardView main_first, main_second, main_third, main_fourth, main_fifth, main_sixth;
    CarDB carDB;
    RecyclerView ulaglarym;
    ImageButton sett;
    int a = 0, b = 0, c = 0;
    LayoutInflater inflater;
    LinearLayout linear;
    int position = 0;
    Animation animation1, animation2, fade, animation3, animation4, slide_left;
    // ImageView first,second,third,fourth;
    // TextView yol_title,desc,t1;
    private static final int REQUEST_CODE = 1;
    String languages = "";
    String newLang = "";
    boolean isFirst = false;
    boolean birinji = false;
    RequestQueue requestQueue;
    CardView more, remove;
    Location weatherLocation;

    private static MainMenu INSTANCE;

    ImageButton more_btn, remove_btn;

    TextView weather_degree, weather_status;

    FusedLocationProviderClient fusedLocationProviderClient;


    String weatherUrl = "";
    String appId = "a457b53064f343ad81e2fc9444e1b8f9";
    String weather_lang = "en";

    ImageView weather;
    SwipeRefreshLayout SwipeRefreshLayout;

    boolean isScrollAdded=false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        loadLocal();
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);


        SwipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);

        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (requestQueue != null) {
                    requestQueue.cancelAll(TAG);
                }
                creater();
            }
        });

        creater();

    }

    private void creater() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Typeface Ping_medium = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(getAssets(), "fonts/Ping LCG Bold.otf");

        ServiceNoDelay mSensorService = new ServiceNoDelay(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        languages = share.getString("My_Lang", "");
        newLang = share.getString("My_Lang", "");


        more = findViewById(R.id.more);
        remove = findViewById(R.id.remove);
        more_btn = findViewById(R.id.more_btn);
        remove_btn = findViewById(R.id.remove_btn);
        weather_status = findViewById(R.id.weather_status);
        weather_degree = findViewById(R.id.weather_degree);
        weather = findViewById(R.id.weather);


        weather_degree.setTypeface(Ping_bold);
        weather_status.setTypeface(Ping_medium);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        ulaglarym = findViewById(R.id.ulaglarym);
        sett = findViewById(R.id.sett);
        linear = findViewById(R.id.linear);

        inflater = LayoutInflater.from(this);

        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
        animation4 = AnimationUtils.loadAnimation(this, R.anim.anim4);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        slide_left = AnimationUtils.loadAnimation(this, R.anim.slide_left);


        sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(MainMenu.this, Sazlamalar.class), REQUEST_CODE);

            }
        });
        BenzinDB benzinDB = new BenzinDB(this);
        CalyshmakDB calyshmakDB = new CalyshmakDB(this);
        RemontDB remontDB = new RemontDB(this);

        carDB = new CarDB(this);
        Cursor cursor = carDB.getAll();
        cars1.clear();
        if (cursor.getCount() == 0) {

        } else {

            while (cursor.moveToNext()) {
                cars1.add(new MyCars(cursor.getString(0), cursor.getString(1) + " / " + cursor.getString(2), "", cursor.getString(8)));
            }
        }

        cars1.add(new MyCars("nothing", "", "", ""));
        ulaglarym.setLayoutManager(layoutManager1);
        MyCarsAdapter adapter = new MyCarsAdapter(cars1, this);
        ulaglarym.setAdapter(adapter);


        Typeface robot = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf");
        btn_1_title = findViewById(R.id.btn_1_title);
        btn_2_title = findViewById(R.id.btn_2_title);
        btn_3_title = findViewById(R.id.btn_3_title);
        btn_4_title = findViewById(R.id.btn_4_title);
        btn_5_title = findViewById(R.id.btn_5_title);
        btn_6_title = findViewById(R.id.btn_6_title);


        main_first = findViewById(R.id.main_first);
        main_second = findViewById(R.id.main_second);
        main_third = findViewById(R.id.main_third);
        main_fourth = findViewById(R.id.main_fourth);


        main_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainMenu.this, MapActivity.class));
            }
        });

        main_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainMenu.this, Ewakuator.class), 2);
            }
        });


        main_fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this, My_Costs.class);
                intent.putExtra("id", "nothing");
                startActivityForResult(intent, 3);
                isFirst = true;
            }
        });

        main_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainMenu.this, My_Garage.class), 5);
                isFirst = true;
            }
        });


        btn_1_title.setTypeface(Ping_medium);
        btn_2_title.setTypeface(Ping_medium);
        btn_3_title.setTypeface(Ping_medium);
        btn_4_title.setTypeface(Ping_medium);
        btn_5_title.setTypeface(Ping_medium);
        btn_6_title.setTypeface(Ping_medium);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isScrollAdded=true;
            ulaglarym.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                            .findFirstVisibleItemPosition() == cars1.size() - 1) {
                        more.setVisibility(View.GONE);
                    } else {
                        more.setVisibility(View.VISIBLE);
                    }


                    if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                            .findFirstVisibleItemPosition() == 0) {
                        remove.setVisibility(View.GONE);
                    } else {
                        remove.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else{
            isScrollAdded=false;
        }

        ulaglarym.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == SCROLL_STATE_DRAGGING)
                    SwipeRefreshLayout.setEnabled(false);

                if(newState == SCROLL_STATE_IDLE)
                    SwipeRefreshLayout.setEnabled(true);


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isScrollAdded){
                    if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                            .findFirstVisibleItemPosition() == cars1.size() - 1) {
                        more.setVisibility(View.GONE);
                    } else {
                        more.setVisibility(View.VISIBLE);
                    }


                    if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                            .findFirstVisibleItemPosition() == 0) {
                        remove.setVisibility(View.GONE);
                    } else {
                        remove.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulaglarym.smoothScrollToPosition(((LinearLayoutManager) ulaglarym.getLayoutManager()).findFirstVisibleItemPosition() - 1);
            }
        });

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulaglarym.smoothScrollToPosition(((LinearLayoutManager) ulaglarym.getLayoutManager()).findFirstVisibleItemPosition() + 1);
            }
        });
        SwipeRefreshLayout.setRefreshing(false);

        getTemp();

    }


    public static MainMenu get() {
        return INSTANCE;
    }

    public CardView getMore() {
        return more;
    }

    public void gotoGeekSpaceSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(ServerAddress.GEEKSPACE_SITE_URL));
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void comingSoon(View v) {
        RelativeLayout parent = findViewById(R.id.parent);
        Snackbar.make(parent, getResources().getString(R.string.come), Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.first))
                .show();
    }

    private void getTemp() {
        weather.setImageResource(R.drawable.weather);
        weather_degree.setText("0");
        weather_status.setText(getResources().getString(R.string.baglan_ar));
        weather_status.setTextColor(getResources().getColor(R.color.third));

        Location location = new Location("");
        location.setLatitude(37.862499);
        location.setLongitude(58.238056);

       // Log.d("Lat", location.getLatitude() + "," + location.getLongitude());
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        weather_lang = share.getString("My_Lang", "");
        if (weather_lang.equals("tm"))
            weather_lang = "en";
        weatherUrl = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&key=" + appId + "&lang=" + weather_lang;
        requestQueue = Volley.newRequestQueue(this);


        // Request a string response from the provided URL.
        final Location finalLocation = location;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("data");

                            JSONObject obj2 = arr.getJSONObject(0);
                            JSONObject weatherArray = obj2.getJSONObject("weather");

                            weather_degree.setText(obj2.getString("temp"));
                            weather_status.setText(weatherArray.getString("description"));

                            loadImageFromAssets("weather_icons/" + weatherArray.getString("icon") + ".png");
                            // load image


                            Log.d("WEATHER", weatherArray.getString("description"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("WEATHER", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weather.setImageResource(R.drawable.error_w);
                weather_degree.setText("0");
                weather_status.setText(getResources().getString(R.string.error_weather));
                weather_status.setTextColor(Color.RED);
            }
        });
        stringRequest.setTag(TAG);

        requestQueue.add(stringRequest);


    }

    public void loadImageFromAssets(String path) {
        try {
            // get input stream
            InputStream ims = getAssets().open(path);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            weather.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }
    }

    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //saved data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocal() {
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String languages = share.getString("My_Lang", "");
        setLocale(languages);
    }


    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("SplashScreen", "Job Scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job Canceled");
    }


    public void startService() {
        String string = "OK";
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", string);
        startService(serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLocal();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        newLang = share.getString("My_Lang", "");

        if (!newLang.equals(languages)) {
            recreate();
        }

        if (isFirst) {
            recreate();
            isFirst = false;
        }

        if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                .findFirstVisibleItemPosition() == cars1.size() - 1) {
            more.setVisibility(View.GONE);
        } else {
            more.setVisibility(View.VISIBLE);
        }


        if (((LinearLayoutManager) ulaglarym.getLayoutManager())
                .findFirstVisibleItemPosition() == 0) {
            remove.setVisibility(View.GONE);
        } else {
            remove.setVisibility(View.VISIBLE);
        }
        //
    }


    public class Message {
        public void displayMessage(int resultCode, Bundle resultData) {
            String message = resultData.getString("message");
            Toast.makeText(MainMenu.this, resultCode + " " + message, Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        isFirst = true;
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }




}
