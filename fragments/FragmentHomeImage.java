package com.basusingh.nsspgdav.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.helper.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by Basu Singh on 10/6/2016.
 */
public class FragmentHomeImage extends Fragment {

    ImageView image;
    String count;
    AppData appData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savesInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_image, parent, false);

        image = (ImageView) v.findViewById(R.id.image);
        count = getArguments().getString("number");
        appData = new AppData(getContext());


       try {
           if (count != null) {
               switch (count) {
                   case "0":
                       Picasso.with(getContext()).load(appData.getImage1()).error(getResources().getDrawable(R.drawable.error_picasso)).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
                       break;
                   case "1":
                       Picasso.with(getContext()).load(appData.getImage2()).error(getResources().getDrawable(R.drawable.error_picasso)).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
                       break;
                   case "2":
                       Picasso.with(getContext()).load(appData.getImage3()).error(getResources().getDrawable(R.drawable.error_picasso)).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
                       break;
                   case "3":
                       Picasso.with(getContext()).load(appData.getImage4()).error(getResources().getDrawable(R.drawable.error_picasso)).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
                       break;
                   case "4":
                       Picasso.with(getContext()).load(appData.getImage5()).error(getResources().getDrawable(R.drawable.error_picasso)).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
                       break;
               }
           }
       } catch (Exception e){
           e.printStackTrace();
       }
        return v;

    }
}
