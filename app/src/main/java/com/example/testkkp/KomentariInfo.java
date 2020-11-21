package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KomentariInfo extends AppCompatActivity {

    ImageView pic_user, btn_back, photo_user, photo_info;
    Button btn_balas;
    TextView nama_lengkap, blok, isi_info, waktu;
    EditText isi_komen;

    DatabaseReference reference, reference2, reference3, reference4;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.komentari_info);

        getNikLocal();

        Bundle bundle = getIntent().getExtras();
        final String idx = bundle.getString("id_info");

        isi_info = findViewById(R.id.isi_info);
        waktu = findViewById(R.id.waktu);
        pic_user = findViewById(R.id.pic_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        blok = findViewById(R.id.blok);
        btn_back = findViewById(R.id.btn_back);
        photo_user = findViewById(R.id.photo_user);
        btn_balas = findViewById(R.id.btn_balas);
        isi_komen = findViewById(R.id.isi_komen);
        photo_info = findViewById(R.id.photo_info);

        btn_balas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_balas.setEnabled(false);
                btn_balas.setText("Wait...");
                final String isi_komenx = isi_komen.getText().toString();
                if (isi_komenx.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap mengisi komen terlebih dahulu.", Toast.LENGTH_SHORT).show();
                }else{
                    reference4 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx).child("komen");
                    final String idd = penomoran();
                    reference4.child(idd).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child("id").setValue(idd);
                            snapshot.getRef().child("id_info").setValue(idx);
                            snapshot.getRef().child("waktu").setValue(getDateTime());
                            snapshot.getRef().child("nik").setValue(nik_key_new);
                            snapshot.getRef().child("isi_komen").setValue(isi_komenx);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Toast.makeText(getApplicationContext(), "Komen anda telah di post.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(KomentariInfo.this, DetailClusterInfo.class);
                intent.putExtra("id_info", idx);
                startActivity(intent);
                finish();
            }
        });

        reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(KomentariInfo.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("url_photo_info").exists()){
                    Picasso.with(KomentariInfo.this).load(snapshot.child("url_photo_info").getValue().toString())
                            .centerCrop().fit().into(photo_info);
                }else {
                    photo_info.setVisibility(View.GONE);
                }
                isi_info.setText(snapshot.child("isi_info").getValue().toString());
                waktu.setText(snapshot.child("waktu").getValue().toString());
                final String nikx = snapshot.child("nik").getValue().toString();

                reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(nikx);
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Picasso.with(KomentariInfo.this).load(snapshot.child("url_photo_profile").getValue().toString())
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private String penomoran() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dMyyyyHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy, HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}