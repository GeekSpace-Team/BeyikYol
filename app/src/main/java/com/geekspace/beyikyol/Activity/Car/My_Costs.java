package com.geekspace.beyikyol.Activity.Car;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekspace.beyikyol.Activity.App.Sazlamalar;
import com.geekspace.beyikyol.Database.BenzinDB;
import com.geekspace.beyikyol.Database.CalyshmakDB;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.Adapter.CostAdapter;
import com.geekspace.beyikyol.CustomView.CostSpinner;
import com.geekspace.beyikyol.Database.CostsDB;
import com.geekspace.beyikyol.Model.CostsKlass;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.RemontDB;
import com.geekspace.beyikyol.Model.SmallCar;
import com.geekspace.beyikyol.Adapter.SmallCarAdapter;
import com.geekspace.beyikyol.CustomView.SwipeHelper;
import com.geekspace.beyikyol.Model.Year;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class My_Costs extends AppCompatActivity {
    RecyclerView ulaglar;
    CarDB carDB;
    ArrayList<SmallCar> smallCars = new ArrayList<>();
    ArrayList<Year> filtr1 = new ArrayList<>();
    ArrayList<Year> filtr2 = new ArrayList<>();
    Spinner first_spinner, second_spinner;
    String ulag_id;
    LinearLayout empty;
    String s_id = "";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton carInfo, benzin, calyshamak, remont;
    String languages = "";
    String newLang = "";

    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    LinearLayout remont_btn, update_btn, benzin_btn, carinfo_btn;

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;
    private boolean clicked = false;
    Context context = this;
    TextView t1, t2, t3, t4;
    ImageView img1, img2, img3, img4;
    TextView empty_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my__costs);
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
        fab = findViewById(R.id.fab);
        benzin_btn = findViewById(R.id.benzin_btn);
        update_btn = findViewById(R.id.update_btn);
        remont_btn = findViewById(R.id.remont_btn);
        carinfo_btn = findViewById(R.id.carinfo_btn);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        filtr1.add(new Year(getResources().getString(R.string.item1), R.drawable.filter_ic));
        filtr1.add(new Year(getResources().getString(R.string.benzin), R.drawable.filter_ic));
        filtr1.add(new Year(getResources().getString(R.string.calyshmyk), R.drawable.filter_ic));
        filtr1.add(new Year(getResources().getString(R.string.remont), R.drawable.filter_ic));

        filtr2.add(new Year(getResources().getString(R.string.item1), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item2), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item3), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item4), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item5), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item6), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item7), R.drawable.sort_ic));
        filtr2.add(new Year(getResources().getString(R.string.item8), R.drawable.sort_ic));


        first_spinner = findViewById(R.id.filtr_1);
        second_spinner = findViewById(R.id.filtr_2);
        empty = findViewById(R.id.empty);
        empty_tv = findViewById(R.id.empty_tv);

        Intent intent = getIntent();
        s_id = intent.getStringExtra("id");

        empty_tv.setTypeface(Ping_medium);


        CostSpinner sp_ad = new CostSpinner(this, filtr1);
        CostSpinner sp_ad2 = new CostSpinner(this, filtr2);
        String name = "";
        first_spinner.setAdapter(sp_ad);
        second_spinner.setAdapter(sp_ad2);
        ulaglar = findViewById(R.id.ulaglar);
        carDB = new CarDB(this);

        // Typeface trebu = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(My_Costs.this, Sazlamalar.class));
                // Animatoo.animateSwipeLeft(My_Costs.this);
            }
        });

        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setTypeface(Ping_bold);

        toolbar_title.setText(getResources().getString(R.string.ykdajylar));


        getSmalls(this);


    }


    public void gotoRemont(View view, Context context, String id) {
        if (clicked) {
            ((Activity) context).finish();
            Intent remont_intent = new Intent(context, Remont.class);
            remont_intent.putExtra("id", id);
            remont_intent.putExtra("type", "2");
            context.startActivity(remont_intent);
            // Animatoo.animateSwipeLeft(context);
        }
    }

    public void gotoUpdate(View view, Context context, String id) {
        if (clicked) {
            ((Activity) context).finish();
            Intent calyshamak_intent = new Intent(context, Calyshmak.class);
            calyshamak_intent.putExtra("id", id);
            calyshamak_intent.putExtra("type", "2");
            context.startActivity(calyshamak_intent);
            //  Animatoo.animateSwipeLeft(context);
        }
    }

    public void gotoBenzin(View view, Context context, String id) {
        if (clicked) {
            ((Activity) context).finish();
            Intent benzin_intent = new Intent(context, Benzin.class);
            benzin_intent.putExtra("id", id);
            benzin_intent.putExtra("type", "2");
            context.startActivity(benzin_intent);
            // Animatoo.animateSwipeLeft(context);
        }
    }


    private void setVisiblity(boolean clicked) {
        if (!clicked) {
            update_btn.setVisibility(View.VISIBLE);
            benzin_btn.setVisibility(View.VISIBLE);
            remont_btn.setVisibility(View.VISIBLE);
            carinfo_btn.setVisibility(View.VISIBLE);
        } else {
            update_btn.setVisibility(View.GONE);
            benzin_btn.setVisibility(View.GONE);
            remont_btn.setVisibility(View.GONE);
            carinfo_btn.setVisibility(View.GONE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            update_btn.startAnimation(fromBottom);
            benzin_btn.startAnimation(fromBottom);
            remont_btn.startAnimation(fromBottom);
            carinfo_btn.startAnimation(fromBottom);
            fab.startAnimation(rotateOpen);
        } else {
            update_btn.startAnimation(toBottom);
            benzin_btn.startAnimation(toBottom);
            remont_btn.startAnimation(toBottom);
            carinfo_btn.startAnimation(toBottom);
            fab.startAnimation(rotateClose);
        }
    }


    public void doldur(final Context context, final String car_id, String oldId) {
        ulag_id = car_id;
        RecyclerView linear;
        LinearLayout empty;


        rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);

        fab = ((Activity) context).findViewById(R.id.fab);
        benzin_btn = ((Activity) context).findViewById(R.id.benzin_btn);
        update_btn = ((Activity) context).findViewById(R.id.update_btn);
        remont_btn = ((Activity) context).findViewById(R.id.remont_btn);
        carinfo_btn = ((Activity) context).findViewById(R.id.carinfo_btn);
        t1 = ((Activity) context).findViewById(R.id.t1);
        t2 = ((Activity) context).findViewById(R.id.t2);
        t3 = ((Activity) context).findViewById(R.id.t3);
        t4 = ((Activity) context).findViewById(R.id.t4);

        img1 = ((Activity) context).findViewById(R.id.img1);
        img2 = ((Activity) context).findViewById(R.id.img2);
        img3 = ((Activity) context).findViewById(R.id.img3);
        img4 = ((Activity) context).findViewById(R.id.img4);

        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Regular.otf");

        t1.setTypeface(Ping_medium);
        t2.setTypeface(Ping_medium);
        t3.setTypeface(Ping_medium);
        t4.setTypeface(Ping_medium);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisiblity(clicked);
                setAnimation(clicked);
                clicked = !clicked;
            }
        });


        first_spinner = ((Activity) context).findViewById(R.id.filtr_1);
        second_spinner = ((Activity) context).findViewById(R.id.filtr_2);
        empty = ((Activity) context).findViewById(R.id.empty);

        first_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doldur(context, car_id, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Typeface trebu = Typeface.createFromAsset(context.getAssets(), "fonts/trebuc.ttf");
        TextView toolbar_title = ((Activity) context).findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(Ping_bold);
        String name = "";
        CarDB carDB1 = new CarDB(context);
        Cursor tt = carDB1.getSelect(car_id);
        if (tt.getCount() == 0) {
            return;
        } else {
            while (tt.moveToNext()) {
                name = tt.getString(1);
            }
        }
        toolbar_title.setText(name);


        materialDesignFAM = (FloatingActionMenu) ((Activity) context).findViewById(R.id.material_design_android_floating_action_menu);
        carInfo = (FloatingActionButton) ((Activity) context).findViewById(R.id.material_design_floating_action_menu_item1);
        remont = (FloatingActionButton) ((Activity) context).findViewById(R.id.material_design_floating_action_menu_item2);
        calyshamak = (FloatingActionButton) ((Activity) context).findViewById(R.id.material_design_floating_action_menu_item3);
        benzin = (FloatingActionButton) ((Activity) context).findViewById(R.id.material_design_floating_action_menu_item4);


        benzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBenzin(v, context, car_id);

            }
        });

        calyshamak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUpdate(v, context, car_id);

            }
        });

        remont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRemont(v, context, car_id);

            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRemont(v, context, car_id);
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUpdate(v, context, car_id);
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBenzin(v, context, car_id);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRemont(v, context, car_id);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUpdate(v, context, car_id);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBenzin(v, context, car_id);
            }
        });

        carinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info_intent = new Intent(context, Car_View.class);
                info_intent.putExtra("id", car_id);
                context.startActivity(info_intent);
                // Animatoo.animateSwipeLeft(context);
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info_intent = new Intent(context, Car_View.class);
                info_intent.putExtra("id", car_id);
                context.startActivity(info_intent);
                //Animatoo.animateSwipeLeft(context);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info_intent = new Intent(context, Car_View.class);
                info_intent.putExtra("id", car_id);
                context.startActivity(info_intent);
                // Animatoo.animateSwipeLeft(context);
            }
        });
        second_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doldur(context, car_id, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayList<CostsKlass> my_cost = new ArrayList<>();
        String sql = "SELECT * FROM costs WHERE carid='" + car_id + "' ";
        if (!(first_spinner.getSelectedItemPosition() == 0)) {
            sql += " and item_type='" + String.valueOf(first_spinner.getSelectedItemPosition()) + "'";
        }
        Calendar calendar = Calendar.getInstance();
        if (second_spinner.getSelectedItemPosition() == 1) {
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int yyl = calendar.get(Calendar.YEAR);
            sql += " and hepde='" + week + "' and yyl='" + yyl + "' ";
        }


        if (second_spinner.getSelectedItemPosition() == 2) {
            int ay = calendar.get(Calendar.MONTH) + 1;
            int yyl = calendar.get(Calendar.YEAR);
            sql += " and ay='" + ay + "' and yyl='" + yyl + "' ";
        }
        if (second_spinner.getSelectedItemPosition() == 3) {
            int ay = calendar.get(Calendar.MONTH) + 1;
            int yyl = calendar.get(Calendar.YEAR);
            if (ay == 1) {
                ay = 12;
                yyl -= 1;
            } else {
                ay -= 1;
            }

            sql += " and ay='" + ay + "' and yyl='" + yyl + "' ";
        }
        if (second_spinner.getSelectedItemPosition() == 4) {
            int week = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
            int yyl = calendar.get(Calendar.YEAR);
            sql += " and hepde='" + week + "' and yyl='" + yyl + "' ";
        }
        if (second_spinner.getSelectedItemPosition() == 5) {
            int yyl = calendar.get(Calendar.YEAR);
            sql += " and yyl='" + yyl + "' ";
        }

        if (second_spinner.getSelectedItemPosition() == 6) {
            int yyl = calendar.get(Calendar.YEAR) - 1;
            sql += " and yyl='" + yyl + "' ";
        }

        sql += "  ORDER BY ID DESC";
        CostsDB db;
        BenzinDB benzinDB;
        CalyshmakDB calyshmakDB;
        RemontDB remontDB;
//        Toast.makeText(context,car_id,Toast.LENGTH_SHORT).show();

        linear = ((Activity) context).findViewById(R.id.cars);
        db = new CostsDB(context);
        benzinDB = new BenzinDB(context);
        calyshmakDB = new CalyshmakDB(context);
        remontDB = new RemontDB(context);
        my_cost.clear();
        Cursor res = db.customSelect(sql);

        if (res.getCount() == 0) {
            linear.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            int q = 0, w = 0, e = 0;
            linear.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            while (res.moveToNext()) {
                String id = res.getString(2);
                String type = res.getString(3);
                String sene = res.getString(4) + "." + res.getString(5) + "." + res.getString(6);

                if (type.equals("1")) {
                    Cursor benzin = benzinDB.getSelect(id);
                    if (benzin.getCount() == 0) {
                        continue;
                    } else {

                        while (benzin.moveToNext()) {
                            String km = benzin.getString(3);
                            String litr = benzin.getString(2);
                            if (q == 0) {
                                my_cost.add(new CostsKlass(id, type, sene, km, litr + " litre", car_id, res.getString(0)));
                            }
                            if (second_spinner.getSelectedItemPosition() == 7) {
                                q++;
                            }
                        }

                    }
                }
                if (type.equals("2")) {
                    Cursor calyshmak = calyshmakDB.getSelect(id);
                    if (calyshmak.getCount() == 0) {
                        continue;
                    } else {

                        while (calyshmak.moveToNext()) {
                            String km = calyshmak.getString(8);
                            String calyshylan = calyshmak.getString(2);

                            if (w == 0) {
                                my_cost.add(new CostsKlass(id, type, sene, km, calyshylan, car_id, res.getString(0)));
                            }
                            if (second_spinner.getSelectedItemPosition() == 7) {
                                w++;
                            }
                        }
                    }
                }

                if (type.equals("3")) {
                    Cursor remont = remontDB.getSelect(id);
                    if (remont.getCount() == 0) {
                        continue;
                    } else {

                        while (remont.moveToNext()) {
                            String km = remont.getString(6);
                            String remont_text = remont.getString(2);
                            if (e == 0) {
                                my_cost.add(new CostsKlass(id, type, sene, km, remont_text, car_id, res.getString(0)));
                            }
                            if (second_spinner.getSelectedItemPosition() == 7) {
                                e++;
                            }
                        }
                    }
                }


            }
        }


        final CostAdapter adapter = new CostAdapter(my_cost, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        linear.setLayoutManager(layoutManager);
        linear.setAdapter(adapter);
//        getSmalls(context);
//        ulaglar=((Activity) context).findViewById(R.id.ulaglar);
//
//        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        SmallCarAdapter adapter1=new SmallCarAdapter(context,smallCars,car_id,oldId);
//        ulaglar.setLayoutManager(layoutManager1);
//        ulaglar.setAdapter(adapter1);


        SwipeHelper swipeHelper = new SwipeHelper(context, linear) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "",
                        0,
                        Color.parseColor("#FFFFFF"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));

//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "",
//                        R.drawable.edit,
//                        Color.parseColor("#FF9502"),
//                        new SwipeHelper.UnderlayButtonClickListener() {
//                            @Override
//                            public void onClick(int pos) {
//                                // TODO: OnTransfer
//                            }
//                        }
//                ));
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Unshare",
//                        0,
//                        Color.parseColor("#C7C7CB"),
//                        new SwipeHelper.UnderlayButtonClickListener() {
//                            @Override
//                            public void onClick(int pos) {
//                                // TODO: OnUnshare
//                            }
//                        }
//                ));
            }
        };


