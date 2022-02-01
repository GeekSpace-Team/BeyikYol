package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CostsDB extends SQLiteOpenHelper {
    private static final String DBNAME="umumy";
    private static final String TBNAME="costs";
    public CostsDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,carid TEXT,item_id TEXT,item_type TEXT,gun TEXT,ay TEXT,yyl TEXT,hepde TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }

    public Cursor getAll(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" WHERE carid='"+id+"' ORDER BY ID DESC",null);
        return cursor;
    }

    public Cursor customSelect(String  sql){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }

    public boolean insert(String carid,String item_id,String item_type,String gun,String ay,String yyl,String hepde){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("carid",carid);
        contentValues.put("item_id",item_id);
        contentValues.put("item_type",item_type);
        contentValues.put("gun",gun);
        contentValues.put("ay",ay);
        contentValues.put("yyl",yyl);
        contentValues.put("hepde",hepde);
        long result=db.insert(TBNAME,null,contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(String id,String carid,String item_id,String item_type,String gun,String ay,String yyl,String hepde){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("carid",carid);
        contentValues.put("item_id",item_id);
        contentValues.put("item_type",item_type);
        contentValues.put("gun",gun);
        contentValues.put("ay",ay);
        contentValues.put("yyl",yyl);
        contentValues.put("hepde",hepde);
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
