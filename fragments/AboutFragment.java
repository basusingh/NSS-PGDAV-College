package com.basusingh.nsspgdav.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basusingh.nsspgdav.R;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savesInstanceState){
        View v = inflater.inflate(R.layout.fragment_about, parent, false);


        return v;
    }

}
