package com.basusingh.nsspgdav.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.database_preference.TableController_Registration;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectRegistration;
import com.basusingh.nsspgdav.helper.ObjectRegistrationCompleted;
import com.basusingh.nsspgdav.utils.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegistrationForm extends AppCompatActivity {

    String tag_str_req;
    ObjectRegistration o;
    ImageView toolbar_image;
    CardView name_layout, course_layout, mobile_layout, email_layout, address_layout, year_layout, dob_layout, past_exp_layout, new_ideas_layout, blood_group_layout, organic_medicine_layout, medical_issue_layout, inter_college_name_layout;
    Button btn_submit;
    EditText edit_name, edit_course, edit_mobile, edit_email, edit_address, edit_year, edit_dob, edit_past_exp, edit_new_ideas, edit_blood_group, edit_organic_medicine, edit_medical_issue, edit_inter_college_name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_registration_form);

        Intent i = getIntent();
        if(i != null){
            Bundle bundle = i.getExtras();
            o = (ObjectRegistration)bundle.getSerializable("data");
        }

        setUpToolbar();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                bindItem();
                setUpVisibility();
            }
        });


        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(RegistrationForm.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                validateDetail();
            }
        });

        try{
            Picasso.with(getApplicationContext()).load(o.getImageurl()).into(toolbar_image);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void bindItem(){
        name_layout = (CardView) findViewById(R.id.name_layout);
        course_layout = (CardView) findViewById(R.id.course_layout);
        mobile_layout = (CardView) findViewById(R.id.mobile_layout);
        email_layout = (CardView) findViewById(R.id.email_layout);
        address_layout = (CardView) findViewById(R.id.address_layout);
        year_layout = (CardView) findViewById(R.id.year_layout);
        dob_layout = (CardView) findViewById(R.id.dob_layout);
        past_exp_layout = (CardView) findViewById(R.id.past_exp_layout);
        new_ideas_layout = (CardView) findViewById(R.id.new_ideas_layout);
        blood_group_layout = (CardView) findViewById(R.id.blood_group_layout);
        organic_medicine_layout = (CardView) findViewById(R.id.organic_medicine_layout);
        medical_issue_layout = (CardView) findViewById(R.id.medical_issue_layout);
        inter_college_name_layout = (CardView) findViewById(R.id.inter_college_name_layout);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_course = (EditText) findViewById(R.id.edit_course);
        edit_mobile = (EditText) findViewById(R.id.edit_mobile);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_year = (EditText) findViewById(R.id.edit_year);
        edit_dob = (EditText) findViewById(R.id.edit_dob);
        edit_past_exp = (EditText) findViewById(R.id.edit_past_exp);
        edit_new_ideas = (EditText) findViewById(R.id.edit_new_ideas);
        edit_blood_group = (EditText) findViewById(R.id.edit_blood_group);
        edit_organic_medicine = (EditText) findViewById(R.id.edit_organic_medicine);
        edit_medical_issue = (EditText) findViewById(R.id.edit_medical_issue);
        edit_inter_college_name = (EditText) findViewById(R.id.edit_inter_college_name);
    }

    public void setUpVisibility(){
        name_layout.setVisibility(o.isName()? View.VISIBLE:View.GONE);
        course_layout.setVisibility(o.isCourse()? View.VISIBLE:View.GONE);
        mobile_layout.setVisibility(o.isMobile()? View.VISIBLE:View.GONE);
        email_layout.setVisibility(o.isEmail()? View.VISIBLE:View.GONE);
        address_layout.setVisibility(o.isAddress()? View.VISIBLE:View.GONE);
        year_layout.setVisibility(o.isYear()? View.VISIBLE:View.GONE);
        dob_layout.setVisibility(o.isDob()? View.VISIBLE:View.GONE);
        past_exp_layout.setVisibility(o.isPast_exp()? View.VISIBLE:View.GONE);
        new_ideas_layout.setVisibility(o.isNew_ideas()? View.VISIBLE:View.GONE);
        blood_group_layout.setVisibility(o.isBlood_group()? View.VISIBLE:View.GONE);
        organic_medicine_layout.setVisibility(o.isOrganic_medicine()? View.VISIBLE:View.GONE);
        medical_issue_layout.setVisibility(o.isMedical_issue()? View.VISIBLE:View.GONE);
        inter_college_name_layout.setVisibility(o.isInter_college_name()? View.VISIBLE:View.GONE);
    }

    public void validateDetail(){

        String name = "null", course = "null", mobile = "null", email = "null", address = "null", year = "null", dob = "null", past_exp = "null", new_ideas = "null", blood_group = "null", organic_material = "null", medical_issue = "null", inter_college_name = "null";

        if(o.isName()){
            if(edit_name.getText().toString().isEmpty()) {
                makeToast("Please Enter Name");
                return;
            }
            name = edit_name.getText().toString();
        }


        if(o.isDob()){
            if(edit_dob.getText().toString().isEmpty()) {
                makeToast("Please Enter Date of Birth");
                return;
            }
            dob = edit_dob.getText().toString();
        }


        if(o.isMobile()){
            if(edit_mobile.getText().toString().isEmpty()) {
                makeToast("Please Enter Mobile Number");
                return;
            }
            if(edit_mobile.getText().toString().length() != 10){
                makeToast("Mobile Number not valid");
                return;
            }
            mobile = edit_mobile.getText().toString();
        }


        if(o.isEmail()){
            if(edit_email.getText().toString().isEmpty()) {
                makeToast("Please Enter Email");
                return;
            }
            if(!isValidEmail(edit_email.getText().toString())){
                makeToast("Invalid Email Address");
                return;
            }

            email = edit_email.getText().toString();
        }


        if(o.isAddress()){
            if(edit_address.getText().toString().isEmpty()) {
                makeToast("Please Enter Address");
                return;
            }
            address = edit_address.getText().toString();
        }

        if(o.isCourse()){
            if(edit_course.getText().toString().isEmpty()) {
                makeToast("Please Enter Course");
                return;
            }
            course = edit_course.getText().toString();
        }


        if(o.isYear()){
            if(edit_year.getText().toString().isEmpty()) {
                makeToast("Please Enter Course Year");
                return;
            }
            year = edit_year.getText().toString();
        }



        if(o.isInter_college_name()){
            if(edit_inter_college_name.getText().toString().isEmpty()) {
                makeToast("Please Enter College Name");
                return;
            }
            inter_college_name = edit_inter_college_name.getText().toString();
        }


        if(o.isBlood_group()){
            if(edit_blood_group.getText().toString().isEmpty()) {
                makeToast("Please Enter Blood Group");
                return;
            }
            blood_group = edit_blood_group.getText().toString();
        }



        if(o.isNew_ideas()){
            if(edit_new_ideas.getText().toString().isEmpty()) {
                makeToast("Please Enter New Ideas, If Any");
                return;
            }
            new_ideas = edit_new_ideas.getText().toString();
        }


        if(o.isMedical_issue()){
            if(edit_medical_issue.getText().toString().isEmpty()) {
                makeToast("Please Enter Medical Issue, If Any");
                return;
            }
            medical_issue = edit_medical_issue.getText().toString();
        }


        if(o.isOrganic_medicine()){
            if(edit_organic_medicine.getText().toString().isEmpty()) {
                makeToast("Please Enter Organic Medicine Consumption Detail, If Any");
                return;
            }
            organic_material = edit_organic_medicine.getText().toString();
        }


        if(o.isPast_exp()){
            if(edit_past_exp.getText().toString().isEmpty()) {
                makeToast("Please Enter Past Experience, If Any");
                return;
            }
            past_exp = edit_past_exp.getText().toString();
        }

        submitForm(name, course, mobile, email, address, year, dob, past_exp, new_ideas, blood_group, organic_material, medical_issue, inter_college_name);
    }


    public void submitForm(final String name, final String course, final String mobile, final String email, final String address, final String year, final String dob, final String past_exp, final String new_ideas, final String blood_group, final String organic_material, final String medical_issue, final String inter_college_name){
        progressDialog.show();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        final String mToday = format.format(Calendar.getInstance().getTime());

        try{

            tag_str_req = "registration_submit_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "registrationsubmit", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    makeToast("There seems to be an problem with your Internet Connection.");
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("postid", o.getId());
                    params.put("deadline", o.getDeadline());
                    params.put("user_email", new UserPreference(getApplicationContext()).getEmail());
                    params.put("name", name);
                    params.put("dob", dob);
                    params.put("mobile", mobile);
                    params.put("email", email);
                    params.put("address", address);
                    params.put("course", course);
                    params.put("year", year);
                    params.put("inter_college_name", inter_college_name);
                    params.put("blood_group", blood_group);
                    params.put("new_ideas", new_ideas);
                    params.put("medical_issue", medical_issue);
                    params.put("organic_material", organic_material);
                    params.put("past_exp", past_exp);
                    params.put("time_stamp", mToday);


                    return params;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            makeToast("Sorry, an error occurred!");
        }
    }



    public void check_response(String response){
        String error, message;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");
            message = jsonObj.getString("message");

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String mToday = format.format(Calendar.getInstance().getTime());
            ObjectRegistrationCompleted on = new ObjectRegistrationCompleted();
            on.setId(o.getId());
            on.setHeading(o.getHeading());
            on.setImageurl(o.getImageurl());

            if(error.equals("true")){
                if(message.equals("Already Registered")){
                    on.setTime_stamp(jsonObj.getString("time_stamp"));
                    new TableController_Registration(getApplicationContext()).addData(on);
                }
                makeToast(message);
            } else if(error.equals("false")) {
                on.setTime_stamp(mToday);
                new TableController_Registration(getApplicationContext()).addData(on);
                makeToast("Submission successful!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 500);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            makeToast("Sorry, an error occurred!");

        }
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



    public void makeToast(String msg){
        progressDialog.dismiss();
        hideKeyboard();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }



    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.setMargins(0, getStatusBarHeight(), 0, 0);
            toolbar.setLayoutParams(lp);
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(o.getHeading());
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
        if(!progressDialog.isShowing()){
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

}
