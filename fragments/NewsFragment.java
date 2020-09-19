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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.NewsView;
import com.basusingh.nsspgdav.adapter.adapter_events;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.basusingh.nsspgdav.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class NewsFragment extends Fragment {


    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<ObjectEvents> list;
    adapter_events adapter;
    RelativeLayout error_layout;
    TextView error_text;
    String tag_str_req;
    TableController_Event tableController_event;
    FloatingActionButton fab_refresh;
    Button btn_reload;
    boolean recyclerClickAdded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_news, parent, false);

        fab_refresh = (FloatingActionButton) v.findViewById(R.id.fab_refresh);
        error_layout = (RelativeLayout) v.findViewById(R.id.error_layout);
        error_text = (TextView) v.findViewById(R.id.error_text);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        tableController_event = new TableController_Event(getContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setScaleAnimationVisible(fab_refresh);
                fab_refresh.setVisibility(View.VISIBLE);
            }
        }, 300);

        getDatabaseContent();

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScaleAnimationGone(fab_refresh);
                fab_refresh.setVisibility(View.GONE);
                fetchOnlineContent();
            }
        });

        btn_reload = (Button) v.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScaleAnimationGone(fab_refresh);
                fab_refresh.setVisibility(View.GONE);
                fetchOnlineContent();
            }
        });

        return v;
    }


    public void getDatabaseContent(){
        progressBar.setVisibility(View.VISIBLE);
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try{
                        list = tableController_event.getAllEvent();
                        if(!list.isEmpty()){
                            adapter = new adapter_events(list, getContext());
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    try{
                        progressBar.setVisibility(View.GONE);
                        if(!list.isEmpty()){
                            recyclerView.setAdapter(adapter);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            });
                            if(!recyclerClickAdded){
                                recyclerClickAdded = true;
                                setUpRecyclerItemClick();
                            }
                        } else {
                            setErrorLayoutVisible("No events found.");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        setErrorLayoutVisible("An error occurred.");
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred.");
        }
    }

    public void fetchOnlineContent(){
        progressBar.setVisibility(View.VISIBLE);
        try{
            error_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            list.clear();
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            tag_str_req = "event_search_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "newssearch", new Response.Listener<String>() {

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
                            ObjectEvents on = new ObjectEvents();
                            on.setId(o.getString("id"));
                            on.setHeading(o.getString("heading"));
                            on.setDescription(o.getString("description"));
                            on.setTimeStamp(o.getString("time_stamp"));
                            on.setImageUrl(o.getString("imageurl"));
                            on.setType(o.getString("type"));
                            list.add(on);
                        }

                        tableController_event.addList(list);

                        if(!list.isEmpty()){
                            adapter = new adapter_events(list, getContext());
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

                                    setScaleAnimationVisible(fab_refresh);
                                    fab_refresh.setVisibility(View.VISIBLE);
                                    if(!recyclerClickAdded){
                                        recyclerClickAdded = true;
                                        setUpRecyclerItemClick();
                                    }
                                }
                            });
                        } else {
                            setErrorLayoutVisible("No events found.");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        setErrorLayoutVisible("Sorry an error occurred!");
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred!");
        }
    }


    public void setUpRecyclerItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectEvents o = list.get(position);
                Intent i = new Intent(getContext(), NewsView.class);
                i.putExtra("id", o.getId());
                i.putExtra("heading", o.getHeading());
                i.putExtra("description", o.getDescription());
                i.putExtra("time_stamp", o.getTimeStamp());
                i.putExtra("type", o.getType());
                i.putExtra("imageurl", o.getImageUrl());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                    error_layout.setVisibility(View.VISIBLE);
                    error_text.setText(msg);
                    progressBar.setVisibility(View.GONE);

                    setScaleAnimationVisible(fab_refresh);
                    fab_refresh.setVisibility(View.VISIBLE);
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
        private NewsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final NewsFragment.ClickListener clickListener) {
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

    private void setScaleAnimationGone(View view) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
