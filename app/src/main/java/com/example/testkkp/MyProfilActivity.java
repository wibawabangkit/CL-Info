package com.example.testkkp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MyProfilActivity extends AppCompatActivity {

    ImageView photo_profile;
    Button btn_add_new_photo, button_edit_profile, btn_simpan_edit, btn_dash;
    TextView my_nik, nama_lengkap, email, blok, no_telp;
    EditText nama_lengkapx, emailx, blokx, no_telpx;

    DatabaseReference reference;
    Uri photo_location;
    Integer photo_max = 1;
    StorageReference storage;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        getNikLocal();

        btn_add_new_photo = findViewById(R.id.btn_add_new_photo);
        btn_add_new_photo.setVisibility(View.GONE);

        btn_simpan_edit = findViewById(R.id.btn_simpan_edit);
        btn_simpan_edit.setVisibility(View.GONE);

        nama_lengkapx = findViewById(R.id.nama_lengkapx);
        nama_lengkapx.setVisibility(View.GONE);

        emailx = findViewById(R.id.emailx);
        emailx.setVisibility(View.GONE);

        blokx = findViewById(R.id.blokx);
        blokx.setVisibility(View.GONE);

        no_telpx = findViewById(R.id.no_telpx);
        no_telpx.setVisibility(View.GONE);

        photo_profile = findViewById(R.id.photo_profile);
        my_nik = findViewById(R.id.my_nik);
        my_nik.setText(nik_key_new);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        email = findViewById(R.id.email);
        blok = findViewById(R.id.blok);
        no_telp = findViewById(R.id.no_telp);

        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(nik_key_new);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(MyProfilActivity.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_profile);
                nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                nama_lengkapx.setText(snapshot.child("nama_lengkap").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                emailx.setText(snapshot.child("email").getValue().toString());
                blok.setText(snapshot.child("blok").getValue().toString());
                blokx.setText(snapshot.child("blok").getValue().toString());
                no_telp.setText(snapshot.child("no_telp").getValue().toString());
                no_telpx.setText(snapshot.child("no_telp").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_edit_profile = findViewById(R.id.button_edit_profile);
        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_add_new_photo.setVisibility(View.VISIBLE);
                button_edit_profile.setVisibility(View.GONE);
                btn_simpan_edit.setVisibility(View.VISIBLE);
                btn_dash.setVisibility(View.GONE);
                nama_lengkap.setVisibility(View.GONE);
                nama_lengkapx.setVisibility(View.VISIBLE);
                email.setVisibility(View.GONE);
                emailx.setVisibility(View.VISIBLE);
                blok.setVisibility(View.GONE);
                blokx.setVisibility(View.VISIBLE);
                no_telp.setVisibility(View.GONE);
                no_telpx.setVisibility(View.VISIBLE);

                btn_add_new_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findPhoto();
                    }
                });

                btn_simpan_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        btn_simpan_edit.setEnabled(false);
                        btn_simpan_edit.setText("Loading...");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("nama_lengkap").setValue(nama_lengkapx.getText().toString());
                                snapshot.getRef().child("email").setValue(emailx.getText().toString());
                                snapshot.getRef().child("blok").setValue(blokx.getText().toString());
                                snapshot.getRef().child("no_telp").setValue(no_telpx.getText().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if (photo_location != null) {
                            final StorageReference storageReference=storage.child(System.currentTimeMillis()+"."+
                                    getFileExtension(photo_location));

                            storageReference.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        }
                                    });
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Intent intent = new Intent(MyProfilActivity.this, MyProfilActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }else {
                            Intent intent = new Intent(MyProfilActivity.this, MyProfilActivity.class);
                            startActivity(intent);
                        }

                    }
                });

            }
        });

        btn_dash = findViewById(R.id.btn_dash);
        btn_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfilActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyProfilActivity.this, MyHome.class);
        intent.putExtra("nik_bundle", nik_key_new);
        startActivity(intent);
        finish();
    }

    private void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_profile);
        }
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}