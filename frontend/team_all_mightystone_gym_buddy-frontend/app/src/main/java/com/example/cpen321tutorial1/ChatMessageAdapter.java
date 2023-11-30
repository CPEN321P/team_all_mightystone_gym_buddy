package com.example.cpen321tutorial1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder>{

    Context context;

    List<ChatMessage> items;

    public ChatMessageAdapter(Context context, List<ChatMessage> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageViewHolder(LayoutInflater.from(context).
                inflate(R.layout.chat_message_view, parent, false));

    }

    @Override
    public void onBindViewHolder
            (@NonNull ChatMessageViewHolder holder, int position) {
        
        if(items.get(position).sender.equals(GlobalClass.myAccount.getUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(items.get(position).body);
        }
        else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatText.setText(items.get(position).body);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
