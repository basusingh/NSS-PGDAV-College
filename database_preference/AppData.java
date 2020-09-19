package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.basusingh.nsspgdav.BuildConfig;
import com.basusingh.nsspgdav.helper.Constants;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class AppData {

    Context context;

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "app_data_preference";

    public static final String TODAY_THOUGHT = "thought";

    public static final String TODAY_THOUGHT_TIME_STAMP = "time_stamp";

    public static final String IMAGE_1 = "image1";

    public static final String IMAGE_2 = "image2";

    public static final String IMAGE_3 = "image3";

    public static final String IMAGE_4 = "image4";

    public static final String IMAGE_5 = "image5";

    public AppData(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void updateTopImages(String image1, String image2, String image3, String image4, String image5){
        editor.putString(IMAGE_1, image1);
        editor.putString(IMAGE_2, image2);
        editor.putString(IMAGE_3, image3);
        editor.putString(IMAGE_4, image4);
        editor.putString(IMAGE_5, image5);
        editor.apply();
    }


   public String getImage1(){
       return pref.getString(IMAGE_1, "");
   }

    public String getImage2(){
        return pref.getString(IMAGE_2, "");
    }

    public String getImage3(){
        return pref.getString(IMAGE_3, "");
    }

    public String getImage4(){
        return pref.getString(IMAGE_4, "");
    }

    public String getImage5(){
        return pref.getString(IMAGE_5, "");
    }


    public String getThought(){
        return pref.getString(TODAY_THOUGHT, "A nation's culture resides in the hearts and soul of its people.");
    }

    public String getThoughtTimeStamp(){
        return pref.getString(TODAY_THOUGHT_TIME_STAMP, "Forever");
    }

    public void setThought(String time_stamp, String thought){
        editor.putString(TODAY_THOUGHT, thought);
        editor.putString(TODAY_THOUGHT_TIME_STAMP, time_stamp);
        editor.apply();
    }

}
