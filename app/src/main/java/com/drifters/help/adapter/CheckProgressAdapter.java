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
import com.drifters.help.R;
import com.drifters.help.MapActivities.CheckProgress_MapsActivity;
import com.drifters.help.viewHolder.CheckProgressHolder;
import com.drifters.help.viewModel.CheckProgressModel;

import java.util.ArrayList;

public class CheckProgressAdapter extends RecyclerView.Adapter<CheckProgressHolder> {

    Context c;
    ArrayList<CheckProgressModel> models;

    public CheckProgressAdapter(Context c, ArrayList<CheckProgressModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public CheckProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Converting xml to View Objects
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_check_progress, null);

        return new CheckProgressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckProgressHolder holder, int position) {

        //Bind Data to views
        holder.uName.setText(models.get(position).getName());
        holder.uPhone.setText(models.get(position).getPhone());
        holder.hType.setText(models.get(position).getHelp_type());
        holder.rStatus.setText(models.get(position).getStatus());
        holder.rTime.setText(models.get(position).getSub_time());
        holder.uImage.setImageResource(models.get(position).getImg());

        //Animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        //Click on Models
        holder.setRecycleViewItemClickListener(new RecycleViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(c, CheckProgress_MapsActivity.class);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
