package com.basusingh.nsspgdav.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.adapter.DividerItemDecorator;
import com.basusingh.nsspgdav.adapter.adapter_daily_thought;
import com.basusingh.nsspgdav.adapter.adapter_registration_completed;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_DailyThought;
import com.basusingh.nsspgdav.database_preference.TableController_Registration;
import com.basusingh.nsspgdav.helper.ObjectDailyThought;

import java.util.ArrayList;
import java.util.List;

public class DailyThought extends AppCompatActivity {


    RecyclerView recyclerView;
    CardView error_layout;
    TextView error_text;
    List<ObjectDailyThought> list;
    adapter_daily_thought adapter;
    CardView cardRecyclerLayout;
    TextView thought, time_stamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_thought);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        time_stamp = (TextView) findViewById(R.id.time_stamp);
        thought = (TextView) findViewById(R.id.thought);
        cardRecyclerLayout = (CardView) findViewById(R.id.cardRecyclerLayout);
        error_layout = (CardView) findViewById(R.id.error_layout);
        error_text = (TextView) findViewById(R.id.error_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorator(DailyThought.this, LinearLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);

        list = new ArrayList<>();

        thought.setText(new AppData(getApplicationContext()).getThought());
        time_stamp.setText(new AppData(getApplicationContext()).getThoughtTimeStamp());

        fetchData();


        findViewById(R.id.thought_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showThoughtDialog(thought.getText().toString());
            }
        });

        findViewById(R.id.thought_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Thought of the Day", thought.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }


    public void fetchData(){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    list = new TableController_DailyThought(getApplicationContext()).getAllList();
                    if(!list.isEmpty()){
                        adapter = new adapter_daily_thought(list, getApplicationContext());
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
                                cardRecyclerLayout.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(adapter);
                                setUpRecyclerClick();
                            }
                        });
                    } else {
                        setErrorLayoutVisible("We couldn't find any thought.");
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred.");
        }
    }


    public void setUpRecyclerClick(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectDailyThought o = list.get(position);
                showThoughtDialog(o.getThought());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void showThoughtDialog(final String thought){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyThought.this);
        alertDialogBuilder.setMessage("Please choose a option");

        alertDialogBuilder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Thought of the Day", thought);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialogBuilder.setNegativeButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, thought);
                startActivity(Intent.createChooser(intent, "Share via"));
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
        private DailyThought.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DailyThought.ClickListener clickListener) {
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


    public void setErrorLayoutVisible(String msg){
        cardRecyclerLayout.setVisibility(View.GONE);
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
