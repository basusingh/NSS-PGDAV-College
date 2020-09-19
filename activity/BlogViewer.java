package com.basusingh.nsspgdav.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.ObjectBlog;
import com.squareup.picasso.Picasso;

public class BlogViewer extends AppCompatActivity {

    ObjectBlog o;
    ImageView toolbar_image;
    LinearLayout optional_image_layout;
    ImageView image1, image2;
    TextView heading, description, sub_description, time_stamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_blog_viewer);

        Intent i = getIntent();
        if(i != null){
            Bundle bundle = i.getExtras();
            o = (ObjectBlog)bundle.getSerializable("data");
        }

        setUpToolbar();

        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        heading = (TextView) findViewById(R.id.heading);
        description = (TextView) findViewById(R.id.description);
        sub_description = (TextView) findViewById(R.id.sub_description);
        time_stamp = (TextView) findViewById(R.id.time_stamp);

        optional_image_layout = (LinearLayout) findViewById(R.id.optional_image_layout);
        try{
            Picasso.with(getApplicationContext()).load(o.getImageurl()).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(toolbar_image);
        } catch (Exception e){
            e.printStackTrace();
        }

        setUpLayout();

    }


    public void setUpLayout(){
        if(o.getImage1().equals("null") && o.getImage2().equals("null")){
            optional_image_layout.setVisibility(View.GONE);
        } else {
            if(o.getImage1().equals("null")){
                image1.setVisibility(View.GONE);
            } else {
                try{
                    Picasso.with(getApplicationContext()).load(o.getImage1()).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(image1);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(o.getImage2().equals("null")){
                image2.setVisibility(View.GONE);
            } else {
                try{
                    Picasso.with(getApplicationContext()).load(o.getImage2()).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(image2);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if(o.getSub_description().equals("null")){
            sub_description.setVisibility(View.GONE);
        } else {
            sub_description.setText(o.getSub_description());
        }

        heading.setText(o.getHeading());
        description.setText(o.getDescription());
        time_stamp.setText(o.getTime_stamp());
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
