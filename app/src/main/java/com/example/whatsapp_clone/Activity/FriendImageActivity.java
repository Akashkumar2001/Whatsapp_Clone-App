package com.example.whatsapp_clone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.databinding.ActivityFriendImageBinding;
import com.squareup.picasso.Picasso;

public class FriendImageActivity extends AppCompatActivity {
    ActivityFriendImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFriendImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String profileName=getIntent().getStringExtra("name");
        String profilePic=getIntent().getStringExtra("profile");
        binding.textView.setText(profileName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.imageView3);
    }
}