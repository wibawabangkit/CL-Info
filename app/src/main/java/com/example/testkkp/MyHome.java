package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testkkp.adapter.InfoAdapter;
import com.example.testkkp.model.ModInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyHome extends AppCompatActivity {

    ImageView pic_home_user;
    TextView user_nik, nama_lengkap, no_telp, blok_rumah;
    Button btn_edit_profile, btn_add_info;

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
        setContentView(R.layout.my_home);

        getNikLocal();

        Bundle bundle = getIntent().getExtras();
        final String nik_bundle = bundle.getString("nik_bundle");

        pic_home_user = findViewById(R.id.pic_home_user);
        user_nik = findViewById(R.id.user_nik);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        no_telp = findViewById(R.id.no_telp);
        blok_rumah = findViewById(R.id.blok_rumah);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_add_info = findViewById(R.id.btn_add_info);

        if (nik_bundle.equals(nik_key_new)){
            btn_add_info.setVisibility(View.VISIBLE);
        }else{
            btn_add_info.setVisibility(View.GONE);
        }

        btn_add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyHome.this, UpdateInfo2.class);
                startActivity(intent);
            }
        });

        info_cluster = findViewById(R.id.info_cluster);
//        info_cluster.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        info_cluster.setLayoutManager(llm);
        list = new ArrayList<ModInfo>();

        reference2 = FirebaseDatabase.getInstance().getReference().child("InfoCluster");
        reference2.getRef().orderByChild("nik").equalTo(nik_bundle).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModInfo p = snapshot.getValue(ModInfo.class);
                list.add(p);
                infoAdapter = new InfoAdapter(MyHome.this, list);
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

        if (nik_bundle.equals(nik_key_new)) {
            btn_edit_profile.setVisibility(View.VISIBLE);
        }else{
            btn_edit_profile.setVisibility(View.GONE);
        }

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyHome.this, MyProfilActivity.class);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_bundle);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(MyHome.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(pic_home_user);
                user_nik.setText(snapshot.child("nik").getValue().toString());
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                no_telp.setText(snapshot.child("no_telp").getValue().toString());
                blok_rumah.setText(snapshot.child("blok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyHome.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}