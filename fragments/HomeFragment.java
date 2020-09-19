package com.basusingh.nsspgdav.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.DailyThought;
import com.basusingh.nsspgdav.activity.NewsView;
import com.basusingh.nsspgdav.adapter.adapter_events;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.helper.ObjectEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class HomeFragment extends Fragment {


    private LinearLayout dotsLayout;
    private Handler handler;
    private int delay = 5000;
    private int page = 0;
    PageAdapters pagerAdapter;
    ViewPager viewPager;
    RecyclerView recyclerView;
    TextView error_text;
    TextView thought;
    List<ObjectEvents> list;
    adapter_events adapter;
    TableController_Event tableController_event;
    Runnable runnable = new Runnable() {
        public void run() {
            if (pagerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savesInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        handler = new Handler();
        list = new ArrayList<>();
        tableController_event = new TableController_Event(getContext());

        error_text = (TextView) v.findViewById(R.id.error_text);
        thought = (TextView) v.findViewById(R.id.thought);
        dotsLayout = (LinearLayout) v.findViewById(R.id.layoutDots);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        pagerAdapter = new PageAdapters(getActivity().getSupportFragmentManager(), 5);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        setUpPage();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setUpRecyclerView();
            }
        });

        thought.setText(new AppData(getContext()).getThought());

        v.findViewById(R.id.thought_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DailyThought.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        v.findViewById(R.id.thought_layout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Thought of the Day", thought.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return v;
    }

    public void setUpPage(){
        viewPager.setAdapter(pagerAdapter);
        addBottomDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setUpRecyclerView(){

       try{
           new AsyncTask<String, String, String>(){
               @Override
               protected String doInBackground(String... params){
                   list = tableController_event.getTopEvent();
                   if(!list.isEmpty()){
                       adapter = new adapter_events(list, getContext());
                   }
                   return null;
               }
               @Override
               public void onPostExecute(String data){

                  try{
                      if(!list.isEmpty()){
                          recyclerView.setAdapter(adapter);
                          new Handler().post(new Runnable() {
                              @Override
                              public void run() {
                                  recyclerView.setVisibility(View.VISIBLE);
                              }
                          });
                          setUpRecyclerItemClick();
                      } else {
                          recyclerView.setVisibility(View.GONE);
                          error_text.setVisibility(View.VISIBLE);
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

    public void setUpRecyclerItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectEvents o = list.get(position);
                Intent i = new Intent(getContext(), NewsView.class);
                i.putExtra("id", o.getId());
                i.putExtra("heading", o.getHeading());
                i.putExtra("description", o.getDescription());
                i.putExtra("time_stamp", o.getTimeStamp());
                i.putExtra("type", o.getType());
                i.putExtra("imageurl", o.getImageUrl());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[5];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(28);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }



    class PageAdapters extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PageAdapters(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle ar = new Bundle();
            FragmentHomeImage fragment = new FragmentHomeImage();

            switch (position) {
                case 0:

                    ar.putString("number", "0");
                    fragment.setArguments(ar);
                    return fragment;

                case 1:
                    ar.putString("number", "1");
                    fragment.setArguments(ar);
                    return fragment;

                case 2:

                    ar.putString("number", "2");
                    fragment.setArguments(ar);
                    return fragment;

                case 3:

                    ar.putString("number", "3");
                    fragment.setArguments(ar);
                    return fragment;

                case 4:

                    ar.putString("number", "4");
                    fragment.setArguments(ar);
                    return fragment;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
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


}
