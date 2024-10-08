package com.example.appbanhangonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhangonline.R;
import com.example.appbanhangonline.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ChatMessage> list;
    private String sendid;
    private static final int VIEW_TYPE_SEND = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;

    public ChatAdapter(Context context, List<ChatMessage> list, String sendid) {
        this.context = context;
        this.list = list;
        this.sendid = sendid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SEND) {
            return new SendMessViewHolder(LayoutInflater.from(context).inflate(R.layout.send_mess, parent, false));
        } else {
            return new ReceiveMessViewHolder(LayoutInflater.from(context).inflate(R.layout.receive_mess, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SendMessViewHolder) {
            SendMessViewHolder holder1 = (SendMessViewHolder) holder;
            ChatMessage chatMessage = list.get(position);
            if (chatMessage != null) {
                holder1.messsend.setText(chatMessage.getMess());
                holder1.datesend.setText(chatMessage.getDatetine());
            }
        } else {
            ReceiveMessViewHolder holder2 = (ReceiveMessViewHolder) holder;
            ChatMessage chatMessage = list.get(position);
            if (chatMessage != null) {
                holder2.messreceive.setText(chatMessage.getMess());
                holder2.datereceive.setText(chatMessage.getDatetine());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).sendid.equals(sendid) ? VIEW_TYPE_SEND : VIEW_TYPE_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SendMessViewHolder extends RecyclerView.ViewHolder {
        TextView datesend, messsend;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            datesend = itemView.findViewById(R.id.time_send);
            messsend = itemView.findViewById(R.id.text_message_send);
        }
    }

    public class ReceiveMessViewHolder extends RecyclerView.ViewHolder {
        TextView datereceive, messreceive;
        public ReceiveMessViewHolder(@NonNull View itemView) {
            super(itemView);
            datereceive = itemView.findViewById(R.id.time_receive);
            messreceive = itemView.findViewById(R.id.text_message_receive);
        }
    }
}
