package com.drifters.help.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.DataModel.RequestModel;
import com.drifters.help.Interfaces.RecycleViewItemClickListener;
import com.drifters.help.MapActivities.SendHelp_MapActivity;
import com.drifters.help.R;
import com.drifters.help.activityClass.SendHelp_ImageViewerActivity;
import com.drifters.help.viewHolder.SendHelp_ViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SendHelp_ViewAdapter extends RecyclerView.Adapter<SendHelp_ViewHolder> {

    private Context c;
    private ArrayList<RequestModel> m;

    public SendHelp_ViewAdapter(Context c, ArrayList<RequestModel> m) {
        this.c = c;
        this.m = m;
    }

    @NonNull
    @Override
    public SendHelp_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Converting xml to View Objects
        View v = LayoutInflater.from(c).inflate(R.layout.model_send_help_view, parent, false);
        return new SendHelp_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SendHelp_ViewHolder holder, int position) {

        //Bind Data to views
        holder.uName.setText(m.get(position).getRequester_Name());
        holder.uPhone.setText(m.get(position).getRequester_Phone());
        holder.rStatus.setText(m.get(position).getStatus());
        holder.rId.setText(m.get(position).getRequest_ID());
        holder.rTime.setText(m.get(position).getSubmission_time());
        Picasso.get()
                .load(m.get(position).getImageURL())
                .placeholder(R.drawable.loading_indicator)
                .fit()
                .into(holder.uImage);

        //Animation
        Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        //Click on Models
        holder.setRecycleViewItemClickListener(new RecycleViewItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                Intent i;
                if (v.getId() == holder.uImage.getId()) {

                    i = new Intent(c, SendHelp_ImageViewerActivity.class);
                    i.putExtra("url", m.get(position).getImageURL());
                    c.startActivity(i);
                } else {
                    if (m.get(position).getStatus().equals("REQUESTED")) {
                        i = new Intent(c, SendHelp_MapActivity.class);
                        i.putExtra("id", m.get(position).getRequest_ID());
                        i.putExtra("accepted?", false);

                        i.putExtra("dest_lat", m.get(position).getLat());
                        i.putExtra("dest_lon", m.get(position).getLang());
                        c.startActivity(i);
                    }
                    if (m.get(position).getStatus().equals("ACCEPTED")) {
                        i = new Intent(c, SendHelp_MapActivity.class);
                        i.putExtra("id", m.get(position).getRequest_ID());
                        i.putExtra("accepted?", true);

                        i.putExtra("dest_lat", m.get(position).getLat());
                        i.putExtra("dest_lon", m.get(position).getLang());
                        c.startActivity(i);
                    }

                    if (m.get(position).getStatus().equals("COMPLETE")) {
                        new AlertDialog.Builder(v.getContext())
                                .setMessage("Do you want to delete the entry?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (ConnectionCheck.isOnline(c)) {
                                            // code to delete the entry
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            final DatabaseReference databaseReference = firebaseDatabase.getReference();
                                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                                            final String key = m.get(position).getRequest_ID();

                                            //Delete the image in the storage
                                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(m.get(position).getImageURL());
                                            storageReference.delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //If Successful delete database entry
                                                            databaseReference.child(key).removeValue();

                                                            Toast.makeText(c, "Request Deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }


                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return m.size();
    }

}
