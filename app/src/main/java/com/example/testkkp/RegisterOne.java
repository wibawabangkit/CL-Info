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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOne extends AppCompatActivity {

    EditText nik, password, email;
    LinearLayout btn_back;
    Button btn_lanjut;
    DatabaseReference reference, reference_nik_warga, reference_nik;

    String NIK_KEY = "nikkey";
    String nik_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        nik = findViewById(R.id.nik);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_lanjut = findViewById(R.id.btn_lanjut);
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_lanjut.setEnabled(false);
                btn_lanjut.setText("Loading...");

                //cek nik warga
                reference_nik_warga = FirebaseDatabase.getInstance().getReference().child("Warga").child(nik.getText().toString());
                reference_nik_warga.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //cek nik user
                            reference_nik = FirebaseDatabase.getInstance().getReference().child("Users").child(nik.getText().toString());
                            reference_nik.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Toast.makeText(getApplicationContext(), "NIK Sudah Terdaftar.!", Toast.LENGTH_SHORT).show();
                                        btn_lanjut.setEnabled(true);
                                        btn_lanjut.setText("CONTINUE");
                                    }else{
                                        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(nik_key, nik.getText().toString());
                                        editor.apply();

                                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik.getText().toString());
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("nik").setValue(nik.getText().toString());
                                                snapshot.getRef().child("password").setValue(password.getText().toString());
                                                snapshot.getRef().child("email").setValue(email.getText().toString());
                                                snapshot.getRef().child("level").setValue("Warga");
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Intent intent = new Intent(RegisterOne.this, RegisterTwo.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "NIK Belum Terdaftar. Harap Lapor Kepada Ketua Cluster", Toast.LENGTH_SHORT).show();
                            btn_lanjut.setEnabled(true);
                            btn_lanjut.setText("CONTINUE");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}