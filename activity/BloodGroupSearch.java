package com.basusingh.nsspgdav.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.basusingh.nsspgdav.adapter.adapter_blood_group_fav;
import com.basusingh.nsspgdav.database_preference.TableController_BloodGroup;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectBloodGroup;
import com.basusingh.nsspgdav.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodGroupSearch extends AppCompatActivity {

    RelativeLayout fav_frame;
    FloatingActionButton fab_favourite;

    RecyclerView recyclerView_fav, recyclerView_search;
    ProgressBar progressBar_fav, progressBar_search;
    TextView no_fav_text, error_text_search;
    TableController_BloodGroup tableController_bloodGroup;
    List<ObjectBloodGroup> mFavList, mSearchList;
    adapter_blood_group_fav adapter_fav, adapter_search;
    LinearLayout btn_search;
    RadioButton radioButton;
    String tag_str_req;
    boolean fav_item_click_added = false;
    boolean search_item_click_added = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        error_text_search = (TextView) findViewById(R.id.error_text_search);
        progressBar_search = (ProgressBar) findViewById(R.id.progressBarSearch);
        recyclerView_search = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        if(recyclerView_search != null){
            recyclerView_search.setHasFixedSize(true);
        }
        recyclerView_search.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView_search.setLayoutManager(layoutManager2);
        recyclerView_search.setNestedScrollingEnabled(false);

        no_fav_text = (TextView) findViewById(R.id.no_fav_text);
        progressBar_fav = (ProgressBar) findViewById(R.id.progressBar_fav);
        recyclerView_fav = (RecyclerView) findViewById(R.id.recyclerView_fav);
        if(recyclerView_fav != null){
            recyclerView_fav.setHasFixedSize(true);
        }
        recyclerView_fav.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView_fav.setLayoutManager(layoutManager1);

        fab_favourite = (FloatingActionButton) findViewById(R.id.fab_favourite);
        fav_frame = (RelativeLayout) findViewById(R.id.fav_frame);
        btn_search = (LinearLayout) findViewById(R.id.btn_search);

        tableController_bloodGroup = new TableController_BloodGroup(getApplicationContext());
        mFavList = new ArrayList<>();
        mSearchList = new ArrayList<>();

        setUpClick();

        LoadFavData();

        initiateSearch();

    }



    public void setUpClick(){
        try{
            fav_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
                    fav_frame.setAnimation(slide_out);
                    fav_frame.setVisibility(View.GONE);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

       try{
           fab_favourite.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(fav_frame.getVisibility() == View.VISIBLE){
                       Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
                       fav_frame.setAnimation(slide_out);
                       fav_frame.setVisibility(View.GONE);
                   } else {
                       Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                       fav_frame.setAnimation(slide_in);
                       fav_frame.setVisibility(View.VISIBLE);
                   }
               }
           });
       } catch (Exception e){
           e.printStackTrace();
       }
    }





    public void LoadFavData(){

        progressBar_fav.setVisibility(View.VISIBLE);

        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try{
                        mFavList = tableController_bloodGroup.getAllFav();
                        if(!mFavList.isEmpty()){
                            adapter_fav = new adapter_blood_group_fav(mFavList, getApplicationContext());
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    try{
                        progressBar_fav.setVisibility(View.GONE);
                        if(!mFavList.isEmpty()){
                            no_fav_text.setVisibility(View.GONE);
                            recyclerView_fav.setAdapter(adapter_fav);
                            recyclerView_fav.setVisibility(View.VISIBLE);
                            if(!fav_item_click_added){
                                fav_item_click_added = true;
                                addFavItemClick();
                            }

                        } else {
                            recyclerView_fav.setVisibility(View.GONE);
                            no_fav_text.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }




    public void addFavItemClick(){
        recyclerView_fav.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView_fav, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectBloodGroup o = mFavList.get(position);
                if(tableController_bloodGroup.isExist(o.getId())){
                    showRemoveFavItemClick(o, position);
                } else {
                    showAddFavItemClick(o);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                ObjectBloodGroup o = mFavList.get(position);
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Mobile Number", o.getMobile());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Mobile Number copied", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }



    public void initiateSearch(){
        TextView search_text = (TextView) findViewById(R.id.search_text);
        search_text.setText("Choose category");
        error_text_search.setVisibility(View.VISIBLE);
        error_text_search.setText("Please choose a Blood Group!");

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBloodGroupChooseDialog();
            }
        });
    }

    public void showBloodGroupChooseDialog(){

        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        final View view = li.inflate(R.layout.item_radiogroup_blood_group, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BloodGroupSearch.this);


        alertDialogBuilder.setView(view);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int mId) {
                                int id = radioGroup.getCheckedRadioButtonId();

                                recyclerView_search.setVisibility(View.GONE);
                                progressBar_search.setVisibility(View.GONE);

                                mSearchList.clear();

                                if(id == R.id.groupA || id == R.id.groupAB || id == R.id.groupB || id == R.id.groupO){
                                    TextView search_text = (TextView) findViewById(R.id.search_text);
                                    search_text.setText("Search");
                                    radioButton = (RadioButton) view.findViewById(id);
                                    error_text_search.setVisibility(View.GONE);
                                    btn_search.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    doSearch(radioButton.getText().toString());
                                } else {
                                    dialog.dismiss();
                                    error_text_search.setText("Please choose a Blood Group!");
                                    error_text_search.setVisibility(View.VISIBLE);
                                    setScaleAnimationZoomIn(error_text_search);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


    }

    public void doSearch(final String type){
        progressBar_search.setVisibility(View.VISIBLE);
        try{
            tag_str_req = "blood_group_search_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "bloodgroupsearch", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_search_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "There seems to be an problem with your Internet Connection.", Toast.LENGTH_LONG).show();
                    showErrorAndActivateSearch("Sorry, an error occurred!");
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", type);

                    return params;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            showErrorAndActivateSearch("Sorry, an error occurred!");
        }
    }



    public void check_search_response(String response){
        String error, message;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");
            message = jsonObj.getString("message");

            if(error.equals("true")){
                showErrorAndActivateSearch("Sorry, an error occurred!");
            } else if(error.equals("false")) {
                add_data(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            showErrorAndActivateSearch("Sorry, an error occurred!");

        }
    }


    public void add_data(final String data){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try {
                        JSONArray a = new JSONArray(data);
                        for (int i = 0; i < a.length(); i++) {
                            JSONObject o = a.getJSONObject(i);
                            ObjectBloodGroup on = new ObjectBloodGroup();
                            on.setId(o.getString("id"));
                            on.setName(o.getString("name"));
                            on.setMobile(o.getString("mobile"));
                            on.setAge(o.getString("age"));
                            on.setBloodgroup(o.getString("bloodgroup"));
                            mSearchList.add(on);
                        }
                        if(!mSearchList.isEmpty()){
                            adapter_search = new adapter_blood_group_fav(mSearchList, getApplicationContext());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                   try{
                       if(!mSearchList.isEmpty()){
                           recyclerView_search.setAdapter(adapter_search);
                           new Handler().post(new Runnable() {
                               @Override
                               public void run() {
                                   progressBar_search.setVisibility(View.GONE);
                                   error_text_search.setVisibility(View.GONE);
                                   recyclerView_search.setVisibility(View.VISIBLE);
                                   btn_search.setVisibility(View.VISIBLE);
                                   if(!search_item_click_added){
                                       search_item_click_added = true;
                                       setUpSearchItemClick();
                                   }
                               }
                           });
                       } else {
                           showErrorAndActivateSearch("No Data Found! Please try again later.");
                       }
                   } catch (Exception e){
                       e.printStackTrace();
                   }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            showErrorAndActivateSearch("Sorry an error occurred");
        }
    }





    public void showErrorAndActivateSearch(String msg){
        progressBar_search.setVisibility(View.GONE);
        error_text_search.setVisibility(View.VISIBLE);
        error_text_search.setText(msg);
        btn_search.setVisibility(View.VISIBLE);
    }

    public void setUpSearchItemClick(){
        recyclerView_search.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView_search, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectBloodGroup o = mSearchList.get(position);
                if(tableController_bloodGroup.isExist(o.getId())){
                    showRemoveFavItemClick(o, position);
                } else {
                    showAddFavItemClick(o);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                ObjectBloodGroup o = mFavList.get(position);
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Mobile Number", o.getMobile());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Mobile Number copied", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }





    public void showAddFavItemClick(final ObjectBloodGroup o){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BloodGroupSearch.this);
        alertDialogBuilder.setTitle("Choose an option");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + o.getMobile()));
                    startActivity(intent);
                } catch (SecurityException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Sorry, but this app don't have permission to call. Long press to copy number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialogBuilder.setNeutralButton("Favourite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tableController_bloodGroup.addData(o);
                if(mFavList.isEmpty()){
                    mFavList.add(o);
                    adapter_fav = new adapter_blood_group_fav(mFavList, getApplicationContext());
                    no_fav_text.setVisibility(View.GONE);
                    recyclerView_fav.setAdapter(adapter_fav);
                    recyclerView_fav.setVisibility(View.VISIBLE);
                    if(!fav_item_click_added){
                        addFavItemClick();
                        fav_item_click_added = true;
                    }
                } else {
                    mFavList.add(o);
                    adapter_fav.notifyDataSetChanged();
                }
            }
        });



        alertDialogBuilder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + o.getMobile()));
                    intent.putExtra("sms_body", "");
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void showRemoveFavItemClick(final ObjectBloodGroup o, final int pos){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BloodGroupSearch.this);
        alertDialogBuilder.setMessage("Choose an option");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + o.getMobile()));
                    startActivity(intent);
                } catch (SecurityException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Sorry, but this app don't have permission to call. Long press to copy number", Toast.LENGTH_SHORT).show();
                }
            }
        });



        alertDialogBuilder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               try{
                   tableController_bloodGroup.removeData(o.getId());
                   mFavList.remove(pos);
                   adapter_fav.notifyDataSetChanged();
                   if(mFavList.isEmpty()){
                       mFavList.clear();
                       adapter_fav = null;
                       recyclerView_fav.removeAllViews();
                       recyclerView_fav.setVisibility(View.GONE);
                       no_fav_text.setVisibility(View.VISIBLE);
                   }
               } catch (Exception e){
                   e.printStackTrace();
               }
            }
        });



        alertDialogBuilder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + o.getMobile()));
                    intent.putExtra("sms_body", "");
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private BloodGroupSearch.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BloodGroupSearch.ClickListener clickListener) {
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



    private void setScaleAnimationZoomIn(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    private void setScaleAnimationZoomOut(View view) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    @Override
    public void onBackPressed(){
        if(fav_frame.getVisibility() == View.VISIBLE){
            Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
            fav_frame.setAnimation(slide_out);
            fav_frame.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(fav_frame.getVisibility() == View.VISIBLE){
            Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
            fav_frame.setAnimation(slide_out);
            fav_frame.setVisibility(View.GONE);
            return false;
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            AppController.getInstance().cancelPendingRequests(tag_str_req);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
