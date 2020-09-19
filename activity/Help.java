package com.basusingh.nsspgdav.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.adapter.DividerItemDecorator;
import com.basusingh.nsspgdav.adapter.adapter_help;
import com.basusingh.nsspgdav.helper.ObjectHelp;

import java.util.ArrayList;
import java.util.List;

public class Help extends AppCompatActivity {

    RecyclerView recyclerView;
    adapter_help adapter;
    List<ObjectHelp> list;
    String[] help_heading = null;
    String[] help_description = null;
    LinearLayout feedback_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        help_heading = getResources().getStringArray(R.array.help_heading);
        help_description = getResources().getStringArray(R.array.help_description);

        feedback_layout = (LinearLayout) findViewById(R.id.feedback_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorator(Help.this, LinearLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        list = new ArrayList<>();

        loadData();


        feedback_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Feedback.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


    }




    public void loadData(){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    for(int i = 0; i<help_heading.length; i++){
                        ObjectHelp o = new ObjectHelp();
                        o.setHeading(help_heading[i]);
                        o.setDescription(help_description[i]);
                        list.add(o);
                    }
                    if(!list.isEmpty()){
                        adapter = new adapter_help(list, getApplicationContext());
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    if(!list.isEmpty()){
                        recyclerView.setAdapter(adapter);
                        setUpRecyclerItemTouch();
                    }
                }
            }.execute();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setUpRecyclerItemTouch(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectHelp o = list.get(position);
                showDetailDialog(o.getHeading(), o.getDescription());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void showDetailDialog(final String mHeading, final String mDescription){
        LayoutInflater li = LayoutInflater.from(Help.this);
        final View view = li.inflate(R.layout.item_help_detail, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Help.this);
        alertDialogBuilder.setView(view);

        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView description = (TextView) view.findViewById(R.id.description);
        heading.setText(mHeading);
        description.setText(mDescription);

        alertDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        setScaleAnimationVisible(view);
        alertDialog.show();

    }

    private void setScaleAnimationVisible(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Help.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Help.ClickListener clickListener) {
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
