package com.basusingh.nsspgdav.NavDrawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.database_preference.UserPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class NavDrawer extends Fragment {

    ImageView facebook_image, twitter_image, email_image;
    RelativeLayout notification_layout;
    SwitchCompat notification_switch;
    RecyclerView recyclerView;
    private FragmentDrawerListener drawerListener;
    NavigationDrawerAdapter adapter;
    private static int[] Icons = {
            R.drawable.home_icon_white,
            R.drawable.event_icon_white,
            R.drawable.registration_icon_white,
            R.drawable.blog_icon_white,
            R.drawable.people_icon_white,
            R.drawable.about_icon_white
    };
    private static String [] titles = null;
    private static final String TAG_HOME = "home";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_REGISTRATION = "registration";
    public static final String TAG_BLOG = "blog";
    private static final String TAG_HELP = "help";
    private static final String TAG_ABOUT = "about";

    private View containerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    UserPreference userPreference;


    public NavDrawer(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItems> getData() {
        List<NavDrawerItems> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItems navItem = new NavDrawerItems();
            navItem.setTitle(titles[i]);
            navItem.setIcon(Icons[i]);
            data.add(navItem);
        }
        return data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View v = inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        facebook_image = (ImageView) v.findViewById(R.id.facebook_image);
        twitter_image = (ImageView) v.findViewById(R.id.twitter_image);
        email_image = (ImageView) v.findViewById(R.id.email_image);
        notification_layout = (RelativeLayout) v.findViewById(R.id.notification_layout);
        notification_switch = (SwitchCompat) v.findViewById(R.id.notification_switch);
        userPreference = new UserPreference(getContext());

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if(recyclerView!=null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new NavigationDrawerAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String mTag;
                switch (position){
                    case 0:
                        mTag = TAG_HOME;
                        break;
                    case 1:
                        mTag = TAG_EVENTS;
                        break;
                    case 2:
                        mTag = TAG_REGISTRATION;
                        break;
                    case 3:
                        mTag = TAG_BLOG;
                        break;
                    case 4:
                        mTag = TAG_HELP;
                        break;
                    case 5:
                        mTag = TAG_ABOUT;
                        break;
                    default:
                        mTag = TAG_HOME;
                        break;
                }
                drawerListener.onDrawerItemSelected(view, position, mTag);
                mDrawerLayout.closeDrawer(containerView);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        setUpClicks();

        return v;
    }

    public void setUpClicks(){
        if(userPreference.getNotificationPref()){
            notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);
        }

        notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification_switch.isChecked()){
                    notification_switch.setChecked(false);
                    userPreference.setNotificationPref(false);
                } else {
                    notification_switch.setChecked(true);
                    userPreference.setNotificationPref(true);
                }
            }
        });

        facebook_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/NssPgdavCollege"));
                    startActivity(browserIntent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        twitter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/nsspgdav"));
                    startActivity(browserIntent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        email_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "office@nsspgdavcollege.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send email"));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setNavItemPostion(int position){
        adapter.selectedPosition = position;
        adapter.notifyDataSetChanged();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public void closeAllDrawer(){
        mDrawerLayout.closeDrawers();
    }

    public boolean isDrawerOpen(){
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }



    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position, String tag);
    }

}
