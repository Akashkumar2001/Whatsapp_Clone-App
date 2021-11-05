package com.example.whatsapp_clone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp_clone.Adapters.MessagesAdapter;
import com.example.whatsapp_clone.Modal.Message;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Modal.User;
import com.example.whatsapp_clone.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String senderRoom, receiverRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messages=new ArrayList<>();
        adapter=new MessagesAdapter(this,messages);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setAdapter(adapter);

        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        String senderUid=auth.getUid();
        String receiverUid=getIntent().getStringExtra("uid");
        String name=getIntent().getStringExtra("name");
        String profilePic=getIntent().getStringExtra("profile");

        senderRoom=senderUid+receiverUid;
        receiverRoom=receiverUid+senderUid;

        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            Message message=snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   String messageTxt = binding.messageBox.getText().toString();
                                                   Date date = new Date();

                                                   Message message = new Message(messageTxt, senderUid, date.getTime());
                                                   binding.messageBox.setText("");
                                                   database.getReference().child("chats")
                                                           .child(senderRoom)
                                                           .child("messages")
                                                           .push()
                                                           .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           database.getReference().child("chats")
                                                                   .child(receiverRoom)
                                                                   .child("messages")
                                                                   .push()
                                                                   .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void unused) {

                                                               }
                                                           });
                                                       }
                                                   });
                                               }
                                           });

       binding.profileName.setText(name);
       Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);

       binding.backArrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent= new Intent(ChatActivity.this,MainActivity.class);
               startActivity(intent);
               finishAffinity();
           }
       });
       binding.profileImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               User user=new User(name,profilePic);
               Intent intent=new Intent(ChatActivity.this,FriendImageActivity.class);
               intent.putExtra("name",user.getName());
               intent.putExtra("profile",user.getProfileImage());
               startActivity(intent);
           }
       });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}