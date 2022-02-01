package com.geekspace.beyikyol.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.geekspace.beyikyol.Activity.Car.Car_View;
import com.geekspace.beyikyol.Activity.Car.Cars;
import com.geekspace.beyikyol.Activity.Car.UpdateCar;
import com.geekspace.beyikyol.Database.CarDB;
import com.geekspace.beyikyol.CustomView.ConfirmDialog;
import com.geekspace.beyikyol.CustomView.CustomTypeFaceSpan;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Cars_Adapter extends RecyclerView.Adapter<Cars_Adapter.ViewHolder> {
    ArrayList<Cars> carsArrayList=new ArrayList<>();
    Context context;
    String path="";
    long DURATION = 500;
    private boolean on_attach = true;
    private int lastPosition = -1;



    public Cars_Adapter(ArrayList<Cars> carsArrayList, Context context) {
        this.carsArrayList = carsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_design, parent, false);
        return new Cars_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
           final Cars cars=carsArrayList.get(position);
          // setAnimation(holder.itemView, position);

        holder.image.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));

       // setAnimation(holder.itemView, position);
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");


           holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

           holder.marka.setText(cars.getName());
           holder.model.setText(cars.getModel());
           holder.km.setText(cars.getKm()+" km");
           holder.marka.setTypeface(Ping_medium);
           holder.model.setTypeface(Ping_medium);
           holder.km.setTypeface(Ping_medium);

           CarDB carDB=new CarDB(context);
            byte[] data=carDB.getImage(cars.getId()+"");
            if(data != null){
                Bitmap bitmap = Utils.getImage(data);
                holder.image.setImageBitmap(bitmap);
            } else{
                holder.image.setImageResource(R.drawable.unnamed);
            }

          holder.cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent=new Intent(context, Car_View.class);
                  intent.putExtra("id",cars.getId()+"");
                  intent.putExtra("uri",cars.getImage());
                  context.startActivity(intent);
                 // Animatoo.animateSwipeLeft(context);
              }
          });



        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,position,cars.getId()+"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return carsArrayList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView marka;
        TextView model;
        ImageView image;
        ImageButton more;
        CardView cardView;
        TextView km;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            marka=itemView.findViewById(R.id.marka);
            model=itemView.findViewById(R.id.model);
            image=itemView.findViewById(R.id.car_image);
            more=itemView.findViewById(R.id.more);
            cardView=itemView.findViewById(R.id.card);
            km=itemView.findViewById(R.id.km);





        }
    }

    private Uri getImageUri(final Context inContext, final Bitmap inImage) {
        path="";
        Dexter.withActivity((Activity) context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "title", null);
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

    private void showPopup(View view, final int pos, final String id){
        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
        popupMenu.inflate(R.menu.popup_menu);
        setForceShowIcon(popupMenu);

        Menu menu = popupMenu.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            applyFontToMenuItem(mi);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.update:
                        Cars cars1=carsArrayList.get(pos);
                        ((Activity) context).finish();
                        Intent intent=new Intent(context, UpdateCar.class);
                        intent.putExtra("id",cars1.getId()+"");
                        context.startActivity(intent);
                      //  Animatoo.animateSwipeLeft(context);

                        return true;
                    case R.id.delete:
                        Cars cars2=carsArrayList.get(pos);
                        ConfirmDialog confirmDialog=new ConfirmDialog(R.drawable.question_new,context.getResources().getString(R.string.attention),context.getResources().getString(R.string.sorag),context,"yes",R.drawable.question_new,1,cars2.getId());
                        confirmDialog.showDialog();
                        return true;
//                    case R.id.share:
//                        Cars cars3=carsArrayList.get(pos);
////                        Intent sendIntent = new Intent();
////                        sendIntent.setAction(Intent.ACTION_SEND);
////                        sendIntent.putExtra(Intent.EXTRA_TEXT, cars3.getName()+" / "+cars3.getModel());
////                        sendIntent.setType("text/plain");
//
//                        Intent sendIntent;
//                         Uri imageUri;
//                        Bitmap bitmap=null;
//                        CarDB carDB=new CarDB(context);
//                        byte[] data=carDB.getImage(id);
//                        if(data != null){
//                          bitmap  = Utils.getImage(data);
//                        } else{
//                           bitmap= BitmapFactory.decodeResource(context.getResources(),
//                                    R.drawable.unnamed);
//                        }
//                        imageUri=getImageUri(context,bitmap);
//                        sendIntent = new Intent();
//                        if(!imageUri.toString().isEmpty()) {
//
//
//
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, cars3.getName()+" / "+cars3.getModel());
//                            sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                            sendIntent.setType("image/*");
//                            Intent shareIntent = Intent.createChooser(sendIntent, null);
//                            context.startActivity(shareIntent);
//                            //context.startActivity(sendIntent);
//                        } else{
//                            Dexter.withActivity((Activity) context)
//                                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                    .withListener(new PermissionListener() {
//                                        @Override
//                                        public void onPermissionGranted(PermissionGrantedResponse response) {
//
//                                        }
//
//                                        @Override
//                                        public void onPermissionDenied(PermissionDeniedResponse response) {
//
//
//                                        }
//
//                                        @Override
//                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                                        }
//                                    }).check();
//                        }


                      //  return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, context.getResources().getColor(R.color.third)), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected final int getPosition(@NonNull final ViewHolder holder) {
        return holder.getAdapterPosition();
    }



    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
