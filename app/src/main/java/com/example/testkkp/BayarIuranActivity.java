package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BayarIuranActivity extends AppCompatActivity {

    TextView at_nama;
    LinearLayout btn_back;
    EditText bank_bca, jlh_iuranx, nama_bankx, no_rekx, nama_pengx;
    Button button_bayar_iuran;

    DatabaseReference reference, reference2, reference3;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_iuran);

        getNikLocal();

        jlh_iuranx = findViewById(R.id.jlh_iuran);
        nama_bankx = findViewById(R.id.nama_bank);
        no_rekx = findViewById(R.id.no_rek);
        nama_pengx = findViewById(R.id.nama_peng);

        button_bayar_iuran = findViewById(R.id.button_bayar_iuran);
        button_bayar_iuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_bayar_iuran.setEnabled(false);
                button_bayar_iuran.setText("Loading...");

                final String jlh_iuran = jlh_iuranx.getText().toString();
                final String nama_bank = nama_bankx.getText().toString();
                final String no_rek = no_rekx.getText().toString();
                final String nama_peng = nama_pengx.getText().toString();

                if (jlh_iuran.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Jumlah iuran tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                    button_bayar_iuran.setEnabled(true);
                    button_bayar_iuran.setText("BAYAR");
                }else if (nama_bank.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama bank harap di isi.", Toast.LENGTH_SHORT).show();
                    button_bayar_iuran.setEnabled(true);
                    button_bayar_iuran.setText("BAYAR");
                }else if (no_rek.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No.Rek harap di isi.", Toast.LENGTH_SHORT).show();
                    button_bayar_iuran.setEnabled(true);
                    button_bayar_iuran.setText("BAYAR");
                }else if (nama_peng.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama Pengirim harap di isi.", Toast.LENGTH_SHORT).show();
                    button_bayar_iuran.setEnabled(true);
                    button_bayar_iuran.setText("BAYAR");
                }else {
                    reference2 = FirebaseDatabase.getInstance().getReference().child("Iuran").child(nik_key_new).child(getDateTime());
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child("waktu").setValue(getDateTime());
                            snapshot.getRef().child("nik").setValue(nik_key_new);
                            snapshot.getRef().child("jlh_iuran").setValue(jlh_iuran);
                            snapshot.getRef().child("nama_bank").setValue(nama_bank);
                            snapshot.getRef().child("no_rek").setValue(no_rek);
                            snapshot.getRef().child("nama_peng").setValue(nama_peng);
                            snapshot.getRef().child("status").setValue("Pending");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    reference3 = FirebaseDatabase.getInstance().getReference().child("Pembayaran").child(getDateTime());
                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().child("waktu").setValue(getDateTime());
                            snapshot.getRef().child("nik").setValue(nik_key_new);
                            snapshot.getRef().child("jlh_iuran").setValue(jlh_iuran);
                            snapshot.getRef().child("nama_bank").setValue(nama_bank);
                            snapshot.getRef().child("no_rek").setValue(no_rek);
                            snapshot.getRef().child("nama_peng").setValue(nama_peng);
                            snapshot.getRef().child("status").setValue("Pending");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent intent = new Intent(BayarIuranActivity.this, SuksesBayarActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        at_nama = findViewById(R.id.at_nama);

        bank_bca = findViewById(R.id.bank_bca);
        bank_bca.setEnabled(false);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                at_nama.setText(snapshot.child("nama_lengkap").getValue().toString()+" / "+snapshot.child("blok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BayarIuranActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy, HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}