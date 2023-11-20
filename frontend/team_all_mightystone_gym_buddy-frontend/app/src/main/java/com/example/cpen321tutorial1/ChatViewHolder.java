package com.example.cpen321tutorial1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;

    TextView nameView;

    TextView usernameView;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        usernameView = itemView.findViewById(R.id.username);
    }
}
