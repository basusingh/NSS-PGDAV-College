package com.basusingh.nsspgdav.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.adapter.adapter_registration_completed;
import com.basusingh.nsspgdav.database_preference.TableController_Registration;
import com.basusingh.nsspgdav.helper.ObjectRegistrationCompleted;

import java.util.ArrayList;
import java.util.List;

public class CompletedRegistration extends AppCompatActivity {


    RecyclerView recyclerView;
    RelativeLayout error_layout;
    TextView error_text;
    List<ObjectRegistrationCompleted> list;
    adapter_registration_completed adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        error_layout = (RelativeLayout) findViewById(R.id.error_layout);
        error_text = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        fetchData();
    }


    public void fetchData(){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    list = new TableController_Registration(getApplicationContext()).getAllList();
                    if(!list.isEmpty()){
                        adapter = new adapter_registration_completed(list, getApplicationContext());
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    if(!list.isEmpty()){
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                error_layout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    } else {
                        setErrorLayoutVisible("You haven't completed any registration yet.");
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred.");
        }
    }

    public void setErrorLayoutVisible(String msg){
        recyclerView.setVisibility(View.GONE);
        error_layout.setVisibility(View.VISIBLE);
        error_text.setText(msg);
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
