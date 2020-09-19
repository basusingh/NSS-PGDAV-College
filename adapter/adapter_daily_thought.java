package com.basusingh.nsspgdav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.helper.ObjectDailyThought;
import com.basusingh.nsspgdav.helper.ObjectHelp;

import java.util.List;

/**
 * Created by Basu Singh on 10/12/2016.
 */
public class adapter_daily_thought extends RecyclerView.Adapter<adapter_daily_thought.ViewHolder> {

    List<ObjectDailyThought> list;
    Context context;

    public adapter_daily_thought(List<ObjectDailyThought> mList, Context mContext){
        this.list = mList;
        this.context = mContext;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_thought, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ObjectDailyThought o = list.get(position);
        holder.thought.setText(o.getThought());
        holder.time_stamp.setText(o.getTime_stamp());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView thought, time_stamp;

        public ViewHolder(final View item){
            super(item);
            thought = (TextView) item.findViewById(R.id.thought);
            time_stamp = (TextView) item.findViewById(R.id.time_stamp);
        }
    }

}
