package com.example.cpen321tutorial1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PersonAdapter
        extends RecyclerView.Adapter<FriendViewHolder> {

    Context context;

    List<Account> items;

    public PersonAdapter(Context context, List<Account> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(LayoutInflater.from(context).
                        inflate(R.layout.friend_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder
            (@NonNull FriendViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getUsername());
        holder.usernameView.setText(items.get(position).getUserId());
        holder.imageView.setImageResource(R.drawable.user);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
