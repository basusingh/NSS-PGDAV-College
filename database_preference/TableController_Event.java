package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.basusingh.nsspgdav.helper.ObjectEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/9/2016.
 */
public class TableController_Event extends DatabaseManagement_Event {

    public TableController_Event(Context context){
        super(context);
    }

    public void addSingleData(ObjectEvents o){
        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mEvent (id, heading, description, date, type, imageurl) values(?, ?, ?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.bindString(1, o.getId());
        insert.bindString(2, o.getHeading());
        insert.bindString(3, o.getDescription());
        insert.bindString(4, o.getTimeStamp());
        insert.bindString(5, o.getType());
        insert.bindString(6, o.getImageUrl());
        insert.execute();

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }



    public void addList(List<ObjectEvents> list){
        deleteAll();

        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mEvent (id, heading, description, date, type, imageurl) values(?, ?, ?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);

        for(int i = 0; i<list.size(); i++){
            ObjectEvents o = list.get(i);
            insert.bindString(1, o.getId());
            insert.bindString(2, o.getHeading());
            insert.bindString(3, o.getDescription());
            insert.bindString(4, o.getTimeStamp());
            insert.bindString(5, o.getType());
            insert.bindString(6, o.getImageUrl());
            insert.execute();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<ObjectEvents> getAllEvent(){
        List<ObjectEvents> list = new ArrayList<>();

        String selectQuery = "SELECT id, heading, description, date, type, imageurl FROM mEvent ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ObjectEvents o = new ObjectEvents();
                o.setId(cursor.getString(0));
                o.setHeading(cursor.getString(1));
                o.setDescription(cursor.getString(2));
                o.setTimeStamp(cursor.getString(3));
                o.setType(cursor.getString(4));
                o.setImageUrl(cursor.getString(5));
                list.add(o);
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return list;
    }

    public List<ObjectEvents> getTopEvent(){
        List<ObjectEvents> list = new ArrayList<>();

        String selectQuery = "SELECT id, heading, description, date, type, imageurl FROM mEvent ORDER BY id DESC LIMIT 10";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ObjectEvents o = new ObjectEvents();
                o.setId(cursor.getString(0));
                o.setHeading(cursor.getString(1));
                o.setDescription(cursor.getString(2));
                o.setTimeStamp(cursor.getString(3));
                o.setType(cursor.getString(4));
                o.setImageUrl(cursor.getString(5));
                list.add(o);
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return list;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM mEvent";
        db.execSQL(sql);
        db.close();
    }


    public boolean isExist(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM mEvent WHERE id = '" + id + "'";
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
