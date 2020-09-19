package com.basusingh.nsspgdav.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    EditText feedback;
    ProgressDialog progressDialog;
    String tag_str_req;
    UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        userPreference = new UserPreference(getApplicationContext());
        feedback = (EditText) findViewById(R.id.feedback);
        feedback.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    hideKeyboard();
                    initiateSubmit(feedback.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        progressDialog = new ProgressDialog(Feedback.this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                initiateSubmit(feedback.getText().toString());
            }
        });

    }

    public void initiateSubmit(String feedback){
        if(feedback.isEmpty()){
            showSnackBarMessage("Please enter feedback");
            return;
        }
        submitFeedback(feedback);
    }


    public void submitFeedback(final String feedback){
        progressDialog.show();

        try{
            tag_str_req = "send_feedback_req";


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "submitfeedback", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "There seems to be an problem with your Internet Connection.", Toast.LENGTH_LONG).show();
                    showSnackBarMessage("Sorry, an error occurred!");
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("feedback", feedback);
                    params.put("email", userPreference.getEmail());


                    return params;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            showSnackBarMessage("Sorry, an error occurred!");
        }
    }



    public void check_response(String response){
        String error;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");

            if(error.equals("true")){
                showSnackBarMessage("Sorry, an error occurred!");
            } else if(error.equals("false")) {
                showSnackBarMessage("Feedback submitted");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            showSnackBarMessage("Sorry, an error occurred!");

        }
    }

    public void showSnackBarMessage(String msg){

        progressDialog.dismiss();

        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinateLayout), msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();

    }


    public void hideKeyboard(){
       try{
           View view = this.getCurrentFocus();
           if (view != null) {
               InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
           }
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public void onStop(){
        super.onStop();
        try{
            AppController.getInstance().cancelPendingRequests(tag_str_req);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }


}
