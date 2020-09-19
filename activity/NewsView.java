package com.basusingh.nsspgdav.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.Constants;
import com.squareup.picasso.Picasso;

public class NewsView extends AppCompatActivity {

    ImageView toolbar_image;
    String id, heading, description, time_stamp, type, imageurl;
    TextView mHeading, mDescription, mTimeStamp;
    Button register_btn;
    LinearLayout date_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_news_view);
        Intent i = getIntent();
        if(i != null){
            id = i.getStringExtra("id");
            heading = i.getStringExtra("heading");
            description = i.getStringExtra("description");
            time_stamp = i.getStringExtra("time_stamp");
            type = i.getStringExtra("type");
            imageurl = i.getStringExtra("imageurl");
        }
        setUpToolbar();

        date_layout = (LinearLayout) findViewById(R.id.date_layout);
        register_btn = (Button) findViewById(R.id.register_btn);
        mHeading = (TextView) findViewById(R.id.heading);
        mTimeStamp = (TextView) findViewById(R.id.time_stamp);
        mDescription = (TextView) findViewById(R.id.description);

        mHeading.setText(heading);
        mDescription.setText(description);
        mTimeStamp.setText(time_stamp);

        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time_stamp);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    intent.putExtra(CalendarContract.Events.TITLE, heading);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "PG DAV College");
                    startActivity(intent);

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops, looks like you don't have Calender app to save this data to calender", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(type.equalsIgnoreCase(Constants.EVENT_TYPE_REGISTRATION)){
            register_btn.setVisibility(View.VISIBLE);
        } else if(type.equalsIgnoreCase(Constants.EVENT_TYPE_NEWS)){
            register_btn.setVisibility(View.GONE);
        }

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("page", "2");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);
        Picasso.with(getApplicationContext()).load(imageurl).into(toolbar_image);

    }


    public void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.setMargins(0, getStatusBarHeight(), 0, 0);
            toolbar.setLayoutParams(lp);
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(heading);
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
