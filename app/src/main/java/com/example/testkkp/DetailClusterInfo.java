package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testkkp.adapter.KomenInfoAdapter;
import com.example.testkkp.model.ModKomenInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailClusterInfo extends AppCompatActivity {

    ImageView pic_user, btn_back, photo_info;
    TextView nama_lengkap, blok, isi_info, waktu, hapus, jlh_komen;
    Button balas;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    DatabaseReference reference, reference2, reference4;

    Integer jlh = 0;

    RecyclerView komen_info;
    ArrayList<ModKomenInfo> list;
    KomenInfoAdapter komenInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_cluster_info);

        getNikLocal();

        Bundle bundle = getIntent().getExtras();
        final String idx = bundle.getString("id_info");

        jlh_komen = findViewById(R.id.jlh_komen);
        photo_info = findViewById(R.id.photo_info);

        komen_info = findViewById(R.id.komen_info);
        komen_info.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ModKomenInfo>();

        reference4 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx).child("komen");
        reference4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModKomenInfo p = snapshot.getValue(ModKomenInfo.class);
                list.add(p);
                komenInfoAdapter = new KomenInfoAdapter(DetailClusterInfo.this, list);
                komen_info.setAdapter(komenInfoAdapter);
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

        pic_user = findViewById(R.id.pic_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        blok = findViewById(R.id.blok);
        isi_info = findViewById(R.id.isi_info);
        waktu = findViewById(R.id.waktu);
        balas = findViewById(R.id.balas);
        hapus = findViewById(R.id.hapus);

        balas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KomentariInfo.class);
                intent.putExtra("id_info", idx);
                startActivity(intent);
            }
        });


        reference2 = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("url_photo_info").exists()){
                    Picasso.with(DetailClusterInfo.this).load(snapshot.child("url_photo_info").getValue().toString())
                            .centerCrop().fit().into(photo_info);
                }else {
                    photo_info.setVisibility(View.GONE);
                }
                isi_info.setText(snapshot.child("isi_info").getValue().toString());
                waktu.setText(snapshot.child("waktu").getValue().toString());
                final String nikx = snapshot.child("nik").getValue().toString();
                if (nikx.equals(nik_key_new)) {
                    hapus.setVisibility(View.VISIBLE);
                }else{
                    hapus.setVisibility(View.GONE);
                }

                pic_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailClusterInfo.this, MyHome.class);
                        intent.putExtra("nik_bundle", nikx);
                        startActivity(intent);
                    }
                });

                reference2.child(nikx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Picasso.with(DetailClusterInfo.this).load(snapshot.child("url_photo_profile").getValue().toString())
                                .centerCrop().fit().into(pic_user);
                        nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                        blok.setText("@"+snapshot.child("blok").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("komen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jlh = (int) snapshot.getChildrenCount();
                jlh_komen.setText(Integer.toString(jlh));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailClusterInfo.this);
                alertDialog.setMessage("Hapus info ini.?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Info berhasil di hapus.", Toast.LENGTH_SHORT).show();
                                reference.removeValue();
                                onBackPressed();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailClusterInfo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}