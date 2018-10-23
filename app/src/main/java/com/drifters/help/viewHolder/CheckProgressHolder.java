package com.drifters.help.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drifters.help.Interfaces.RecycleViewItemClickListener;
import com.drifters.help.R;

public class CheckProgressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView uImage;
    public TextView uName, uPhone, hType, rStatus, rTime;
    private RecycleViewItemClickListener recycleViewItemClickListener;

    public CheckProgressHolder(View itemView) {
        super(itemView);

        this.uImage = itemView.findViewById(R.id.progress_iv);
        this.uName = itemView.findViewById(R.id.progress_name);
        this.uPhone = itemView.findViewById(R.id.progress_phone);
        this.hType = itemView.findViewById(R.id.progress_help_type);
        this.rStatus = itemView.findViewById(R.id.progress_status);
        this.rTime = itemView.findViewById(R.id.req_sub_time);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.recycleViewItemClickListener.onItemClick(v, getLayoutPosition());
    }

    public void setRecycleViewItemClickListener(RecycleViewItemClickListener ic){
        this.recycleViewItemClickListener = ic;
    }
}
