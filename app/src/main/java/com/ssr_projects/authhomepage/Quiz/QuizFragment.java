package com.ssr_projects.authhomepage.Quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizFragment extends Fragment {

    ProgressBar progressBar;
    String ques;
    HashMap<String, String> Answers = new HashMap();
    ArrayList<Boolean> answersRight;
    JSONObject jsonObject;

    int noOfoptions;
    String rightkey;
    Button[] opt = new Button[4];
    Button next;
    TextView question;
    int count;

    public QuizFragment(int count, JSONObject jsonObject, ArrayList<Boolean> answersRight) {
        this.count = count;
        this.jsonObject = jsonObject;
        this.answersRight = answersRight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_quiz_main, container, false);
        progressBar = view.findViewById(R.id.progress_circular_bar);

        TextView questionNo = view.findViewById(R.id.question_no);
        questionNo.setText((count + 1) + ". ");

        final String requestURL;

        requestURL = "http://alzquiz.pythonanywhere.com/get_questions_big";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final StringRequest request = new StringRequest(
                Request.Method.GET,
                requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response);

                            Log.e("TAG", "onResponse: " + jsonObject );

                            rightkey = jsonObject.getString("correct_opt");
                            noOfoptions = jsonObject.getInt("no_of_options");
                            ques = jsonObject.getString("question");

                            JSONObject optionsObject = jsonObject.getJSONObject("options");

                            String optionString;

                            opt[0] = view.findViewById(R.id.opt1);
                            opt[1]= view.findViewById(R.id.opt2);
                            opt[2]= view.findViewById(R.id.opt3);
                            opt[3] = view.findViewById(R.id.opt4);

                            for(int i = 0; i < noOfoptions; i++){
                                optionString = "Option" + (i + 1);
                                Log.e("TAG", "onResponse: " + noOfoptions + " " + optionString + " " +  optionsObject.getString(optionString));
                                Answers.put(String.valueOf(i + 1), optionsObject.getString(optionString));
                            }

                            next = view.findViewById(R.id.nextButton);

                            question = view.findViewById(R.id.QuestionTextView);
                            question.setText(ques);

                            int i =0;
                            List<Map.Entry<String, String>> list = new ArrayList<>(Answers.entrySet());
                            for (Map.Entry<String, String> entry : list) {
                                opt[i].setText(entry.getValue());
                                opt[i].setVisibility(View.VISIBLE);
                                i++;
                            }
                            opt[0].setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    CheckAnswer(v);
                                }
                            });

                            opt[1].setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    CheckAnswer(v);
                                }
                            });

                            opt[2].setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    CheckAnswer(v);
                                }
                            });

                            opt[3].setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    CheckAnswer(v);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().finish();
                    }
                });

        if((count + 1) % 3 == 0){
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObjectMain;

                Log.e("TAG" , "onCreateView: " +  ((count + 1)/3) + 1);

                jsonObjectMain = jsonObject.getJSONObject("Question" + ((count + 1)/3));

                rightkey = jsonObjectMain.getString("correct_opt");
                noOfoptions = jsonObjectMain.getInt("no_of_options");
                ques = jsonObjectMain.getString("question");

                JSONObject optionsObject = jsonObjectMain.getJSONObject("options");

                String optionString;
                for(int i = 0; i < noOfoptions; i++){
                    optionString = "Option" + (i + 1);
                    Answers.put(String.valueOf(i + 1), optionsObject.getString(optionString));
                }

                opt[0] = view.findViewById(R.id.opt1);
                opt[1]= view.findViewById(R.id.opt2);
                opt[2]= view.findViewById(R.id.opt3);
                opt[3] = view.findViewById(R.id.opt4);

                next = view.findViewById(R.id.nextButton);

                question = view.findViewById(R.id.QuestionTextView);
                question.setText(ques);

                int i =0;
                List<Map.Entry<String, String>> list = new ArrayList<>(Answers.entrySet());
                for (Map.Entry<String, String> entry : list) {
                    opt[i].setText(entry.getValue());
                    opt[i].setVisibility(View.VISIBLE);
                    i++;
                }
                opt[0].setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        CheckAnswer(v);
                    }
                });

                opt[1].setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        CheckAnswer(v);
                    }
                });

                opt[2].setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        CheckAnswer(v);
                    }
                });

                opt[3].setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        CheckAnswer(v);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                getActivity().finish();
            }
        }

        else{
            queue.add(request);
            queue.start();
        }


        return view;
    }

    private void CheckAnswer(View view) {
        Button v = (Button) view;
        if(v.getText().toString().equals(Answers.get(rightkey))){
            v.setBackgroundColor(Color.GREEN);
            if((count + 1) % 3 == 0) {
                answersRight.add(true);
            }
        }
        else{
            Log.e("TAG", "CheckAnswer: " + rightkey );
            opt[Integer.parseInt(rightkey) - 1].setBackgroundColor(Color.GREEN);
            v.setBackgroundColor(Color.RED);
            if((count + 1) % 3 == 0) {
                answersRight.add(false);
            }
        }

        for(int i = 0; i<noOfoptions ; i++){
            opt[i].setEnabled(false);
        }
        next.setVisibility(View.VISIBLE);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(count != 14){
                    count ++;
                    Fragment fragment = new QuizFragment(count, jsonObject, answersRight);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
                }
                else{
                    Log.e("TAG", "onClick: Answers: " + answersRight);

                    VolleyResult volleyResult = new VolleyResult(getActivity());
                    volleyResult.setGameType("word_quiz");
                    volleyResult.setScore(answersRight.toString());
                    volleyResult.setUserId(FirebaseAuth.getInstance().getUid());
                    volleyResult.build();
                    volleyResult.start();

                }
            }
        });
    }
}