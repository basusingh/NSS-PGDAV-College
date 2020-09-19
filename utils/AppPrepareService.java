package com.basusingh.nsspgdav.utils;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_DailyThought;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectDailyThought;
import com.basusingh.nsspgdav.helper.ObjectEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class AppPrepareService extends IntentService {

    String tag_str_req;

    public AppPrepareService() {
        super("AppPrepareService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String key  = intent.getStringExtra("key");
            String imei = intent.getStringExtra("imei");
            sendKeyToServer(key, imei);
        }
    }


    public void sendKeyToServer(final String key, final String imei){

        tag_str_req = "update_fcm_req";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.SERVER_BASE_URL + "updatefcm", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                check_response(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                makeToast("Internet connectivity issue");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fcm", key);
                params.put("email", new UserPreference(getApplicationContext()).getEmail());
                params.put("imei", imei);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }


    public void check_response(String response){
        String error;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");

            if(error.equals("true")){
                makeToast("Server error");
            } else if(error.equals("false")) {
                getFirstData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            makeToast("Sorry an error occurred");
        }
    }



    public void getFirstData(){

        try{
            tag_str_req = "first_data_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "getfirstdata", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_loadData_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    makeToast("There seems to be an problem with your Internet Connection.");
                }
            });

            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            makeToast("Sorry an error occurred");
        }
    }

    public void check_loadData_response(String response){
        String error;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");

            if(error.equals("true")){
                makeToast("Sorry an error occurred");
            } else if(error.equals("false")) {
                add_data(jsonObj.getString("news"), jsonObj.getString("toppost"), jsonObj.getString("thought"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            makeToast("Sorry an error occurred");

        }
    }


    public void add_data(final String news, final String toppost, final String thought){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try{

                        List<ObjectEvents> list = new ArrayList<>();
                        JSONArray a = new JSONArray(news);
                        for(int i = 0; i<a.length(); i++){
                            JSONObject o = a.getJSONObject(i);
                            ObjectEvents on = new ObjectEvents();
                            on.setId(o.getString("id"));
                            on.setTimeStamp(o.getString("time_stamp"));
                            on.setDescription(o.getString("description"));
                            on.setHeading(o.getString("heading"));
                            on.setImageUrl(o.getString("imageurl"));
                            on.setType(o.getString("type"));
                            list.add(on);
                        }

                        new TableController_Event(getApplicationContext()).addList(list);


                        JSONArray array1 = new JSONArray(toppost);
                        for(int i = 0; i<array1.length(); i++){
                            JSONObject o = array1.getJSONObject(i);
                            new AppData(getApplicationContext()).updateTopImages(o.getString("image1"), o.getString("image2"), o.getString("image3"), o.getString("image4"), o.getString("image5"));
                        }



                        List<ObjectDailyThought> list1 = new ArrayList<>();
                        JSONArray array2 = new JSONArray(thought);
                        for(int i = 0; i<array2.length(); i++){
                            JSONObject o = array2.getJSONObject(i);
                            if(i == 0){
                                new AppData(getApplicationContext()).setThought(o.getString("time_stamp"), o.getString("thought"));
                            }
                            ObjectDailyThought on = new ObjectDailyThought();
                            on.setId(o.getString("id"));
                            on.setThought(o.getString("thought"));
                            on.setTime_stamp(o.getString("time_stamp"));
                            list1.add(on);
                        }
                        new TableController_DailyThought(getApplicationContext()).addList(list1);


                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void onPostExecute(String data){
                   sendLocalBroadcast("AppReadySuccess");
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            makeToast("Sorry an error occurred");
        }
    }


    public void sendLocalBroadcast(String msg){
        Intent intent = new Intent("com.basusingh.nsspgdav.FcmAndLoadFirstData.Error");
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
    }

    public void makeToast(final String msg){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

}
