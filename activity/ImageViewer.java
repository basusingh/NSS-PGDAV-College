package com.basusingh.nsspgdav.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.basusingh.nsspgdav.R;
import com.squareup.picasso.Picasso;

public class ImageViewer extends AppCompatActivity {

    String type, data;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        Intent i = getIntent();
        if(i != null){
            type = i.getStringExtra("type");
            data = i.getStringExtra("data");
        }
        image = (ImageView) findViewById(R.id.image);
        loadData();

    }


    public void loadData(){
        switch (type){
            case "core":
                switch (data){
                    case "1":
                        image.setImageResource(R.drawable.president_large);
                        break;
                    case "2":
                        image.setImageResource(R.drawable.vice_president_large);
                        break;
                    case "3":
                        image.setImageResource(R.drawable.secretary_large);
                        break;
                    case "4":
                        image.setImageResource(R.drawable.joint_secretary_large);
                        break;
                    case "5":
                        image.setImageResource(R.drawable.treasurer_large);
                        break;
                }
                break;

            case "office":
                try{
                    Picasso.with(getApplicationContext()).load(data).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(image);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;


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
