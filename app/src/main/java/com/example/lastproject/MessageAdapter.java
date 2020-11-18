package com.example.lastproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;

    FirebaseUser fUser;

    ArrayList<MessageObject> messageList;

    public MessageAdapter(ArrayList<MessageObject> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder rcv;
        if(viewType == MSG_TYPE_RIGHT){
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messageright, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

         rcv = new MessageViewHolder(layoutView);
        }
        else
        {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);

            rcv = new MessageViewHolder(layoutView);

        }
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        holder.mMessage.setText(messageList.get(position).getMessage());
//        holder.mSender.setText(messageList.get(position).getSenderId());

        if(messageList.get(holder.getAdapterPosition()).getMediaUrlList().isEmpty())
            holder.mViewMedia.setVisibility(View.GONE);
        if(messageList.get(holder.getAdapterPosition()).getMessage().isEmpty())
            holder.mMessage.setVisibility(View.GONE);

        holder.mViewMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(v.getContext(), messageList.get(holder.getAdapterPosition()).getMediaUrlList())
                        .setStartPosition(0)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }







    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView    mMessage,
                mSender;
        ImageButton mViewMedia;
        LinearLayout mLayout;
        MessageViewHolder(View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);

            mMessage = view.findViewById(R.id.message);
            //mSender = view.findViewById(R.id.sender);

            mViewMedia = view.findViewById(R.id.viewMedia);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messageList.get(position).senderId.equals(fUser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}