package com.example.cpen321tutorial1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{
    Context context;

    List<Chat> items;

    public ChatAdapter(Context context, List<Chat> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ChatViewHolder(LayoutInflater.from(context).
                inflate(R.layout.chat_item_view, parent, false));
        
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getOtherAccount().getUsername());
        holder.usernameView.setText(items.get(position).getOtherAccount().getEmailAddress());
        holder.imageView.setImageResource(R.drawable.user);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
