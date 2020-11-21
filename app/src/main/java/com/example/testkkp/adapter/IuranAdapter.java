package com.example.testkkp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testkkp.IuranActivity;
import com.example.testkkp.MyDetailIuranActivity;
import com.example.testkkp.R;
import com.example.testkkp.model.ModIuran;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class IuranAdapter extends RecyclerView.Adapter<IuranAdapter.MyViewHolder> {

    Context context;
    ArrayList<ModIuran> modIuran;
    DatabaseReference reference, reference2;

    public IuranAdapter(Context c, ArrayList<ModIuran> p) {
        context = c;
        modIuran = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_iuran, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.xwaktu.setText(modIuran.get(position).getWaktu());
        holder.xjlh_iuran.setText("Rp. "+modIuran.get(position).getJlh_iuran());
        holder.xstatus.setText(modIuran.get(position).getStatus());

        final String getStatus = modIuran.get(position).getStatus();
        final String getWaktu = modIuran.get(position).getWaktu();
        final String getJlh_iuran = modIuran.get(position).getJlh_iuran();
        final String Success = "Success";
        final String nik = modIuran.get(position).getNik();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (getStatus.equals(Success)) {
                    Intent intent = new Intent(context, MyDetailIuranActivity.class);
                    intent.putExtra("waktu", getWaktu);
                    intent.putExtra("status", getStatus);
                    intent.putExtra("jlh_iuran", getJlh_iuran);
                    context.startActivity(intent);
                }else if (getStatus.equals("Ditolak")){
                    String dialogTitle, dialogMessage;
                    dialogTitle = "Info Pembayaran";
                    dialogMessage = "Pembayaran anda ditolak.";

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle(dialogTitle);
                    alertDialogBuilder.setMessage(dialogMessage)
                            .setCancelable(false)
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference = FirebaseDatabase.getInstance().getReference().child("Iuran").child(nik).child(getWaktu);
                                    reference.removeValue();

                                    reference2 = FirebaseDatabase.getInstance().getReference().child("Pembayaran").child(getWaktu);
                                    reference2.removeValue();

                                    Toast.makeText(context, "Data telah dihapus.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, IuranActivity.class);
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

                }else {
                    Toast.makeText(context, "Pembayaran Sedang Di Proses.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modIuran.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView xwaktu, xjlh_iuran, xstatus;

        public MyViewHolder(View view) {
            super(view);

            xwaktu = view.findViewById(R.id.xwaktu);
            xjlh_iuran = view.findViewById(R.id.xjlh_iuran);
            xstatus = view.findViewById(R.id.xstatus);
        }
    }
}
