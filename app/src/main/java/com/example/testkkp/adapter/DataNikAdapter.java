package com.example.testkkp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testkkp.PenggunaActivity;
import com.example.testkkp.R;
import com.example.testkkp.model.ModDataNik;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataNikAdapter extends RecyclerView.Adapter<DataNikAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModDataNik> modDataNik;
    DatabaseReference reference;

    public DataNikAdapter(Context c, ArrayList<ModDataNik> p) {
        this.context = c;
        this.modDataNik = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_nik, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nik_warga.setText(modDataNik.get(position).getNik());

        final String nik = modDataNik.get(position).getNik();

        holder.btn_hapus_nik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }

            private void showAlertDialog() {
                String dialogTitle, dialogMessage;
                dialogTitle = "Hapus Data";
                dialogMessage = "Apakah anda yakin ingin menghapus data ini.?";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(dialogTitle);
                alertDialogBuilder.setMessage(dialogMessage)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference = FirebaseDatabase.getInstance().getReference().child("Warga").child(nik);
                                reference.removeValue();

                                Intent intent = new Intent(context, PenggunaActivity.class);
                                context.startActivity(intent);
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return modDataNik.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nik_warga;
        ImageView btn_hapus_nik;

        public MyViewHolder(View view) {
            super(view);
            nik_warga = view.findViewById(R.id.nik_warga);
            btn_hapus_nik = view.findViewById(R.id.btn_hapus_nik);
        }
    }
}
