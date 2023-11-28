package com.example.cpen321tutorial1;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    LinearLayout leftChatLayout, rightChatLayout;
    TextView leftChatText, rightChatText;

    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
        rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
        leftChatText = itemView.findViewById(R.id.left_chat_text);
        rightChatText = itemView.findViewById(R.id.right_chat_text);

    }
}
