package com.ssr_projects.authhomepage.Quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.authhomepage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        final ArrayList<Boolean> answers = new ArrayList<>();

        final String requestURL = "http://alzquiz.pythonanywhere.com/get_questions_small" + "?user_id=" + FirebaseAuth.getInstance().getUid();
        getSupportActionBar().setTitle("Word Quiz");

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Volley", "onResponse: " + response );
                            JSONObject jsonArray = new JSONObject(response);
                            Fragment fragment = new QuizFragment(count, jsonArray, answers);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                    }
                });



        queue.add(request);
        queue.start();


    }
}