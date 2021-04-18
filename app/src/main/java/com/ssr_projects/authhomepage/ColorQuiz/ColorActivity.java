package com.ssr_projects.authhomepage.ColorQuiz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ssr_projects.authhomepage.R;

public class ColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_quiz);

        getSupportActionBar().setTitle("Color Quiz");

        Fragment fragment = new ColourStartFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

    }
}