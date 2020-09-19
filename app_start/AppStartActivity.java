package com.basusingh.nsspgdav.app_start;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.MainActivity;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.UserPreference;

import static com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EFFECT_DEFAULT;
import static com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EXIT_HIGH;
import static com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EXIT_MEDIUM;
import static com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EXIT_SLOW;


public class AppStartActivity extends AppCompatActivity{

    ViewPager viewPager;
    TextView btn_done, btn_skip;
    ImageView btn_next;
    private LinearLayout dotsLayout;
    UserPreference appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_app_start);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btn_done = (TextView) findViewById(R.id.btn_done);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        appData = new UserPreference(getApplicationContext());

        viewPager.setAdapter(new PageAdapters(getSupportFragmentManager(), 4));

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextActivity();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeNextPage();
            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextActivity();
            }
        });

        askForPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_PHONE_STATE});

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                switch (position){
                    case 0:
                    case 1:
                    case 2:
                        if(btn_done.getVisibility() == View.VISIBLE){
                            hideDoneButton();
                        }
                        break;
                    case 3:
                        showDoneButton();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addBottomDots(0);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setUpPageAnimation();
            }
        });
    }

    public void setUpPageAnimation(){
        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page11, -0.99f, 18))
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page21, -0.99f, 18))
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page31, -0.99f, 18))
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page41, -0.99f, 18))

                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page12, -0.65f, 8))

                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page22, -0.30f, 3))
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page32, -0.30f, 3))
                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page42, -0.30f, 3))

                .addViewToParallax(new com.basusingh.nsspgdav.app_start.ParallaxPageTransformer.ParallaxTransformInformation(R.id.page13, -0.30f, 3));

        viewPager.setPageTransformer(true, pageTransformer);
    }

    public void swipeNextPage(){
        int currentpost = viewPager.getCurrentItem();
        if(currentpost != 3){
            currentpost++;
            viewPager.setCurrentItem(currentpost, true);
            if(currentpost == 3){
                showDoneButton();
            }
        } else {
            showDoneButton();
        }
    }

    private void loadNextActivity() {
        new UserPreference(getApplicationContext()).setNotificationPref(true);
        appData.setFirstRunDone();
        if (!new UserPreference(getApplicationContext()).isUserRegistered()) {
            startActivity(new Intent(getApplicationContext(), LoginUser.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else if (!appData.isFirstDataLoaded()) {
            startActivity(new Intent(getApplicationContext(), FcmAndLoadFirstData.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    public void showDoneButton(){
        btn_next.setVisibility(View.GONE);
        btn_skip.setText("      ");
        btn_skip.setClickable(false);
        btn_done.setVisibility(View.VISIBLE);
    }

    public void hideDoneButton(){
        btn_skip.setText("SKIP");
        btn_skip.setClickable(true);
        btn_done.setVisibility(View.GONE);
        btn_next.setVisibility(View.VISIBLE);
        btn_skip.setVisibility(View.VISIBLE);
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[4];

        int[] colorsActive = getResources().getIntArray(R.array.app_start_array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.app_start_array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getApplicationContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(28);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


    public void askForPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                   if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                       Toast.makeText(this,"Permission granted for this app",Toast.LENGTH_LONG).show();
                   }
               }
                break;


        }
    }


    class PageAdapters extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PageAdapters(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentSlide.newInstance(R.layout.app_intro);
                case 1:
                    return FragmentSlide.newInstance(R.layout.app_intro2);
                case 2:
                    return FragmentSlide.newInstance(R.layout.app_intro3);
                case 3:
                    return FragmentSlide.newInstance(R.layout.app_intro4);

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

    }


    @Override
    public void onBackPressed(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        return true;
    }

}


