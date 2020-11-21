package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity {

    CircleView btn_goto_profile;
    ImageView pic_home_user;
    TextView user_nik, nama_lengkap, no_telp, blok_rumah;
    LinearLayout btn_home, btn_logout, btn_back, btn_iuran, line_admin, btn_pengguna, btn_edit_pass, btn_confirm, btn_profile;

    DatabaseReference reference, reference2, reference3;
    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getNikLocal();

        pic_home_user = findViewById(R.id.pic_home_user);
        user_nik = findViewById(R.id.user_nik);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        no_telp = findViewById(R.id.no_telp);
        blok_rumah = findViewById(R.id.blok_rumah);
        line_admin = findViewById(R.id.line_admin);
        btn_profile = findViewById(R.id.btn_profile);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MyHome.class);
                intent.putExtra("nik_bundle", nik_key_new);
                startActivity(intent);
            }
        });

        btn_goto_profile = findViewById(R.id.btn_goto_profile);
        btn_goto_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MyHome.class);
                intent.putExtra("nik_bundle", nik_key_new);
                startActivity(intent);
            }
        });

        btn_edit_pass = findViewById(R.id.btn_edit_pass);
        btn_edit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) DashboardActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View formView = inflater.inflate(R.layout.edit_password, null, false);
                final EditText pass_lama = formView.findViewById(R.id.pass_lama);
                final EditText pass_baru = formView.findViewById(R.id.pass_baru);
                final EditText konfirm_pass_baru = formView.findViewById(R.id.konfirm_pass_baru);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardActivity.this)
                        .setView(formView)
                        .setTitle("Edit Password")
                        .setCancelable(false)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String password_lama = pass_lama.getText().toString();
                                final String password = pass_baru.getText().toString();
                                final String konfirm_password = konfirm_pass_baru.getText().toString();

                                reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String passFormDB = snapshot.child("password").getValue().toString();

                                        if (password_lama.equals(passFormDB)) {
                                            if (password.equals(konfirm_password)) {
                                                reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
                                                reference3.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        snapshot.getRef().child("password").setValue(password);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Konfirmasi password tidak sama dengan password baru.", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Password lama tidak benar.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btn_pengguna = findViewById(R.id.btn_pengguna);
        btn_pengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, PenggunaActivity.class);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String levelAdmin = snapshot.child("level").getValue().toString();
                final String level = "Warga";
                if (levelAdmin.equals(level)) {
                    line_admin.setVisibility(View.GONE);
                }
                Picasso.with(DashboardActivity.this).load(snapshot.child("url_photo_profile").getValue().toString())
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

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(nik_key, null);
                editor.apply();

                Intent intent = new Intent(DashboardActivity.this, GetStarted.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_iuran = findViewById(R.id.btn_iuran);
        btn_iuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, IuranActivity.class);
                startActivity(intent);
            }
        });

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ConfirmPembayaran.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}