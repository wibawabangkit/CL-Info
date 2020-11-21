package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    EditText nikx, passwordx;
    Button btn_login, btn_new_acc;

    DatabaseReference reference;

    String NIK_KEY = "nikkey";
    String nik_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        nikx = findViewById(R.id.nikx);
        passwordx = findViewById(R.id.passwordx);

        btn_new_acc = findViewById(R.id.btn_new_acc);
        btn_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, RegisterOne.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_login.setEnabled(false);
                btn_login.setText("Loading...");

                final String nik = nikx.getText().toString();
                final String password = passwordx.getText().toString();

                if (nik.isEmpty() && password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "NIK dan Password tidak boleh kosong.!", Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                    btn_login.setText("SIGN IN");
                }else if (nik.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "NIK tidak boleh kosong.!", Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                    btn_login.setText("SIGN IN");
                }else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password tidak boleh kosong.!", Toast.LENGTH_SHORT).show();
                    btn_login.setEnabled(true);
                    btn_login.setText("SIGN IN");
                }else {
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //ambil pw dari firebase
                                String pwFromFirebase = snapshot.child("password").getValue().toString();
                                //validasi pw input dg pw di firebase
                                if (password.equals(pwFromFirebase)) {
                                    //menyimpan data ke local storage
                                    SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(nik_key, nikx.getText().toString());
                                    editor.apply();

                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Password Salah.!", Toast.LENGTH_SHORT);
                                    btn_login.setEnabled(true);
                                    btn_login.setText("SIGN IN");
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "NIK belum terdaftar, harap lapor kepada ketua.!", Toast.LENGTH_SHORT).show();
                                btn_login.setEnabled(true);
                                btn_login.setText("SIGN IN");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }
}
