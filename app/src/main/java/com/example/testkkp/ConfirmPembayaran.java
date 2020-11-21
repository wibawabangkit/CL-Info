package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testkkp.adapter.ConfirmAdapter;
import com.example.testkkp.model.ModConfirm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConfirmPembayaran extends AppCompatActivity {

    LinearLayout btn_back;

    DatabaseReference reference;

    RecyclerView data_pembayar;
    ArrayList<ModConfirm> list;
    ConfirmAdapter confirmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_pembayaran);

        data_pembayar = findViewById(R.id.data_pembayar);
        data_pembayar.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ModConfirm>();

        reference = FirebaseDatabase.getInstance().getReference().child("Pembayaran");
        reference.getRef().orderByChild("status").equalTo("Pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ModConfirm p = dataSnapshot.getValue(ModConfirm.class);
                    list.add(p);
                }
                confirmAdapter = new ConfirmAdapter(ConfirmPembayaran.this, list);
                data_pembayar.setAdapter(confirmAdapter);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConfirmPembayaran.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}