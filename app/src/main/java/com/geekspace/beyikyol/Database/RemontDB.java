package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RemontDB extends SQLiteOpenHelper {
    private static final String DBNAME="remontdb";
    private static final String TBNAME="remont";
    public RemontDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,carid TEXT,remont_text TEXT,gun TEXT,ay TEXT,yyl TEXT,km TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
       onCreate(db);
    }


    public boolean insert(String carid,String remont_text,String gun,String ay,String yyl,String km){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("carid",carid);
        contentValues.put("remont_text",remont_text);
        contentValues.put("gun",gun);
        contentValues.put("ay",ay);
        contentValues.put("yyl",yyl);
        contentValues.put("km",km);

        long result=db.insert(TBNAME,null,contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME,null);
        return cursor;
    }
    public Cursor getCar(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carid = '"+id+"'", null);
        return cursor;
    }


    public boolean updateData(String id,String remont_text,String km){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("remont_text",remont_text);
        contentValues.put("km",km);
        db.update(TBNAME,contentValues,"ID=?",new String[]{id});
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
    public Cursor getSt(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carid = '"+id+"' ORDER BY ID DESC LIMIT 1", null);
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
