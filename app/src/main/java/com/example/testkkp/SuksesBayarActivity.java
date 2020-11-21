package com.example.testkkp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuksesBayarActivity extends AppCompatActivity {

    Animation app_splash, btt, ttb;
    ImageView icon_success_ticket;
    TextView text_success_ticket, text_success_ticket2;
    Button button_view_iuran, btn_my_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_bayar);

        icon_success_ticket = findViewById(R.id.icon_success_ticket);
        text_success_ticket = findViewById(R.id.text_success_ticket);
        text_success_ticket2 = findViewById(R.id.text_success_ticket2);
        button_view_iuran = findViewById(R.id.button_view_iuran);
        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);

        //load anim
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        icon_success_ticket.startAnimation(app_splash);
        text_success_ticket.startAnimation(ttb);
        text_success_ticket2.startAnimation(ttb);
        button_view_iuran.startAnimation(btt);
        btn_my_dashboard.startAnimation(btt);

        button_view_iuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuksesBayarActivity.this, IuranActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuksesBayarActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SuksesBayarActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}