package com.basusingh.nsspgdav.fcm;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.utils.AppController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class IDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    UserPreference userPreference;
    String tag_str_req;

    @Override
    public void onTokenRefresh() {

        userPreference = new UserPreference(getApplicationContext());
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String key) {
        if(userPreference.isUserRegistered()){
            final String imei;

            try{
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            try{
                tag_str_req = "update_fcm_req";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        Constants.SERVER_BASE_URL + "updatefcm", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
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

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}