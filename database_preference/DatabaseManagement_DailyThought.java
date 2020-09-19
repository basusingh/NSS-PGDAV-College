package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Basu Singh on 10/12/2016.
 */
public class DatabaseManagement_DailyThought extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "DailyThoughtDatabase";

    public DatabaseManagement_DailyThought(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE mDailyThought " +
                "( id INTEGER PRIMARY KEY, " +
                "thought TEXT, " +
                "time_stamp TEXT ) ";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        String sql = "DROP TABLE IF EXISTS mDailyThought";
        db.execSQL(sql);

        onCreate(db);
    }

}
