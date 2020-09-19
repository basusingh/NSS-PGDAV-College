package com.basusingh.nsspgdav.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.database_preference.TableController_Registration;
import com.basusingh.nsspgdav.helper.ObjectRegistration;
import com.basusingh.nsspgdav.helper.ObjectRegistrationCompleted;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Basu Singh on 10/12/2016.
 */
public class adapter_registration_completed extends RecyclerView.Adapter<adapter_registration_completed.ViewHolder> {

    List<ObjectRegistrationCompleted> list;
    Context context;

    public adapter_registration_completed(List<ObjectRegistrationCompleted> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }


    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectRegistrationCompleted o = list.get(position);
        holder.time_stamp.setText(o.getTime_stamp());
        holder.heading.setText(o.getHeading());

        try{
            Picasso.with(context).load(o.getImageurl()).placeholder(context.getResources().getDrawable(R.drawable.loading_picasso)).error(context.getResources().getDrawable(R.drawable.error_picasso)).into(holder.image);
        } catch (Exception e){
            e.printStackTrace();
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_picasso));
        }
        Animation slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        holder.cardLayout.startAnimation(slide_up);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration_completed, parent, false);

        return new ViewHolder(v);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView heading, time_stamp;
        ImageView image;
        CardView cardLayout;

        public ViewHolder(final View v){
            super(v);

            heading = (TextView) v.findViewById(R.id.heading);
            time_stamp = (TextView) v.findViewById(R.id.time_stamp);
            image = (ImageView) v.findViewById(R.id.image);
            cardLayout = (CardView) v.findViewById(R.id.cardLayout);
        }
    }
}
