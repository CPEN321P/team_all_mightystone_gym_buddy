package com.example.cpen321tutorial1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GymViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    TextView nameView;

    TextView addressView;

    public GymViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        addressView = itemView.findViewById(R.id.address);
    }
}
