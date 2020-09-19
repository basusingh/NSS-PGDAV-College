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
import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Basu Singh on 10/8/2016.
 */
public class adapter_events extends RecyclerView.Adapter<adapter_events.ViewHolder> {

    List<ObjectEvents> list;
    Context context;
    private int lastPosition = -1;

    public adapter_events(List<ObjectEvents> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectEvents o = list.get(position);
        holder.date.setText(o.getTimeStamp());
        holder.description.setText(o.getDescription());
        holder.heading.setText(o.getHeading());
        try{
            Picasso.with(context).load(o.getImageUrl()).placeholder(context.getResources().getDrawable(R.drawable.loading_picasso)).error(context.getResources().getDrawable(R.drawable.error_picasso)).into(holder.image);
        } catch (Exception e){
            e.printStackTrace();
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_picasso));
        }
        Animation slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        holder.cardLayout.startAnimation(slide_up);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView heading, description, date;
        CardView cardLayout;

        public ViewHolder(final View item){
            super(item);
            heading = (TextView) item.findViewById(R.id.heading);
            description = (TextView) item.findViewById(R.id.description);
            date = (TextView) item.findViewById(R.id.date);
            image = (ImageView) item.findViewById(R.id.image);
            cardLayout = (CardView) item.findViewById(R.id.cardLayout);
        }
    }
}
