package com.basusingh.nsspgdav.utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.adapter.adapter_events;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_DailyThought;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectDailyThought;
import com.basusingh.nsspgdav.helper.ObjectEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AppDataService extends IntentService {

    String tag_str_req;

    public AppDataService() {
        super("AppDataService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            fetchOnlineContent();
        }
    }


    public void fetchOnlineContent(){
        try{
            tag_str_req = "event_search_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "getfirstdata", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_search_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
        }
    }



    public void check_search_response(String response){
        String error;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");

            if(error.equals("false")) {
                add_data(jsonObj.getString("news"), jsonObj.getString("toppost"), jsonObj.getString("thought"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);

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

            }.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