//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                Toast.makeText(My_Costs.this, "on Move", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                Toast.makeText(My_Costs.this, "on Swiped "+swipeDir, Toast.LENGTH_SHORT).show();
//                //Remove swiped item from list and notify the RecyclerView
//                int position = viewHolder.getAdapterPosition();
//               // my_cost.remove(position);
//                adapter.notifyDataSetChanged();
//
//            }
//        };
//
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(linear);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Animatoo.animateSwipeRight(My_Costs.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        recreate();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void comingSoon() {
        RelativeLayout parent = findViewById(R.id.parent);
        Snackbar.make(parent, getResources().getString(R.string.ulag_gosh_cykdayjy), Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.goshmak), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(My_Costs.this, Add_Car.class);
                        intent.putExtra("type", "3");
                        startActivityForResult(intent, 12);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.first))
                .show();
    }

    public void getSmalls(Context context) {
        carDB = new CarDB(context);
        Cursor cursor = carDB.getAll();
        empty = ((Activity) context).findViewById(R.id.empty);
        ulaglar = ((Activity) context).findViewById(R.id.ulaglar);
        if (cursor.getCount() == 0) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comingSoon();
                }
            });
            // Toast.makeText(My_Costs.this,getResources().getString(R.string.maglumatlar_bazasy_bo),Toast.LENGTH_SHORT).show();
            //finish();
            empty.setVisibility(View.VISIBLE);
            ulaglar.setVisibility(View.GONE);


            return;
        }
        empty.setVisibility(View.GONE);
        ulaglar.setVisibility(View.VISIBLE);

        smallCars.clear();
        while (cursor.moveToNext()) {
            smallCars.add(new SmallCar(cursor.getString(0) + "", cursor.getString(1) + " / " + cursor.getString(2)));
        }

        SmallCar car = smallCars.get(0);
        if (s_id.equals("nothing")) {
            doldur(context, car.getCarId(), "");
        } else {
            doldur(context, s_id, "");
        }

        // Toast.makeText(context, ""+s_id, Toast.LENGTH_SHORT).show();
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        SmallCarAdapter adapter1 = new SmallCarAdapter(context, smallCars, "", s_id);
        ulaglar.setLayoutManager(layoutManager1);
        ulaglar.setAdapter(adapter1);

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