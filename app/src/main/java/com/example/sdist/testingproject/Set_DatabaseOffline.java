package com.example.sdist.testingproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

/**
 * Created by Sdist on 8/23/2017.
 */

public class Set_DatabaseOffline extends SQLiteOpenHelper{

//    Variables for offline database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "camshot.db";
    private static final String TABLE_NAME = "";
    private static final String COLUMN_USERID = "userid";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_FILE = "object";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_RECIPIENT = "recipient";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String EMPTY_RESULT = "null";
    SQLiteDatabase db;

    private static String TABLE_CREATE = "CREATE TABLE %S (%S INTEGER PRIMARY KEY, %S %S, %S %S, %S %S, %S %S)";

//            "create table contacts (id integer primary key not null, " +
//            "name text not null, email text not null, uname text not null, pass text not null)";

    public Set_DatabaseOffline(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(String.format(TABLE_CREATE,"messages","_id",COLUMN_USERID, "INTEGER", COLUMN_MESSAGE, "TEXT", COLUMN_RECIPIENT, "TEXT", COLUMN_TIMESTAMP, "NUMERIC"));
        db.execSQL(String.format(TABLE_CREATE,"messages","_id",COLUMN_USERID, "INTEGER", COLUMN_FILE, "TEXT", COLUMN_RECIPIENT, "TEXT", COLUMN_TIMESTAMP, "NUMERIC"));
        this.db = db;
    }

    public void insertContact(Data_Offline c, int table){

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

//    public void createTable(int table){
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//
//        String query = "select * from contacts";
//        Cursor cursor = db.rawQuery(query, null);
//        int count = cursor.getCount();
//
//        values.put(COLUMN_NAME, c.getName());
//        values.put(COLUMN_EMAIL, c.getEmail());
//        values.put(COLUMN_UNAME, c.getUname());
//        values.put(COLUMN_PASS, c.getPass());
//
//        db.insert(TABLE_NAME, null, values);
//        db.close();
//    }
}
