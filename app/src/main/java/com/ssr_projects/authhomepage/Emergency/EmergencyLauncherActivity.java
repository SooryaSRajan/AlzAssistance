package com.ssr_projects.authhomepage.Emergency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.authhomepage.R;

import org.json.JSONArray;
import org.json.JSONException;

public class EmergencyLauncherActivity extends AppCompatActivity {
    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_launcher);

        getSupportActionBar().hide();

        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                "The red markers are hospitals near you, the blue marker is your current location",
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();

        Intent myIntent = new Intent(this, SosService.class);
        myIntent.setAction("ACTION_ENABLE_SOS");
        this.startService(myIntent);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            /**Here**/
                            Log.e("Fused", "onComplete: " + location);
                            getHospitals(location);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            getHospitals(mLastLocation);
            Log.e("LocationCallBack", "onComplete: " + mLastLocation);
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    void getHospitals(final Location location) {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String URL = "http://alzquiz.pythonanywhere.com/emergency_hospital_location?lat=VAR1&long=VAR2";

        URL = URL.replace("VAR1", String.valueOf(location.getLatitude()));
        URL = URL.replace("VAR2", String.valueOf(location.getLongitude()));

        Log.e(getClass().getName(), "getHospitals: URL: " + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e(getClass().getName(), "onResponse: " + response);

                            Fragment fragment = new EmergencyMapFragment(jsonArray, new LatLng(location.getLatitude(), location.getLongitude()));
                            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("USER")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            SmsManager sms = SmsManager.getDefault();
                                            sms.sendTextMessage(
                                                    snapshot.child("EMERGENCY NUMBER")
                                                            .getValue()
                                                            .toString(),
                                                    null,
                                                    snapshot
                                                            .child("EMERGENCY NAME")
                                                            .getValue()
                                                            .toString()
                                                            + ", Your ward " + snapshot
                                                            .child("NAME")
                                                            .getValue()
                                                            .toString()
                                                            + " has reported an emergency at location: " +
                                                            "https://www.google.com/maps/search/?api=1&query="
                                                            + location.getLatitude() + "," + location.getLongitude()
                                                    , null, null);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(stringRequest);
        requestQueue.start();
    }


}