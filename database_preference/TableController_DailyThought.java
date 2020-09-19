package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.basusingh.nsspgdav.helper.ObjectDailyThought;
import com.basusingh.nsspgdav.helper.ObjectEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/12/2016.
 */
public class TableController_DailyThought extends DatabaseManagement_DailyThought {

    public TableController_DailyThought(Context context){
        super(context);
    }

    public void addData(ObjectDailyThought o){

        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mDailyThought (id, thought, time_stamp) values(?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.bindString(1, o.getId());
        insert.bindString(2, o.getThought());
        insert.bindString(3, o.getTime_stamp());
        insert.execute();

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public void addList(List<ObjectDailyThought> list){
        deleteAll();

        SQLiteDatabase db  = this.getWritableDatabase();
        db.beginTransaction();

        String sql = "Insert or Replace into mDailyThought (id, thought, time_stamp) values(?, ?, ?)";
        SQLiteStatement insert = db.compileStatement(sql);

        for(int i = 0; i<list.size(); i++){
            ObjectDailyThought o = list.get(i);
            insert.bindString(1, o.getId());
            insert.bindString(2, o.getThought());
            insert.bindString(3, o.getTime_stamp());
            insert.execute();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    public List<ObjectDailyThought> getAllList(){
        List<ObjectDailyThought> list = new ArrayList<>();

        String selectQuery = "SELECT id, thought, time_stamp FROM mDailyThought ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ObjectDailyThought o = new ObjectDailyThought();
                o.setId(cursor.getString(0));
                o.setThought(cursor.getString(1));
                o.setTime_stamp(cursor.getString(2));

                list.add(o);
            } while (cursor.moveToNext());
        }

        cursor.close();

        db.close();


        return list;
    }


    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM mDailyThought";
        db.execSQL(sql);
        db.close();
    }
}
