package com.example.cpen321tutorial1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FriendViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView, usernameView;

    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        usernameView = itemView.findViewById(R.id.username);
    }
}
