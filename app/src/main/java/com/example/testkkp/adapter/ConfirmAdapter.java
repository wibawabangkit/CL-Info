package com.example.testkkp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testkkp.ConfirmPembayaran;
import com.example.testkkp.R;
import com.example.testkkp.model.ModConfirm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModConfirm> modConfirm;
    DatabaseReference reference, reference2, reference3, reference4, reference5;

    public ConfirmAdapter(Context c, ArrayList<ModConfirm> p) {
        context = c;
        modConfirm = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_confirm_pembayaran, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.status.setText(modConfirm.get(position).getStatus());
        holder.jlh_iuran.setText(modConfirm.get(position).getJlh_iuran());
        holder.txt_bank.setText(modConfirm.get(position).getNama_bank());
        holder.nama_peng.setText(modConfirm.get(position).getNama_peng());
        holder.no_rek.setText(modConfirm.get(position).getNo_rek());
        holder.waktu.setText(modConfirm.get(position).getWaktu());

        final String getNikx = modConfirm.get(position).getNik();
        final String getWaktux = modConfirm.get(position).getWaktu();
        final String getStatusx = modConfirm.get(position).getStatus();
        final String jlhIuranx = modConfirm.get(position).getJlh_iuran();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(getNikx);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(context).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(holder.photo_profile);
                holder.nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getStatusx.equals("Pending")) {
                    showDialogPending();
                }else {
//                    showDialogSuccess();
                }

            }

            private void showDialogPending() {
                String dialogTitle, dialogMessage;
                dialogTitle = "Konfirmasi Pembayaran";
                dialogMessage = "Apakah pembayaran ini di terima.?\nJumlah Rp. "+jlhIuranx+"\n"+"Tanggal "+getWaktux;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(dialogTitle);
                alertDialogBuilder.setMessage(dialogMessage)
                        .setCancelable(true)
                        .setPositiveButton("Terima", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference2 = FirebaseDatabase.getInstance().getReference().child("Iuran").child(getNikx).child(getWaktux);
                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().child("status").setValue("Success");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                reference3 = FirebaseDatabase.getInstance().getReference().child("Pembayaran").child(getWaktux);
                                reference3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().child("status").setValue("Success");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Toast.makeText(context, "Pembayaran telah diterima.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, ConfirmPembayaran.class);
                                context.startActivity(intent);

                            }
                        }).setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference4 = FirebaseDatabase.getInstance().getReference().child("Iuran").child(getNikx).child(getWaktux);
                        reference4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("status").setValue("Ditolak");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        reference5 = FirebaseDatabase.getInstance().getReference().child("Pembayaran").child(getWaktux);
                        reference5.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("status").setValue("Ditolak");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(context, "Pembayaran ini ditolak.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ConfirmPembayaran.class);
                        context.startActivity(intent);

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


        });

    }

    @Override
    public int getItemCount() {
        return modConfirm.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photo_profile;
        TextView nama_lengkap, status, jlh_iuran, txt_bank, nama_peng, no_rek, waktu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo_profile = itemView.findViewById(R.id.photo_profile);
            nama_lengkap = itemView.findViewById(R.id.nama_lengkap);
            status = itemView.findViewById(R.id.status);
            jlh_iuran = itemView.findViewById(R.id.jlh_iuran);
            txt_bank = itemView.findViewById(R.id.txt_bank);
            nama_peng = itemView.findViewById(R.id.nama_peng);
            no_rek = itemView.findViewById(R.id.no_rek);
            waktu = itemView.findViewById(R.id.waktu);
        }
    }
}
