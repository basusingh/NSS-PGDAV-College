package com.basusingh.nsspgdav.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.basusingh.nsspgdav.BuildConfig;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.app_start.AppStartActivity;
import com.basusingh.nsspgdav.app_start.FcmAndLoadFirstData;
import com.basusingh.nsspgdav.app_start.LoginUser;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.utils.DataService;

/**
 * Created by Basu Singh on 10/4/2016.
 */
public class SplashScreen extends AppCompatActivity {

    UserPreference appData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appData = new UserPreference(getApplicationContext());
        startService(new Intent(getApplicationContext(), DataService.class));

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
    }

    public void start(){
        if(appData.isFirstRun()){
            startActivity(new Intent(getApplicationContext(), AppStartActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(! new UserPreference(getApplicationContext()).isUserRegistered()){
            startActivity(new Intent(getApplicationContext(), LoginUser.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(!appData.isFirstDataLoaded()){
            startActivity(new Intent(getApplicationContext(), FcmAndLoadFirstData.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if(Float.parseFloat(BuildConfig.VERSION_NAME)>Float.parseFloat(appData.getUpdateVersion())){
            startActivity(new Intent(getApplicationContext(), UpdateApp.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }
}
