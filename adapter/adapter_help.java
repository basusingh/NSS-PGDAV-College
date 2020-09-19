package com.basusingh.nsspgdav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.ObjectHelp;

import java.util.List;

/**
 * Created by Basu Singh on 10/9/2016.
 */
public class adapter_help extends RecyclerView.Adapter<adapter_help.ViewHolder> {

    List<ObjectHelp> list;
    Context context;

    public adapter_help(List<ObjectHelp> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectHelp o = list.get(position);
        holder.heading.setText(o.getHeading());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView heading;

        public ViewHolder(final View item){
            super(item);
            heading = (TextView) item.findViewById(R.id.heading);
        }
    }

}
