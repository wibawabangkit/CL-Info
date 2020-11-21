package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDetailIuranActivity extends AppCompatActivity {

    TextView nama_lengkap, blok, waktu, jlh_iuran, status;
    LinearLayout btn_back;

    DatabaseReference reference;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail_iuran);

        getNikLocal();

        nama_lengkap = findViewById(R.id.nama_lengkap);
        blok = findViewById(R.id.blok);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String waktu_baru = bundle.getString("waktu");
        final String jlh_iuran_baru = bundle.getString("jlh_iuran");
        final String status_baru = bundle.getString("status");

        waktu = findViewById(R.id.waktu);
        jlh_iuran = findViewById(R.id.jlh_iuran);
        status = findViewById(R.id.status);
        waktu.setText(waktu_baru);
        jlh_iuran.setText("Rp. "+jlh_iuran_baru);
        status.setText(status_baru);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                blok.setText(snapshot.child("blok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}