package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testkkp.adapter.IuranAdapter;
import com.example.testkkp.model.ModIuran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IuranActivity extends AppCompatActivity {

    LinearLayout btn_back_dashboard;
    Button btn_back_home;
    ImageView photo_profile;
    TextView nama_lengkap, blok, btn_bayar;

    DatabaseReference reference, reference2;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    RecyclerView iuran_ku;
    ArrayList<ModIuran> list;
    IuranAdapter iuranAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iuran);

        getNikLocal();

        photo_profile = findViewById(R.id.photo_profile);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        blok = findViewById(R.id.blok);

        iuran_ku = findViewById(R.id.iuran_ku);
        iuran_ku.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ModIuran>();

        reference2 = FirebaseDatabase.getInstance().getReference().child("Iuran").child(nik_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ModIuran p = dataSnapshot.getValue(ModIuran.class);
                    list.add(p);
                }
                iuranAdapter = new IuranAdapter(IuranActivity.this, list);
                iuran_ku.setAdapter(iuranAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_bayar = findViewById(R.id.btn_bayar);
        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IuranActivity.this, BayarIuranActivity.class);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(IuranActivity.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_profile);
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                blok.setText(snapshot.child("blok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back_home = findViewById(R.id.btn_back_home);
        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_back_dashboard = findViewById(R.id.btn_back_dashboard);
        btn_back_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IuranActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}