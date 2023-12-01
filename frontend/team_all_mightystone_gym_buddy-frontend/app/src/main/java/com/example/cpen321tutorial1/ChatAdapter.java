package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{
    Context context;

    List<ChatModelFromBackend> items;

    public ChatAdapter(Context context, List<ChatModelFromBackend> items) {
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
        ConnectionToBackend c = new ConnectionToBackend();
        Account otherAccount;

        if(items.get(position).members.get(0).equals(myAccount.getUserId())){
            otherAccount = c.getAccountInformation(items.get(position).members.get(1));
        } else {
            otherAccount = c.getAccountInformation(items.get(position).members.get(0));
        }


        holder.nameView.setText(otherAccount.getUsername());
        holder.usernameView.setText(otherAccount.getEmailAddress());
        holder.imageView.setImageResource(R.drawable.user);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
