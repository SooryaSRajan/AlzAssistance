package com.ssr_projects.authhomepage.MathQuiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.ssr_projects.authhomepage.R;
import com.ssr_projects.authhomepage.VolleyResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BrainTimerActivity extends AppCompatActivity {

    Button go;
    ArrayList<Integer> options = new ArrayList<>();
    int RightAnswerPlace;
    TextView result;
    TextView scoreText;
    int score = 0;
    int round = 0;
    Button opt0;
    Button opt1;
    Button opt2;
    Button opt3;
    TextView timer;
    Button playAgainButton;
    ConstraintLayout GameLayout;

    @SuppressLint("SetTextI18n")
    public void PlayAgain(View view){
        score =0;
        round =0;
        timer.setText("30s");
        playAgainButton.setVisibility(View.INVISIBLE);
        opt0.setEnabled(true);
        opt1.setEnabled(true);
        opt2.setEnabled(true);
        opt3.setEnabled(true);
        scoreText.setText(score+ "/" + round);

        result.setText("");
        NewRound();

        new CountDownTimer(30100, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                String seconds = String.valueOf(millisUntilFinished/1000);
                timer.setText(seconds + "s");
            }

            @Override
            public void onFinish() {

                Map<String, Integer> hashMap = new HashMap<>();
                hashMap.put("SCORE", score);
                hashMap.put("TOTAL", round);
                VolleyResult volleyResult = new VolleyResult(BrainTimerActivity.this);
                volleyResult.setGameType("math_quiz");
                volleyResult.setScore(hashMap.toString());
                volleyResult.setUserId(FirebaseAuth.getInstance().getUid());
                volleyResult.build();
                volleyResult.start();

                result.setText("DONE!");
                playAgainButton.setVisibility(View.VISIBLE);
                opt0.setEnabled(false);
                opt1.setEnabled(false);
                opt2.setEnabled(false);
                opt3.setEnabled(false);
            }
        }.start();
    }

    public void startGame(View view){
        go.setVisibility(View.INVISIBLE);
        GameLayout.setVisibility(View.VISIBLE);
        go.setVisibility(View.INVISIBLE);
        PlayAgain(findViewById(R.id.resultTextView));
    }

    public void Answer(View view){
        if(Integer.toString(RightAnswerPlace).equals(view.getTag().toString())){
            result.setText("CORRECT!");
            score++;
        }
        else{
            result.setText("WRONG! :(");
        }
        round++;
        scoreText.setText(Integer.toString(score)+ "/" + Integer.toString(round));
        NewRound();
    }

    @SuppressLint("SetTextI18n")
    public void NewRound(){
        Random random = new Random();
        int a = random.nextInt(21);
        int b = random.nextInt(21);

        options.clear();
        TextView ques = findViewById(R.id.quesTextView);
        ques.setText(Integer.toString(a)+ " + " + Integer.toString(b));

        RightAnswerPlace = random.nextInt(4);

        for (int i=0; i<4; i++){
            int f = 0;
            if(RightAnswerPlace == i){
                options.add(a+b);
            }
            else{
                int wrongAnswer = random.nextInt(41);
                while(wrongAnswer==a+b || options.contains(wrongAnswer)){
                    wrongAnswer = random.nextInt(41);
                }
                options.add(wrongAnswer);
            }
        }
        opt0.setText(Integer.toString(options.get(0)));
        opt1.setText(Integer.toString(options.get(1)));
        opt2.setText(Integer.toString(options.get(2)));
        opt3.setText(Integer.toString(options.get(3)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_timer);

        getSupportActionBar().setTitle("Math Quiz");

        go = findViewById(R.id.Gobutton);
        opt0 = findViewById(R.id.button0);
        opt1 = findViewById(R.id.button1);
        opt2 = findViewById(R.id.button2);
        opt3 = findViewById(R.id.button3);
        result = findViewById(R.id.resultTextView);
        scoreText = findViewById(R.id.scoreTextView);
        timer = findViewById(R.id.timerTextView);
        playAgainButton = findViewById(R.id.playAgainbutton);
        GameLayout = findViewById(R.id.GameLayout);

        GameLayout.setVisibility(View.INVISIBLE);
        go.setVisibility(View.VISIBLE);
    }
}