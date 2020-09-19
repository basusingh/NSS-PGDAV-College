package com.basusingh.nsspgdav.NavDrawer;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basusingh.nsspgdav.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<NavDrawerItems> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    int selectedPosition = 0;


    public NavigationDrawerAdapter(Context context, List<NavDrawerItems> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(selectedPosition == position){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.grey_efef));
            holder.title.setTextColor(context.getResources().getColor(R.color.dark_red));
            holder.icon.setColorFilter(context.getResources().getColor(R.color.dark_red));
        } else {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            holder.icon.setColorFilter(context.getResources().getColor(R.color.black));
        }
        NavDrawerItems current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(current.getIcon());

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

}
