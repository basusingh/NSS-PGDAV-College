package com.basusingh.nsspgdav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.basusingh.nsspgdav.helper.ObjectTeamMember;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Basu Singh on 10/9/2016.
 */
public class adapter_team_member extends RecyclerView.Adapter<adapter_team_member.ViewHolder> {

    List<ObjectTeamMember> list;
    Context context;

    public adapter_team_member(List<ObjectTeamMember> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectTeamMember o = list.get(position);

        holder.name.setText(o.getName());
        holder.post.setText(o.getPost());
        holder.mobile.setText(o.getMobile());
        try{
            Picasso.with(context).load(o.getImageUrl()).placeholder(context.getResources().getDrawable(R.drawable.loading_picasso)).error(context.getResources().getDrawable(R.drawable.loading_picasso)).into(holder.image);
        } catch (Exception e){
            e.printStackTrace();
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.loading_picasso));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name, post, mobile;

        public ViewHolder(final View item){
            super(item);
            image = (ImageView) item.findViewById(R.id.image);
            name = (TextView) item.findViewById(R.id.name);
            post = (TextView) item.findViewById(R.id.post);
            mobile = (TextView) item.findViewById(R.id.mobile);
        }
    }
}
