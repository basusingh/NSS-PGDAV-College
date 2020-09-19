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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class adapter_registration extends RecyclerView.Adapter<adapter_registration.ViewHolder> {

    List<ObjectRegistration> list;
    Context context;

    public adapter_registration(List<ObjectRegistration> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }


    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectRegistration o = list.get(position);
        if(new TableController_Registration(context).isExist(o.getId())){
            holder.registered.setVisibility(View.VISIBLE);
        } else {
            holder.registered.setVisibility(View.GONE);
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date today = Calendar.getInstance().getTime();
            String mToday = format.format(Calendar.getInstance().getTime());
            Date date = format.parse(o.getDeadline());

            if(o.getDeadline().equals(mToday)){
                holder.completed.setVisibility(View.VISIBLE);
                holder.completed.setText("Deadline Today");
            } else if(today.after(date)){
                holder.completed.setVisibility(View.VISIBLE);
                holder.completed.setText("Completed");
            } else {
                holder.completed.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.deadline.setText(o.getDeadline());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);

        return new ViewHolder(v);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView heading, deadline;
        ImageView image;
        CardView cardLayout;
        TextView completed;
        TextView registered;

        public ViewHolder(final View v){
            super(v);

            heading = (TextView) v.findViewById(R.id.heading);
            deadline = (TextView) v.findViewById(R.id.deadline);
            image = (ImageView) v.findViewById(R.id.image);
            cardLayout = (CardView) v.findViewById(R.id.cardLayout);
            completed = (TextView) v.findViewById(R.id.deadline_completed);
            registered = (TextView) v.findViewById(R.id.registered);
        }
    }
}
