package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BenzinDB extends SQLiteOpenHelper {
    private static final String DBNAME="benzinDB";
    private static final String TBNAME="benzin";
    public BenzinDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,carID TEXT,litre TEXT,miles TEXT,gun TEXT,ay TEXT,yyl TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
       onCreate(db);
    }

    public boolean insertInto(String cardID,String litre,String miles,String gun,String ay,String yyl){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("carID",cardID);
        contentValues.put("litre",litre);
        contentValues.put("miles",miles);
        contentValues.put("gun",gun);
        contentValues.put("ay",ay);
        contentValues.put("yyl",yyl);
       long result= db.insert(TBNAME,null,contentValues);
       if(result==-1){
           return false;
       } else{
           return true;
       }
    }


    public Cursor getAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME, null);
        return cursor;
    }

    public Cursor getSelect(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE id = '"+id+"'", null);
        return cursor;
    }

    public Cursor getCar(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carID = '"+id+"'", null);
        return cursor;
    }

    public Cursor getSt(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carID = '"+id+"' ORDER BY ID DESC LIMIT 1", null);
        return cursor;
    }

    public boolean updateData(String id,String litre,String miles){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("litre",litre);
        contentValues.put("miles",miles);
        db.update(TBNAME,contentValues,"ID=?",new String[]{id});
        return true;
    }

    public Cursor getLastInsertId(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM "+TBNAME+" ORDER BY ID DESC LIMIT 1", null);
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


