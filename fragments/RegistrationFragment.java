package com.basusingh.nsspgdav.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.CompletedRegistration;
import com.basusingh.nsspgdav.activity.RegistrationForm;
import com.basusingh.nsspgdav.adapter.adapter_registration;
import com.basusingh.nsspgdav.database_preference.TableController_Registration;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectRegistration;
import com.basusingh.nsspgdav.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class RegistrationFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    RelativeLayout error_layout;
    TextView error_text;
    String tag_str_req;
    List<ObjectRegistration> list;
    adapter_registration adapter;
    TableController_Registration tableController_registration;
    FloatingActionButton fab_completed_registration;
    Button btn_reload;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v  = inflater.inflate(R.layout.fragment_registration, parent, false);

        tableController_registration = new TableController_Registration(getContext());
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        error_layout = (RelativeLayout) v.findViewById(R.id.error_layout);
        error_text = (TextView) v.findViewById(R.id.error_text);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        fab_completed_registration = (FloatingActionButton) v.findViewById(R.id.fab_completed_registration);

        fab_completed_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CompletedRegistration.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setScaleAnimationVisible(fab_completed_registration);
                fab_completed_registration.setVisibility(View.VISIBLE);
            }
        }, 300);
        list = new ArrayList<>();

        fetchContent();

        btn_reload = (Button) v.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_layout.setVisibility(View.GONE);
                fetchContent();
            }
        });

        return v;
    }


    public void fetchContent(){
        progressBar.setVisibility(View.VISIBLE);
        try{
            tag_str_req = "registration_search_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "registrationform", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_search_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    setErrorLayoutVisible("The Internet is down.");
                }
            });
            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred!");
        }
    }


    public void check_search_response(String response){
        String error, message;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");
            message = jsonObj.getString("message");

            if(error.equals("true")){
                setErrorLayoutVisible("An error occurred!");
            } else if(error.equals("false")) {
                add_data(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            setErrorLayoutVisible("An error occurred!");

        }
    }


    public void add_data(final String data){
        try{

            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try{
                        JSONArray a = new JSONArray(data);
                        for(int i = 0; i<a.length(); i++){
                            JSONObject o = a.getJSONObject(i);
                            ObjectRegistration on = new ObjectRegistration();
                            on.setId(o.getString("id"));
                            on.setHeading(o.getString("heading"));
                            on.setDeadline(o.getString("deadline"));
                            on.setImageurl(o.getString("imageurl"));
                            on.setAddress(o.getBoolean("address"));
                            on.setBlood_group(o.getBoolean("blood_group"));
                            on.setCourse(o.getBoolean("course"));
                            on.setDob(o.getBoolean("dob"));
                            on.setEmail(o.getBoolean("email"));
                            on.setInter_college_name(o.getBoolean("inter_college_name"));
                            on.setMedical_issue(o.getBoolean("medical_issue"));
                            on.setMobile(o.getBoolean("mobile"));
                            on.setName(o.getBoolean("name"));
                            on.setNew_ideas(o.getBoolean("new_ideas"));
                            on.setOrganic_medicine(o.getBoolean("organic_medicine"));
                            on.setPast_exp(o.getBoolean("past_exp"));
                            on.setYear(o.getBoolean("year"));
                            list.add(on);
                        }
                        if(!list.isEmpty()){
                            adapter = new adapter_registration(list, getContext());
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    try{
                        if(!list.isEmpty()){
                            recyclerView.setAdapter(adapter);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    error_layout.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    setUpRecyclerItemClick();
                                }
                            });
                        } else {
                            setErrorLayoutVisible("No registration option available at this time.");
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                        setErrorLayoutVisible("Sorry an error occurred!");
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred!");
        }
    }


    public void setUpRecyclerItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final ObjectRegistration o = list.get(position);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Date today = Calendar.getInstance().getTime();
                            String mToday = format.format(Calendar.getInstance().getTime());
                            Date date = format.parse(o.getDeadline());

                            if(tableController_registration.isExist(o.getId())){
                                Toast.makeText(getContext(), "You have already completed this registration!", Toast.LENGTH_SHORT).show();
                            } else if(o.getDeadline().equals(mToday)){
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", o);
                                startActivity(new Intent(getContext(), RegistrationForm.class).putExtras(bundle));
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } else if(today.after(date)){
                                Toast.makeText(getContext(), "Event already completed", Toast.LENGTH_SHORT).show();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", o);
                                startActivity(new Intent(getContext(), RegistrationForm.class).putExtras(bundle));
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }





    public void setErrorLayoutVisible(final String msg){
        try{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    error_layout.setVisibility(View.VISIBLE);
                    error_text.setText(msg);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RegistrationFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RegistrationFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    private void setScaleAnimationVisible(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(100);
        view.startAnimation(anim);
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
}
