package com.ssr_projects.authhomepage.Emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ssr_projects.authhomepage.MainActivity;
import com.ssr_projects.authhomepage.R;

import java.util.Timer;
import java.util.TimerTask;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        final TextView countDown = findViewById(R.id.count);
        Button cancelButton = findViewById(R.id.cancel);

        getSupportActionBar().hide();


        int DELAY = 1000;
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int counter = 5;

            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                countDown.setText("(" + counter + "s)");
                            }
                        });
                    }
                });
                if (--counter == 0) {
                    timer.cancel();
                    Intent intent = new Intent(getApplication(), EmergencyLauncherActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                finish();
            }
        });
        timer.schedule(task, DELAY, DELAY);

    }

    @Override
    public void onBackPressed() {

    }
}