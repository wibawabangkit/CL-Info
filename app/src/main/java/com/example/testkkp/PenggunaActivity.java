package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testkkp.adapter.DataNikAdapter;
import com.example.testkkp.model.ModDataNik;
import com.example.testkkp.model.ModIuran;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PenggunaActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_tambah;

    DatabaseReference reference, reference2;

    RecyclerView data_nik;
    ArrayList<ModDataNik> list;
    DataNikAdapter dataNikAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna);

        data_nik = findViewById(R.id.data_nik);
        data_nik.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ModDataNik>();

        reference = FirebaseDatabase.getInstance().getReference().child("Warga");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModDataNik p = snapshot.getValue(ModDataNik.class);
                list.add(p);
                dataNikAdapter = new DataNikAdapter(PenggunaActivity.this, list);
                data_nik.setAdapter(dataNikAdapter);
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

        btn_tambah = findViewById(R.id.btn_tambah);
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) PenggunaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View formView = inflater.inflate(R.layout.tambah_nik_pengguna, null, false);
                final EditText isi_nik = formView.findViewById(R.id.isi_nik);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PenggunaActivity.this)
                        .setView(formView)
                        .setTitle("Isi NIK")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String nik = isi_nik.getText().toString();
                                reference2 = FirebaseDatabase.getInstance().getReference().child("Warga").child(nik);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(getApplicationContext(), "NIK sudah terdaftar.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            snapshot.getRef().child("nik").setValue(nik);
                                            Toast.makeText(getApplicationContext(), "NIK berhasil terdaftar.", Toast.LENGTH_SHORT).show();

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

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PenggunaActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PenggunaActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}