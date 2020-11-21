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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateInfo extends AppCompatActivity {

    ImageView btn_back, pic_user, photo_info;
    Button btn_post_info, btn_camera;
    EditText isi_info;

    DatabaseReference reference, reference3;
    StorageReference storage;
    Uri photo_location;
    Integer photo_max = 1;

    String NIK_KEY = "nikkey";
    String nik_key = "";
    String nik_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_info);

        getNikLocal();

        pic_user = findViewById(R.id.pic_user);
        isi_info = findViewById(R.id.isi_info);
        photo_info = findViewById(R.id.photo_info);
        btn_camera = findViewById(R.id.btn_camera);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(nik_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.with(UpdateInfo.this).load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(pic_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_post_info = findViewById(R.id.btn_post_info);
        btn_post_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_post_info.setEnabled(false);
                btn_post_info.setText("Wait...");

                final String isi_infox = isi_info.getText().toString();
                final String id = penomoran();
                reference3 = FirebaseDatabase.getInstance().getReference().child("InfoCluster").child(id);
                storage = FirebaseStorage.getInstance().getReference().child("PhotoInfo").child(id);

                if (isi_infox.isEmpty()) {
                    btn_post_info.setEnabled(true);
                    btn_post_info.setText("POST");
                    Toast.makeText(getApplicationContext(), "Info Cluster tidak dapat di POST.", Toast.LENGTH_SHORT).show();
                }else {

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
                                        reference3.getRef().child("id").setValue(id);
                                        reference3.getRef().child("nik").setValue(nik_key_new);
                                        reference3.getRef().child("waktu").setValue(getDateTime());
                                        reference3.getRef().child("isi_info").setValue(isi_infox);
                                        reference3.getRef().child("url_photo_info").setValue(uri_photo);

                                        Toast.makeText(getApplicationContext(), "Data berhasil di post.", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                });
                            }
                        });

                    }else{
                        reference3.getRef().child("id").setValue(id);
                        reference3.getRef().child("nik").setValue(nik_key_new);
                        reference3.getRef().child("waktu").setValue(getDateTime());
                        reference3.getRef().child("isi_info").setValue(isi_infox);
//                        reference3.getRef().child("url_photo_info").setValue("kosong");
                        Toast.makeText(getApplicationContext(), "Data berhasil di post.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                }
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

    private void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_info);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateInfo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String penomoran() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dMyyyyHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy, HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getNikLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(NIK_KEY, MODE_PRIVATE);
        nik_key_new = sharedPreferences.getString(nik_key, "");
    }
}