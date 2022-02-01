package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SearchDB extends SQLiteOpenHelper {
    private static final String DBNAME="history_db";
    private static final String TBNAME="history";

    public SearchDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,search_title TEXT,latitude TEXT,longitude TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
       onCreate(db);
    }

    public boolean insertData(String search,String latitude,String longitude){
         SQLiteDatabase db=this.getWritableDatabase();
         ContentValues values=new ContentValues();
          values.put("search_title",search);
          values.put("latitude",latitude);
          values.put("longitude",longitude);

          long result=db.insert(TBNAME,null,values);
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



    public Cursor getSelect(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID FROM "+TBNAME+" WHERE search_title = '"+name+"'", null);
        return cursor;
    }

    public boolean updateData(String id,String search){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("search_title",search);

        db.update(TBNAME,values,"ID=?",new String[]{id});
        return true;
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
