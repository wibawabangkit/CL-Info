package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.testkkp.adapter.InfoAdapter;
import com.example.testkkp.model.ModInfo;
import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CircleView btn_navi;
    ImageView pic_photo_home_user, photo_info;
    Button btn_add_info;
    DatabaseReference reference, reference2;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    RecyclerView info_cluster;
    ArrayList<ModInfo> list;
    InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNikLocal();

        pic_photo_home_user = findViewById(R.id.pic_photo_home_user);
        photo_info = findViewById(R.id.photo_info);
//        item_list_info_cluster = findViewById(R.id.item_list_info_cluster);

        info_cluster = findViewById(R.id.info_cluster);
//        info_cluster.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        info_cluster.setLayoutManager(llm);
        list = new ArrayList<ModInfo>();

        reference2 = FirebaseDatabase.getInstance().getReference("InfoCluster");
//        reference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    ModInfo p = dataSnapshot.getValue(ModInfo.class);
//                    list.add(p);
//                }
//                infoAdapter = new InfoAdapter(MainActivity.this, list);
//                info_cluster.setAdapter(infoAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModInfo p = snapshot.getValue(ModInfo.class);
                list.add(p);
                infoAdapter = new InfoAdapter(MainActivity.this, list);
                info_cluster.setAdapter(infoAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(MainActivity.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(pic_photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_add_info = findViewById(R.id.btn_add_info);
        btn_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateInfo.class);
                startActivity(intent);
            }
        });

        btn_navi = findViewById(R.id.btn_navi);
        btn_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}