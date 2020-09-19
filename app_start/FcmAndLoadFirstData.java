package com.basusingh.nsspgdav.app_start;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.BuildConfig;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.MainActivity;
import com.basusingh.nsspgdav.activity.UpdateApp;
import com.basusingh.nsspgdav.adapter.adapter_events;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_DailyThought;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectDailyThought;
import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.basusingh.nsspgdav.utils.AppController;
import com.basusingh.nsspgdav.utils.AppPrepareService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FcmAndLoadFirstData extends AppCompatActivity {


    TextView bottom_msg;
    ProgressBar progressBar;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_load_first_data);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bottom_msg = (TextView) findViewById(R.id.bottom_msg);

        try{
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if(refreshedToken != null){
                final String imei;
                try{
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    imei = telephonyManager.getDeviceId();
                } catch (Exception e){
                    e.printStackTrace();
                    askForPermissions(new String[]{Manifest.permission.READ_PHONE_STATE});
                    return;
                }
                startService(new Intent(getApplicationContext(), AppPrepareService.class).putExtra("key", refreshedToken).putExtra("imei", imei));
            } else {
                Toast.makeText(FcmAndLoadFirstData.this, "Sorry an error occurred! Please check your internet connection and try again later.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(FcmAndLoadFirstData.this, "Sorry an error occurred! Please check your internet connection and try again later.", Toast.LENGTH_LONG).show();
        }


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals("com.basusingh.nsspgdav.FcmAndLoadFirstData.Error")) {
                    String msg = intent.getStringExtra("msg");
                    if(msg.equalsIgnoreCase("AppReadySuccess")){
                        startMainActivity();
                    }
                }
            }
        };


    }



    public void askForPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("com.basusingh.nsspgdav.FcmAndLoadFirstData.Error"));

    }

    @Override
    public void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    public void startMainActivity(){
        new UserPreference(FcmAndLoadFirstData.this).setFirstDataLoadedDone();
        if(Float.parseFloat(BuildConfig.VERSION_NAME)>Float.parseFloat(new UserPreference(FcmAndLoadFirstData.this).getUpdateVersion())){
            startActivity(new Intent(FcmAndLoadFirstData.this, UpdateApp.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(FcmAndLoadFirstData.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }


    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        return true;
    }

    private void setScaleAnimationVisible(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    private void setScaleAnimationGone(View view) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }
}
