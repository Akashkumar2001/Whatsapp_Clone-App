package com.example.whatsapp_clone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp_clone.Modal.Message;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.databinding.ItemReceiveBinding;
import com.example.whatsapp_clone.databinding.ItemSentBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends  RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;


    public  MessagesAdapter(Context context, ArrayList<Message> messages,String senderRoom,String receiverRoom){
        this.context = context;
        this.messages = messages;
        this.senderRoom=senderRoom;
        this.receiverRoom=receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new sentViewholder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new receiveViewholder(view);
        }

    }
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        int reactions[]=new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(pos<0){
                return false;
            }
            if(holder.getClass() == sentViewholder.class){
                sentViewholder viewHolder = (sentViewholder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else{
                receiveViewholder viewHolder = (receiveViewholder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }

            message.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId())
                    .setValue(message);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(message.getMessageId())
                    .setValue(message);

           return true;
        });

        if(holder.getClass() == sentViewholder.class) {
            sentViewholder viewHolder = (sentViewholder)holder;
            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling()>=0){
                viewHolder.binding.feeling.setImageResource(reactions[(int)message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view ,motionEvent);
                    return false;
                }
            });
        } else {
            receiveViewholder viewHolder = (receiveViewholder)holder;
            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling()>=0){
                viewHolder.binding.feeling.setImageResource(reactions[(int)message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view ,motionEvent);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentViewholder extends RecyclerView.ViewHolder{

        ItemSentBinding binding;
        public sentViewholder(@NonNull View itemView) {
            super(itemView);
            binding=ItemSentBinding.bind(itemView);
        }
    }
    public  class receiveViewholder extends RecyclerView.ViewHolder{

        ItemReceiveBinding binding;
        public receiveViewholder(@NonNull View itemView) {
            super(itemView);
            binding=ItemReceiveBinding.bind(itemView);
        }
    }
}
