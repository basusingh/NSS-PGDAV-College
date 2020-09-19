package com.basusingh.nsspgdav.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.basusingh.nsspgdav.NavDrawer.NavDrawer;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.app_start.LoginUser;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.fragments.AboutFragment;
import com.basusingh.nsspgdav.fragments.BlogFragment;
import com.basusingh.nsspgdav.fragments.NewsFragment;
import com.basusingh.nsspgdav.fragments.TeamMemberFragment;
import com.basusingh.nsspgdav.fragments.HomeFragment;
import com.basusingh.nsspgdav.fragments.RegistrationFragment;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.utils.AppDataService;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavDrawer.FragmentDrawerListener {

    private NavDrawer drawerFragment;
    private Handler mHandler;
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;
    FloatingActionMenu fab_menu;
    TextView toolbar_title;
    private String [] titles = null;
    com.github.clans.fab.FloatingActionButton fab_feedback, fab_blood_group_search;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    if(! new UserPreference(getApplicationContext()).isUserRegistered()){
            startActivity(new Intent(getApplicationContext(), LoginUser.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        titles = getResources().getStringArray(R.array.nav_drawer_labels);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        fab_menu = (FloatingActionMenu) findViewById(R.id.menu);
        fab_feedback = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_feedback);
        fab_blood_group_search = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_blood_group_search);
        mHandler = new Handler();
        drawerFragment = (NavDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        if (savedInstanceState == null) {
            Intent i = getIntent();
            if (i != null) {
                try {
                    if (i.getStringExtra("page").equals("2")) {
                        navItemIndex = 2;
                        CURRENT_TAG = "registration";
                        loadHomeFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    loadHomeFragment();
                }
            } else {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }
        }

        fab_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        fab_menu.close(true);
                    }
                });
                startActivity(new Intent(getApplicationContext(), Feedback.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        fab_blood_group_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        fab_menu.close(true);
                    }
                });
                startActivity(new Intent(getApplicationContext(), BloodGroupSearch.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        findViewById(R.id.menu_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        fab_menu.close(true);
                    }
                });
                startActivity(new Intent(getApplicationContext(), Help.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                 if (intent.getAction().equals(Constants.LBM_POPUP_FESTIVAL)) {
                    showPopupDialog(intent.getStringExtra("heading"), intent.getStringExtra("description"), intent.getStringExtra("imageurl"));

                }
            }
        };


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    startService(new Intent(getApplicationContext(), AppDataService.class));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 800);

    }

    public void showPopupDialog(String mHeading, String mDescription, String imageurl){
        try{
            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            final View view = li.inflate(R.layout.layout_popup_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);
            alertDialogBuilder.setView(view);

            TextView heading = (TextView) view.findViewById(R.id.heading);
            TextView description = (TextView) view.findViewById(R.id.description);
            ImageView image = (ImageView) view.findViewById(R.id.imaage);
            heading.setText(mHeading);
            description.setText(mDescription);
            try{
                Picasso.with(getApplicationContext()).load(imageurl).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(image);
            } catch (Exception e){
                e.printStackTrace();
            }

            AlertDialog alertDialog = alertDialogBuilder.create();
            setScaleAnimationVisible(view);
            alertDialog.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setToolbarTitle(){
        toolbar_title.setText(titles[navItemIndex]);
    }

    @Override
    public void onDrawerItemSelected(View view, int position, String tag) {
        navItemIndex = position;
        CURRENT_TAG = tag;
        loadHomeFragment();
    }

    private void loadHomeFragment() {

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerFragment.closeAllDrawer();
            return;
        }

        setToolbarTitle();
        invalidateFab();

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.container, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.postDelayed(mPendingRunnable, 250);
        }

        drawerFragment.setNavItemPostion(navItemIndex);

        drawerFragment.closeAllDrawer();

    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new NewsFragment();
            case 2:
                return new RegistrationFragment();
            case 3:
                return new BlogFragment();
            case 4:
                return new TeamMemberFragment();
            case 5:
                return new AboutFragment();
            default:
                return new HomeFragment();
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        setToolbarTitle();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.LBM_POPUP_FESTIVAL));

    }

    @Override
    public void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        if(fab_menu.isOpened()){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    fab_menu.close(true);
                }
            });
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void invalidateFab(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(navItemIndex == 0){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            setScaleAnimationVisible(fab_menu);
                            fab_menu.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    if(fab_menu.getVisibility() == View.VISIBLE){
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                fab_menu.close(true);
                                setScaleAnimationGone(fab_menu);
                                fab_menu.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        }, 300);
    }

    private void setScaleAnimationVisible(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    private void setScaleAnimationGone(View view) {
        ScaleAnimation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
