package com.basusingh.nsspgdav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.ObjectBloodGroup;

import java.util.List;

/**
 * Created by Basu Singh on 10/7/2016.
 */
public class adapter_blood_group_fav extends RecyclerView.Adapter<adapter_blood_group_fav.ViewHolder> {


    List<ObjectBloodGroup> list;
    Context context;

    public adapter_blood_group_fav(List<ObjectBloodGroup> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_group, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectBloodGroup o = list.get(position);
        holder.name.setText(o.getName());
        holder.age.setText(o.getAge());
        holder.mobile.setText(o.getMobile());
        holder.bloodgroup.setText(o.getBloodgroup());
    }
    @Override
    public int getItemCount(){
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, age, mobile, bloodgroup;

        public ViewHolder(final View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name);
            age = (TextView) item.findViewById(R.id.age);
            mobile = (TextView) item.findViewById(R.id.mobile);
            bloodgroup = (TextView) item.findViewById(R.id.bloodgroup);
        }
    }
}
