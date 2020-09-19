package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Basu Singh on 10/7/2016.
 */
public class DatabaseManagment_BloodGroup extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "BloodGroupFavouriteDatabase";

    public DatabaseManagment_BloodGroup(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE mBloodGroup " +
                "( id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "age TEXT, " +
                "mobile TEXT, " +
                "bloodgroup TEXT ) ";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        String sql = "DROP TABLE IF EXISTS mBloodGroup";
        db.execSQL(sql);

        onCreate(db);
    }
}
