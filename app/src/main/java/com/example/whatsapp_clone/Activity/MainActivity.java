package com.example.whatsapp_clone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Modal.User;
import com.example.whatsapp_clone.Adapters.UsersAdapter;
import com.example.whatsapp_clone.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<User> users;
    UsersAdapter usersAdapter;

    ProgressDialog dialog;

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();

        users=new ArrayList<>();
        usersAdapter=new UsersAdapter(this,users);

        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(usersAdapter);

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user=snapshot1.getValue(User.class);
                    user.setUid(snapshot1.getKey());
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid())){
                        users.add(user);
                    }

                }
                usersAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.my_profile:
                Intent intent=new Intent(MainActivity.this,SetupProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.search:
                Toast.makeText(MainActivity.this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent1= new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.group:
                Toast.makeText(MainActivity.this, "Groups Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}