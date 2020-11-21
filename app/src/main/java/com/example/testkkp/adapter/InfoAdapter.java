package com.example.testkkp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testkkp.DetailClusterInfo;
import com.example.testkkp.MainActivity;
import com.example.testkkp.MyHome;
import com.example.testkkp.R;
import com.example.testkkp.model.ModInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EnumSet;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModInfo> modInfo;
    DatabaseReference reference, reference2, reference3;
    StorageReference storage;
    Integer jlh = 0;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    public InfoAdapter(Context c, ArrayList<ModInfo> p) {
        context = c;
        modInfo = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_info_cluster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        getNikLocal();
        final String nikx = modInfo.get(position).getNik();
        final String idx = modInfo.get(position).getId();
        holder.photo_info.setVisibility(View.GONE);

        holder.isi_info.setText(modInfo.get(position).getIsi_info());
        holder.waktu.setText(modInfo.get(position).getWaktu());

        if (modInfo.get(position).getUrl_photo_info() != null){
            holder.photo_info.setVisibility(View.VISIBLE);
            Picasso.with(context).load(modInfo.get(position).getUrl_photo_info())
                    .centerCrop().fit().into(holder.photo_info);
        }


        if (nikx.equals(nik_key_new)){
            holder.hapus.setVisibility(View.VISIBLE);
        }else{
            holder.hapus.setVisibility(View.GONE);
        }

        reference3 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx).child("komen");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jlh = (int) snapshot.getChildrenCount();
                holder.jlh_komen.setText(Integer.toString(jlh));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.pic_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyHome.class);
                intent.putExtra("nik_bundle", nikx);
                context.startActivity(intent);
            }
        });



        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Hapus info ini.?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference2 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(idx);
                                reference2.removeValue();

                                Toast.makeText(context.getApplicationContext(), "Info berhasil dihapus.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
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

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nikx);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(context).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(holder.pic_user);
                holder.nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                holder.blok.setText("@"+snapshot.child("blok").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailClusterInfo.class);
                intent.putExtra("id_info", idx);
                context.startActivity(intent);
            }
        });

    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NIK_KEY, Context.MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }

    @Override
    public int getItemCount() {
        return modInfo.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView pic_user, photo_info;
        TextView nama_lengkap, blok, isi_info, waktu, hapus, jlh_komen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pic_user = itemView.findViewById(R.id.pic_user);
            nama_lengkap = itemView.findViewById(R.id.nama_lengkap);
            blok = itemView.findViewById(R.id.blok);
            isi_info = itemView.findViewById(R.id.isi_info);
            waktu = itemView.findViewById(R.id.waktu);
            hapus = itemView.findViewById(R.id.hapus);
            jlh_komen = itemView.findViewById(R.id.jlh_komen);
            photo_info = itemView.findViewById(R.id.photo_info);
        }
    }
}
