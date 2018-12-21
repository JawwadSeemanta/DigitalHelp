package com.drifters.help.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.DataModel.RequestModel;
import com.drifters.help.R;
import com.drifters.help.viewHolder.CheckProgressHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckProgressAdapter extends RecyclerView.Adapter<CheckProgressHolder> {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Context context;
    private ArrayList<RequestModel> models;
    private FirebaseStorage firebaseStorage;

    public CheckProgressAdapter(Context c, ArrayList<RequestModel> models) {
        this.context = c;
        this.models = models;
    }

    @NonNull
    @Override
    public CheckProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Converting xml to View Objects
        View v = LayoutInflater.from(context).inflate(R.layout.model_check_progress, parent, false);

        return new CheckProgressHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckProgressHolder holder, int position) {

        final RequestModel currentModel = models.get(position);
        //Bind Data to views
        holder.uName.setText(currentModel.getRequester_Name());
        holder.uPhone.setText(currentModel.getRequester_Phone());
        holder.hType.setText(currentModel.getRequest_type());
        holder.rStatus.setText(currentModel.getStatus());
        holder.rTime.setText(currentModel.getSubmission_time());

        Picasso.get()
                .load(currentModel.getImageURL())
                .placeholder(R.drawable.loading_indicator)
                .fit()
                .into(holder.uImage);

        //Animation
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        holder.setOnItemClickListener(new myItemOnclickListener() {
            @Override
            public void OnItemClick(final int position) {
                new AlertDialog.Builder(context).setMessage("Do you want to delete your request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (ConnectionCheck.isOnline(context)) {
                                    // code to delete the entry
                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    databaseReference = firebaseDatabase.getReference();
                                    firebaseStorage = FirebaseStorage.getInstance();

                                    final String key = currentModel.getRequest_ID();

                                    //Delete the image in the storage
                                    StorageReference storageReference = firebaseStorage.getReferenceFromUrl(currentModel.getImageURL());
                                    storageReference.delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //If Successful delete database entry
                                                    databaseReference.child(key).removeValue();

                                                    Toast.makeText(context, "Have a nice day!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface myItemOnclickListener {
        void OnItemClick(int position);
    }
}
