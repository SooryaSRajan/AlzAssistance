package com.ssr_projects.authhomepage.ColorQuiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ssr_projects.authhomepage.R;

public class ColourStartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_colour_start, container, false);

        Button startButton = view.findViewById(R.id.startbutton);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment fragment = new ColourQuizFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });
        return view;
    }
}