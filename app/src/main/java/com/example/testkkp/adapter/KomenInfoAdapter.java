package com.example.testkkp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testkkp.DetailClusterInfo;
import com.example.testkkp.MyHome;
import com.example.testkkp.R;
import com.example.testkkp.model.ModKomenInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KomenInfoAdapter extends RecyclerView.Adapter<KomenInfoAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModKomenInfo> modKomenInfo;
    DatabaseReference reference, reference2;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    public KomenInfoAdapter(Context c, ArrayList<ModKomenInfo> p) {
        this.context = c;
        this.modKomenInfo = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_komen_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        getNikLocal();
        holder.isi_komen.setText(modKomenInfo.get(position).getIsi_komen());
        final String nikx = modKomenInfo.get(position).getNik();
        final String idx = modKomenInfo.get(position).getId();
        final String id_infox = modKomenInfo.get(position).getId_info();

        if (nikx.equals(nik_key_new)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Hapus komentar ini.?")
                            .setCancelable(false)
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference2 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(id_infox).child("komen")
                                            .child(idx);
                                    reference2.removeValue();
                                    Toast.makeText(context, "Berhasil hapus komenter", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, DetailClusterInfo.class);
                                    intent.putExtra("id_info", id_infox);
                                    context.startActivity(intent);
                                }
                            }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                }
            });
        }



        holder.pic_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyHome.class);
                intent.putExtra("nik_bundle", nikx);
                context.startActivity(intent);
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nikx);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(context).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(holder.pic_user);
                holder.nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                holder.blok.setText("@"+snapshot.child("blok").getValue().toString()+" - "+modKomenInfo.get(position).getWaktu());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NIK_KEY, context.MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }

    @Override
    public int getItemCount() {
        return modKomenInfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView pic_user;
        TextView nama_lengkap, blok, isi_komen;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pic_user = itemView.findViewById(R.id.pic_user);
            nama_lengkap = itemView.findViewById(R.id.nama_lengkap);
            blok = itemView.findViewById(R.id.blok);
            isi_komen = itemView.findViewById(R.id.isi_komen);
        }
    }
}
