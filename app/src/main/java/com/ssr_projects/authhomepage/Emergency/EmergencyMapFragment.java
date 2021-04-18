package com.ssr_projects.authhomepage.Emergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssr_projects.authhomepage.MapsQuiz.QuizFragment;
import com.ssr_projects.authhomepage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EmergencyMapFragment extends Fragment {

    LatLng location;
    JSONArray jsonArray;

    public EmergencyMapFragment(JSONArray jsonArray, LatLng location) {
        this.jsonArray = jsonArray;
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_emergency_map, container, false);

        SupportMapFragment mMapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.GoogleMaps);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                v.findViewById(R.id.progress_circular).setVisibility(View.GONE);

                mMap.addMarker(new MarkerOptions().position(location).title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                for(int i = 0; i < jsonArray.length(); i++){
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lon"))).title(jsonObject.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            }
        });
        return v;
    }
}