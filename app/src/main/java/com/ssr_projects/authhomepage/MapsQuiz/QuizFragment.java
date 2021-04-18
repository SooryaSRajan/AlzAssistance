package com.ssr_projects.authhomepage.MapsQuiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ssr_projects.authhomepage.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QuizFragment extends Fragment {
    int RightAnswerPlace;

    ArrayList<Boolean> Result = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<Integer> number = new ArrayList<>();
    ArrayList<String> ans = new ArrayList<>();

    List<HashMap<String, Object>> list;

    LatLng location;
    Button opt1;
    Button opt2;
    Button opt3;
    Button opt4;
    int i;
    int a = 0;
    GoogleMap mMap;

    public QuizFragment(List<HashMap<String, Object>> list, LatLng location) {
        this.list = list;
        this.location = location;
        i = 0;

        for (int i = 0; i<4; i++){
            ans.add(list.get(i).get("LOCATION").toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        opt1 = view.findViewById(R.id.opt1);
        opt2 = view.findViewById(R.id.opt2);
        opt3 = view.findViewById(R.id.opt3);
        opt4 = view.findViewById(R.id.opt4);

        SupportMapFragment mMapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.quizmap);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                i++;
                for (int j = 0; j <= 3; ++j) number.add(j);
                Collections.shuffle(number);
                NewRound();
            }
        });

        return view;
    }

    public void NewRound() {
        Random random = new Random();

        mMap.addMarker(new MarkerOptions().position((LatLng) list.get(number.get(a)).get("LATLANG")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));

        RightAnswerPlace = random.nextInt(4);

        int j;
        for (int i = 0; i < 4; i++) {
            j = i;
            if (RightAnswerPlace == i) {
                options.add(ans.get(number.get(a)));
            } else {
                while (ans.get(number.get(a)).equals(ans.get(j)) || options.contains(ans.get(j))) {
                    j++;
                    if (j > 3) {
                        j = 0;
                    }
                }
                options.add(ans.get(j));
            }
        }
        Log.i("a = ", Integer.toString(number.get(a)));
        Log.i("Answers : ", ans.toString());
        Log.i("Options: ", options.toString());
        Log.i("RightAnswer: ", Integer.toString(RightAnswerPlace));
        opt1.setText(options.get(0));
        opt2.setText(options.get(1));
        opt3.setText(options.get(2));
        opt4.setText(options.get(3));
        a++;

        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer(v);
            }
        });

        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer(v);
            }
        });

        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer(v);
            }
        });

        opt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer(v);
            }
        });

    }

    private void Answer(View v) {
        if (Integer.toString(RightAnswerPlace).equals(v.getTag().toString())) {
            Result.add(true);
        } else {
            Result.add(false);
        }
        ClearRound();
        i++;
        if (i <= 3) {
            NewRound();
        } else {
            Fragment fragment = new ResultFragment(Result, number, ans);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        }
    }

    private void ClearRound() {
        options.clear();
        mMap.clear();
    }
}