package com.basusingh.nsspgdav.app_start;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.basusingh.nsspgdav.R;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class FragmentSlide extends Fragment{

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    LinearLayout layoutContainer;
    int pageCount;

    public static FragmentSlide newInstance(int layoutResId) {
        FragmentSlide sampleSlide = new FragmentSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    private int layoutResId;

    public FragmentSlide() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(layoutResId, container, false);

        layoutContainer = (LinearLayout) v.findViewById(R.id.layout);

        return v;
    }


}
