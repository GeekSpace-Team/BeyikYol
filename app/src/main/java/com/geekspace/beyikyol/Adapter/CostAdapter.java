package com.geekspace.beyikyol.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.geekspace.beyikyol.Activity.Car.BenzinUpdate;
import com.geekspace.beyikyol.Activity.Car.CalyshmakUpdate;
import com.geekspace.beyikyol.Activity.Car.RemontUpdate;
import com.geekspace.beyikyol.Model.CostsKlass;
import com.geekspace.beyikyol.CustomView.CustomTypeFaceSpan;
import com.geekspace.beyikyol.CustomView.DeleteDialog;
import com.geekspace.beyikyol.CustomView.InfoAlert;
import com.geekspace.beyikyol.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.ViewHolder> {
    ArrayList<CostsKlass> costsKlassArrayList=new ArrayList<>();
    Context context;

    public CostAdapter(ArrayList<CostsKlass> costsKlassArrayList, Context context) {
        this.costsKlassArrayList = costsKlassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cost_design, parent, false);
        return new CostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CostAdapter.ViewHolder holder, final int position) {
        final CostsKlass costsKlass=costsKlassArrayList.get(position);
        String type="";
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        Typeface Ping_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Bold.otf");
        Typeface Ping_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Regular.otf");
        holder.km.setTypeface(Ping_regular);
        holder.sene.setTypeface(Ping_regular);
        holder.type_name.setTypeface(Ping_regular);
        holder.value.setTypeface(Ping_medium);
        if(costsKlass.getType().equals("1")) {

            holder.type_name.setText(context.getResources().getString(R.string.benzin));
            type=context.getResources().getString(R.string.benzin);
            holder.type.setImageResource(R.drawable.benzin_new_icon);
        }

        if(costsKlass.getType().equals("2")){

            holder.type_name.setText(context.getResources().getString(R.string.calyshmyk));
            type=context.getResources().getString(R.string.calyshmyk);
            holder.type.setImageResource(R.drawable.update_new_icon);

        }

        if(costsKlass.getType().equals("3")){

            holder.type_name.setText(context.getResources().getString(R.string.remont));
            type=context.getResources().getString(R.string.remont);
            holder.type.setImageResource(R.drawable.remont_new_icon);
        }
        holder.sene.setText(costsKlass.getSene());
        holder.km.setText(costsKlass.getKm()+" km");
        if(costsKlass.getValue().length()<=100) {


                holder.value.setText(costsKlass.getValue());


        } else if(costsKlass.getValue().length()>100){
            holder.value.setText(costsKlass.getValue().substring(1,100)+"...");
        }


        if(costsKlass.getType().equals("2")) {
            String[] vl = costsKlass.getValue().split(",");
            String txt = "";
            for (int i = 0; i < vl.length; i++) {
                if (vl[i].equals("1")) {
                    txt += context.getResources().getString(R.string.motor_filtre2) + ",";
                }
                if (vl[i].equals("2")) {
                    txt += context.getResources().getString(R.string.motor_yag2) + ",";
                }
                if (vl[i].equals("3")) {
                    txt += context.getResources().getString(R.string.korobka_yag_we_filtr2) + ",";
                }
                if (vl[i].equals("4")) {
                    txt += context.getResources().getString(R.string.howa_filtr2) + ",";
                }
                if (vl[i].equals("5")) {
                    txt += context.getResources().getString(R.string.salon_filtr2) + ",";
                }
                if (vl[i].equals("6")) {
                    txt += context.getResources().getString(R.string.frion2) + ",";
                }


            }
           // txt=txt.replace(" ","");

            txt = txt.substring(0, txt.length() - 1);
            holder.value.setText(txt);
        }


        holder.card.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));


        if(costsKlass.getValue().length()>40 && costsKlass.getValue().length()<=80){
           holder.gapdaly.getLayoutParams().height=550;
        } else if(costsKlass.getValue().length()>80){
            holder.gapdaly.getLayoutParams().height=600;
        }

        final String finalType = type;
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(costsKlass.getValue().length()>100 && !costsKlass.getType().equals("2")) {
                    InfoAlert alert = new InfoAlert(context, "info", finalType, costsKlass.getValue());
                    alert.ShowDialog();
                }
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,position,costsKlass.getId()+"",costsKlass.getType(),costsKlass.getCarId());
            }
        });









    }

    @Override
    public int getItemCount() {
        return costsKlassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView type;
        ImageView gapdaly;
        TextView type_name;
        TextView sene;
        TextView km;
        TextView value;
        ImageButton more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.icon);
            gapdaly=itemView.findViewById(R.id.gapdal);
            type_name=itemView.findViewById(R.id.type);
            sene=itemView.findViewById(R.id.sene);
            km=itemView.findViewById(R.id.km);
            value=itemView.findViewById(R.id.value);
            card=itemView.findViewById(R.id.card);
            more=itemView.findViewById(R.id.more);
        }
    }

    private void showPopup(View view, final int pos, final String id, final String type, final String carId){
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
                        if(type.equals("1")){
                            Intent benzin_intent = new Intent(context, BenzinUpdate.class);
                            benzin_intent.putExtra("id",carId);
                            benzin_intent.putExtra("type","2");
                            benzin_intent.putExtra("costId",id);
                            context.startActivity(benzin_intent);
                          //  Animatoo.animateSwipeLeft(context);
                            ((Activity) context).finish();
                        }

                        if(type.equals("2")){
                            Intent benzin_intent = new Intent(context, CalyshmakUpdate.class);
                            benzin_intent.putExtra("id",carId);
                            benzin_intent.putExtra("type","2");
                            benzin_intent.putExtra("costId",id);
                            context.startActivity(benzin_intent);
                           // Animatoo.animateSwipeLeft(context);
                            ((Activity) context).finish();
                        }

                        if(type.equals("3")){
                            Intent benzin_intent = new Intent(context, RemontUpdate.class);
                            benzin_intent.putExtra("id",carId);
                            benzin_intent.putExtra("type","2");
                            benzin_intent.putExtra("costId",id);
                            context.startActivity(benzin_intent);
                         //   Animatoo.animateSwipeLeft(context);
                            ((Activity) context).finish();
                        }



                        return true;
                    case R.id.delete:
                        CostsKlass cars2=costsKlassArrayList.get(pos);
                        DeleteDialog confirmDialog=new DeleteDialog(R.drawable.question_new,context.getResources().getString(R.string.attention),context.getResources().getString(R.string.sorag),context,"yes",R.drawable.question_new,1,id,cars2.getCostId(),carId,type);
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

}
