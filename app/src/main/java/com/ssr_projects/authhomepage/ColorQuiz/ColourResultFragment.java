package com.ssr_projects.authhomepage.ColorQuiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.authhomepage.R;
import com.ssr_projects.authhomepage.VolleyResult;

import java.util.ArrayList;

public class ColourResultFragment extends Fragment {

    String s;
    ArrayList<Boolean> res;
    public ColourResultFragment(String s, ArrayList<Boolean> res) {
        this.s = s;
        this.res = res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_colour_result, container, false);
        TextView ResultTextView = v.findViewById(R.id.resultTextView);
        ResultTextView.setText(s);

        VolleyResult volleyResult = new VolleyResult(getActivity());
        volleyResult.setGameType("colour_quiz");
        volleyResult.setScore(res.toString());
        volleyResult.setUserId(FirebaseAuth.getInstance().getUid());
        volleyResult.build();
        volleyResult.start();


        return v;
    }
}