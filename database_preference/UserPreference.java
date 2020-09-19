package com.basusingh.nsspgdav.database_preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.basusingh.nsspgdav.BuildConfig;

/**
 * Created by Basu Singh on 10/9/2016.
 */
public class UserPreference {

    Context contect;

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "user_preference";

    public static final String SHOW_NOTIFICATION = "show_notification";

    public static final String KEY_EMAIL = "email";

    public static final String IS_FCM_AVAILABLE = "isFcmAvailable";

    public static final String IS_USER_REGISTERED = "isUserRegistered";

    public static final String IS_FIRST_RUN = "isFirstRun";

    public static final String IS_FIRST_DATA_LOADED = "isFirstDataLoaded";

    public static final String UPDATE_VERSION = "UpdateVersion";


    public UserPreference(Context context){
        this.contect = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setUserRegistered(){
        editor.putBoolean(IS_USER_REGISTERED, true);
        editor.apply();
    }

    public boolean isUserRegistered(){
        return pref.getBoolean(IS_USER_REGISTERED, false);
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getEmail(){
        return pref.getString(KEY_EMAIL, "info@basusingh.com");
    }

    public void setNotificationPref(boolean value){
        editor.putBoolean(SHOW_NOTIFICATION, value);
        editor.apply();
    }

    public boolean getNotificationPref(){
        return pref.getBoolean(SHOW_NOTIFICATION, false);
    }



    public void setFirstRunDone(){
        editor.putBoolean(IS_FIRST_RUN, false);
        editor.apply();
    }

    public boolean isFirstRun(){
        return pref.getBoolean(IS_FIRST_RUN, true);
    }


    public void setFirstDataLoadedDone(){
        editor.putBoolean(IS_FIRST_DATA_LOADED, true);
        editor.apply();
    }

    public boolean isFirstDataLoaded(){
        return pref.getBoolean(IS_FIRST_DATA_LOADED, false);
    }

    public void setUpdateVersionRequired(String version){
        editor.putString(UPDATE_VERSION, version);
        editor.apply();
    }

    public String getUpdateVersion(){
        return pref.getString(UPDATE_VERSION, BuildConfig.VERSION_NAME);
    }
}
