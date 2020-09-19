package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.basusingh.nsspgdav.helper.ObjectRegistrationCompleted;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/11/2016.
 */
public class TableController_Registration extends DatabaseManagement_Registration {

    public TableController_Registration(Context context){
        super(context);
    }


    public void addData(ObjectRegistrationCompleted o){
        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mRegistration (id, heading, time_stamp, imageurl) values(?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.bindString(1, o.getId());
        insert.bindString(2, o.getHeading());
        insert.bindString(3, o.getTime_stamp());
        insert.bindString(4, o.getImageurl());
        insert.execute();

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<ObjectRegistrationCompleted> getAllList(){
        List<ObjectRegistrationCompleted> list = new ArrayList<>();

        String selectQuery = "SELECT id, heading, time_stamp, imageurl FROM mRegistration ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                ObjectRegistrationCompleted o = new ObjectRegistrationCompleted();
                o.setId(cursor.getString(0));
                o.setHeading(cursor.getString(1));
                o.setTime_stamp(cursor.getString(2));
                o.setImageurl(cursor.getString(3));
                list.add(o);
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return list;
    }


    public boolean isExist(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM mRegistration WHERE id = '" + id + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }
}
