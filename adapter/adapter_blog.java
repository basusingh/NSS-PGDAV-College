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
import com.basusingh.nsspgdav.helper.ObjectBlog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Basu Singh on 10/18/2016.
 */
public class adapter_blog extends RecyclerView.Adapter<adapter_blog.ViewHolder> {

    List<ObjectBlog> list;
    Context context;

    public adapter_blog(List<ObjectBlog> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectBlog o = list.get(position);
        holder.heading.setText(o.getHeading());
        holder.description.setText(o.getDescription());
        holder.time_stamp.setText(o.getTime_stamp());

        try{
            Picasso.with(context).load(o.getImageurl()).placeholder(R.drawable.loading_picasso).error(R.drawable.error_picasso).into(holder.image);
        } catch (Exception e){
            e.printStackTrace();
        }

        Animation slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);
        holder.cardLayout.startAnimation(slide_up);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);

        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView heading, description, time_stamp;
        ImageView image;
        CardView cardLayout;

        public ViewHolder(final View v){
            super(v);
            cardLayout = (CardView) v.findViewById(R.id.cardLayout);
            heading = (TextView) v.findViewById(R.id.heading);
            description = (TextView) v.findViewById(R.id.description);
            time_stamp = (TextView) v.findViewById(R.id.time_stamp);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }
}
