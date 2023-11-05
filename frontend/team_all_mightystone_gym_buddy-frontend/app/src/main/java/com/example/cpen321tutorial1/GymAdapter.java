package com.example.cpen321tutorial1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymViewHolder>{

    Context context;
    List<Gym> items;

    public GymAdapter(Context context, List<Gym> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        return new GymViewHolder
                (LayoutInflater.from(context).inflate(R.layout.gym_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder
            (@NonNull GymViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.addressView.setText(items.get(position).getAddress());
        holder.imageView.setImageResource(R.drawable.gym);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
