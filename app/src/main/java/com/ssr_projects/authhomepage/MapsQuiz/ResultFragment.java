package com.ssr_projects.authhomepage.MapsQuiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.authhomepage.R;
import com.ssr_projects.authhomepage.VolleyResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ResultFragment extends Fragment {
    ArrayList<Boolean> result;
    ArrayList<Integer> number;
    ArrayList<String> ans;

    public ResultFragment(ArrayList<Boolean> result, ArrayList<Integer> number, ArrayList<String> ans) {
        this.result = result;
        this.number = number;
        this.ans = ans;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        VolleyResult volleyResult = new VolleyResult(getActivity());
        volleyResult.setGameType("map_quiz");
        volleyResult.setScore(result.toString());
        volleyResult.setUserId(FirebaseAuth.getInstance().getUid());
        volleyResult.build();
        volleyResult.start();

        TextView ResulttextView = view.findViewById(R.id.resultTextView);
        String s = "";
        for(int i = 0;i<3; i++){
            if(result.get(i)){
                s = s + "You guessed " + ans.get(number.get(i)) + " correct!\n";
            }
            else{
                s = s + "You guessed " + ans.get(number.get(i)) + " wrong!\n";
            }
            ResulttextView.setText(s);
        }

        return view;
    }
}