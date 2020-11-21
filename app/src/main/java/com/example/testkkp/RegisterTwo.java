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
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwo extends AppCompatActivity {

    LinearLayout btn_back;
    Button button_continue, btn_add_photo;
    ImageView pic_photo_regis_user, icon_nopic;
    EditText no_telp, blok_rumah, nama_lengkap;

    Uri photo_location;
    Integer photo_max = 1;
    DatabaseReference reference;
    StorageReference storage;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getNikLocal();

        no_telp = findViewById(R.id.no_telp);
        blok_rumah = findViewById(R.id.blok_rumah);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        pic_photo_regis_user = findViewById(R.id.pic_photo_regis_user);
        icon_nopic = findViewById(R.id.icon_nopic);

        btn_add_photo = findViewById(R.id.btn_add_photo);
        button_continue = findViewById(R.id.button_continue);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_continue.setEnabled(false);
                button_continue.setText("Loading...");

                //simpan ke database
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(nik_key_new);

                //validasi photo
                if (photo_location != null){
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
                                    reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                    reference.getRef().child("blok").setValue(blok_rumah.getText().toString());
                                    reference.getRef().child("no_telp").setValue(no_telp.getText().toString());
                                }
                            });
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent intent = new Intent(RegisterTwo.this, SuccessRegister.class);
                            startActivity(intent);
                        }
                    });

                }else{
                    reference.getRef().child("url_photo_profile").setValue("https://firebasestorage.googleapis.com/v0/b/testkkp-c5338.appspot.com/o/Photousers%2Ficon_nopic.png?alt=media&token=80368039-9684-4ad5-9d70-462f40b3c682");
                    reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                    reference.getRef().child("blok").setValue(blok_rumah.getText().toString());
                    reference.getRef().child("no_telp").setValue(no_telp.getText().toString());

                    Intent intent = new Intent(RegisterTwo.this, SuccessRegister.class);
                    startActivity(intent);
                }
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_regis_user);
        }
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}