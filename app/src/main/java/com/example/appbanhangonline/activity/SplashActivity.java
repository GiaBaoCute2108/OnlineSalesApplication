package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appbanhangonline.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (Exception e) {

                } finally {
                    if (Paper.book().read("user") == null) {
                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(intent);
                    } else {
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                    }
                }
            }
        };
        thread.start();
    }
}