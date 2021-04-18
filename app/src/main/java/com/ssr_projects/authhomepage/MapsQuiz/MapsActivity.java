package com.ssr_projects.authhomepage.MapsQuiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.authhomepage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private final String serverURL = "http://alzquiz.pythonanywhere.com/map_questions";
    RequestQueue queue;
    private String TAG = getClass().getName();
    List<HashMap<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_quiz);

        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle("Maps Quiz");

        dataList = new ArrayList<>();

        final StringRequest request = new StringRequest(
                Request.Method.GET,
                serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, "onResponse: Object" + jsonObject );
                            Log.e(TAG, "onResponse: Lat" + jsonObject.getDouble("lat") );
                            Log.e(TAG, "onResponse: Long" + jsonObject.getDouble("long") );
                            Log.e(TAG, "onResponse: Quiz Location" + jsonObject.getJSONArray("locations") );

                            LatLng location = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("long"));

                            JSONArray jsonArray = jsonObject.getJSONArray("locations");

                            JSONObject arrayJSONObject;
                            LatLng latLng;
                            HashMap<String, Object> hashMap;

                            for(int i = 0; i<4; i++){
                                arrayJSONObject = jsonArray.getJSONObject(i);
                                Log.e(TAG, "onResponse: Iteration through json array" +  jsonArray.getJSONObject(i));

                                latLng = new LatLng(arrayJSONObject.getDouble("lat"), arrayJSONObject.getDouble("long"));

                                hashMap = new HashMap<>();
                                hashMap.put("LATLANG", latLng);
                                hashMap.put("LOCATION", arrayJSONObject.getString("name"));

                                dataList.add(hashMap);

                            }

                            Fragment fragment = new MapsFragment(dataList, location);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponseError: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error);
                        try {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                        catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

        queue.add(request);
        queue.start();


    }
}