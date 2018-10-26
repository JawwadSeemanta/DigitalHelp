package com.drifters.help.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.drifters.help.Interfaces.RecycleViewItemClickListener;
import com.drifters.help.MapActivities.SendHelp_MapActivity;
import com.drifters.help.R;
import com.drifters.help.activityClass.SendHelp_ImageViewerActivity;
import com.drifters.help.viewHolder.SendHelp_ViewHolder;
import com.drifters.help.viewModel.SendHelp_ViewModel;

import java.util.ArrayList;

public class SendHelp_ViewAdapter extends RecyclerView.Adapter<SendHelp_ViewHolder> {

    Context c;
    ArrayList<SendHelp_ViewModel> m;

    public SendHelp_ViewAdapter(Context c, ArrayList<SendHelp_ViewModel> m) {
        this.c = c;
        this.m = m;
    }

    @NonNull
    @Override
    public SendHelp_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Converting xml to View Objects
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_send_help_view, null);
        return new SendHelp_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SendHelp_ViewHolder holder, int position) {

        //Bind Data to views
        holder.uName.setText(m.get(position).getName());
        holder.uPhone.setText(m.get(position).getPhone());
        holder.rStatus.setText(m.get(position).getStatus());
        holder.uImage.setImageResource(m.get(position).getImg());
        holder.rId.setText(m.get(position).getReq_id());
        holder.rTime.setText(m.get(position).getReq_time());

        //Animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        //Click on Models
        holder.setRecycleViewItemClickListener(new RecycleViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i;
                if(v.getId() == holder.uImage.getId()){
                    i = new Intent(c, SendHelp_ImageViewerActivity.class);
                    c.startActivity(i);
                }
                else{
                    i = new Intent(c, SendHelp_MapActivity.class);
                    c.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return m.size();
    }

}
