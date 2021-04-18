package com.ssr_projects.authhomepage.MapsQuiz;

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
import com.ssr_projects.authhomepage.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment {

    LatLng location;
    List<HashMap<String, Object>> dataList;

    public MapsFragment(List<HashMap<String, Object>> dataList, LatLng location) {
        this.dataList = dataList;
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mMapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.GoogleMaps);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                getActivity().findViewById(R.id.progress_circular).setVisibility(View.GONE);
                mMap.addMarker(new MarkerOptions().position((LatLng) Objects.requireNonNull(dataList.get(0).get("LATLANG"))).title((String) dataList.get(0).get("LOCATION")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position((LatLng) Objects.requireNonNull(dataList.get(1).get("LATLANG"))).title((String) dataList.get(1).get("LOCATION")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position((LatLng) Objects.requireNonNull(dataList.get(2).get("LATLANG"))).title((String) dataList.get(2).get("LOCATION")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).showInfoWindow();
                mMap.addMarker(new MarkerOptions().position((LatLng) Objects.requireNonNull(dataList.get(3).get("LATLANG"))).title((String) dataList.get(3).get("LOCATION")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
            }
        });

        Button startButton = v.findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fragment = new QuizFragment(dataList,location);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });
        return v;
    }
}