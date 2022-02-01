package com.geekspace.beyikyol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CarDB extends SQLiteOpenHelper {
    private static final String DBNAME = "my_db";
    private static final String TBNAME = "cars";

    public CarDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBNAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,marka TEXT,model TEXT,engine_power TEXT,engine_type TEXT,type TEXT,transmission TEXT,year TEXT,miles TEXT,vin TEXT,phone_number TEXT,image blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBNAME);
        onCreate(db);
    }

    public boolean insertData(String marka, String model, String engine_power, String engine_type, String type, String transmission, String year, String miles, String vin, String phone_number, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("marka", marka);
        values.put("model", model);
        values.put("engine_power", engine_power);
        values.put("engine_type", engine_type);
        values.put("type", type);
        values.put("transmission", transmission);
        values.put("year", year);
        values.put("miles", miles);
        values.put("vin", vin);
        values.put("phone_number", phone_number);
        values.put("image", image);
        long result = db.insert(TBNAME, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBNAME, null);
        return cursor;
    }

    public Cursor getSelect(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBNAME + " WHERE id = '" + id + "'", null);
        return cursor;
    }

    public boolean updateData(String id, String marka, String model, String engine_power, String engine_type, String type, String transmission, String year, String miles, String vin, String phone_number, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("marka", marka);
        values.put("model", model);
        values.put("engine_power", engine_power);
        values.put("engine_type", engine_type);
        values.put("type", type);
        values.put("transmission", transmission);
        values.put("year", year);
        values.put("miles", miles);
        values.put("vin", vin);
        values.put("phone_number", phone_number);
        values.put("image", image);
        values.put("image", image);
        db.update(TBNAME, values, "ID=?", new String[]{id});
        return true;
    }

    public byte[] getImage(String id) {
        byte[] result = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT image FROM " + TBNAME + " WHERE id = '" + id + "'", null);
        if (cursor.moveToNext()) {

            do {
                result = cursor.getBlob(0);
            } while (cursor.moveToNext());

        }

        return result;

    }

    public boolean updateBenzin(String id, String miles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("miles", miles);
        db.update(TBNAME, values, "ID=?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TBNAME, "ID=?", new String[]{id});

    }

    public void truncate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TBNAME);
        onCreate(db);
    }


}
