package com.drifters.help.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drifters.help.Interfaces.RecycleViewItemClickListener;
import com.drifters.help.R;

public class SendHelp_ViewHolder extends ViewHolder implements View.OnClickListener{

    public ImageView uImage;
    public TextView uName, uPhone, rStatus, rId, rTime, rDistance;
    private RecycleViewItemClickListener recycleViewItemClickListener;

    public SendHelp_ViewHolder(View itemView) {
        super(itemView);

        this.uImage = itemView.findViewById(R.id.view_iv);
        this.uName = itemView.findViewById(R.id.view_name);
        this.uPhone = itemView.findViewById(R.id.view_phone);
        this.rStatus = itemView.findViewById(R.id.view_status);
        this.rId = itemView.findViewById(R.id.req_id);
        this.rTime = itemView.findViewById(R.id.sub_time);
        this.rDistance = itemView.findViewById(R.id.req_distance);

        itemView.setOnClickListener(this);
        uImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.recycleViewItemClickListener.onItemClick(v, getLayoutPosition());
    }

    public void setRecycleViewItemClickListener(RecycleViewItemClickListener ic){
        this.recycleViewItemClickListener = ic;
    }
}
