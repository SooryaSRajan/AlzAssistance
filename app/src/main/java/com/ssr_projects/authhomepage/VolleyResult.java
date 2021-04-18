package com.ssr_projects.authhomepage;

import android.app.Activity;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class VolleyResult {

    Activity activity;
    String URL = "http://alzquiz.pythonanywhere.com/test_post_data";
    JSONObject jsonObject;
    String mRequestBody;
    StringRequest stringRequest;

    RequestQueue requestQueue;

    public VolleyResult(Activity activity) {
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(activity);
        jsonObject = new JSONObject();

    }

    public void setGameType(String gameType) {
        try {
            jsonObject.put("type", gameType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setScore(String score) {
        try {
            jsonObject.put("answer", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUserId(String userId) {
        try {
            jsonObject.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void build(){
        mRequestBody = jsonObject.toString();
        Log.e(getClass().getName(), "build: " + mRequestBody );
        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                activity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                activity.finish();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                return mRequestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
    }

    public void start(){
        requestQueue.add(stringRequest);
        requestQueue.start();
    }
}
