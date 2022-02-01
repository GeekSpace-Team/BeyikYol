package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CalyshmakDB extends SQLiteOpenHelper {
    private static final String DBNAME="calyshmakdb";
    private static final String TBNAME="calyshmak";
    public CalyshmakDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,carId TEXT,calyshylan TEXT,gun TEXT,ay TEXT,yyl TEXT,alarm TEXT,status TEXT,km TEXT,yene INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }

    public boolean insert(String carId,String calyshylan,String gun,String ay,String yyl,String alarm,String status,String km,int yene){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("carId",carId);
        contentValues.put("calyshylan",calyshylan);
        contentValues.put("gun",gun);
        contentValues.put("ay",ay);
        contentValues.put("yyl",yyl);
        contentValues.put("alarm",alarm);
        contentValues.put("status",status);
        contentValues.put("km",km);
        contentValues.put("yene",yene);
        long result=db.insert(TBNAME,null,contentValues);
        if(result==-1){
            return false;
        } else{
            return true;
        }
    }

    public Cursor getAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME,null);
        return cursor;
    }

    public Cursor getAlarm(String km,String carId){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE alarm='1' and status='0' and carId='"+carId+"' and yene<="+km, null);
        return cursor;
    }
    public Cursor getSt(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carId = '"+id+"' ORDER BY ID DESC LIMIT 1", null);
        return cursor;
    }

    public boolean updateData(String id,String calyshylan,String alarm,String status,String km,int yene){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("calyshylan",calyshylan);
        contentValues.put("alarm",alarm);
        contentValues.put("status",status);
        contentValues.put("km",km);
        contentValues.put("yene",yene);
        db.update(TBNAME,contentValues,"ID=?",new String[]{id});
        return true;
    }
    public Cursor getCar(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carId = '"+id+"'", null);
        return cursor;
    }

    public boolean updateSt(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("status","1");
        db.update(TBNAME,contentValues,"carId=?",new String[]{id});
        return true;
    }

    public Cursor getLastInsertId(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM "+TBNAME+" ORDER BY ID DESC LIMIT 1", null);
        return cursor;
    }

    public Cursor getSelect(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE id = '"+id+"'", null);
        return cursor;
    }




    public Integer deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TBNAME,"ID=?",new String[]{id});

    }

    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }
}
