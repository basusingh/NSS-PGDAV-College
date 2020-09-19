package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.basusingh.nsspgdav.helper.ObjectBloodGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/7/2016.
 */
public class TableController_BloodGroup extends DatabaseManagment_BloodGroup {

    public TableController_BloodGroup(Context context){
        super(context);
    }


    public void addData(ObjectBloodGroup o){
        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mBloodGroup (id, name, age, mobile, bloodgroup) values(?, ?, ?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.bindString(1, o.getId());
        insert.bindString(2, o.getName());
        insert.bindString(3, o.getAge());
        insert.bindString(4, o.getMobile());
        insert.bindString(5, o.getBloodgroup());
        insert.execute();

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void removeData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM mBloodGroup WHERE id = '" + id + "'";
        db.execSQL(sql);
        db.close();
    }

    public List<ObjectBloodGroup> getAllFav(){
        List<ObjectBloodGroup> list = new ArrayList<>();

        String selectQuery = "SELECT id, name, age, mobile, bloodgroup FROM mBloodGroup";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ObjectBloodGroup o = new ObjectBloodGroup();
                o.setId(cursor.getString(0));
                o.setName(cursor.getString(1));
                o.setAge(cursor.getString(2));
                o.setMobile(cursor.getString(3));
                o.setBloodgroup(cursor.getString(4));
                list.add(o);
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();


        return list;
    }


    public boolean isExist(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM mBloodGroup WHERE id = '" + id + "'";
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
