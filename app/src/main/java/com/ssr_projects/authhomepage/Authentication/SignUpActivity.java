package com.ssr_projects.authhomepage.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ssr_projects.authhomepage.MainActivity;
import com.ssr_projects.authhomepage.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String mEmail, mPasswordOne, mPasswordTwo, mName, EcontactNumber, EcontactEmail, EcontactName, medicalCond, bloodGroup;
    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Sign Up");

        final EditText mEmailText = findViewById(R.id.email_signup_text);
        final EditText mPasswordText = findViewById(R.id.password_signup_text);
        final EditText mPasswordTextTwo = findViewById(R.id.password2_signup_text);
        final EditText mNameText = findViewById(R.id.username_signup_text);
        final EditText EcontactNameText = findViewById(R.id.emergencyContactName_text);
        final EditText EcontactEmailText = findViewById(R.id.emergencyContactEmail_text);
        final EditText EcontactNumberText = findViewById(R.id.emergencyContactNumber_text);
        final EditText medicalcondText = findViewById(R.id.medicalConditions_text);
        final EditText bloodGroupText = findViewById(R.id.bloodGroup_text);

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                mEmail = mEmailText.getText().toString();
                mPasswordOne = mPasswordText.getText().toString();
                mPasswordTwo = mPasswordTextTwo.getText().toString();
                mName = mNameText.getText().toString();
                EcontactName = EcontactNameText.getText().toString();
                EcontactNumber = EcontactNumberText.getText().toString();
                EcontactEmail = EcontactEmailText.getText().toString();
                medicalCond = medicalcondText.getText().toString();
                bloodGroup = bloodGroupText.getText().toString();

                Log.e("TAG", "onClick:  " + mName + mEmail + mPasswordOne + mPasswordTwo );

                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if (mEmail.length() == 0 || mPasswordOne.length() == 0 || mPasswordTwo.length() == 0 || mName.length() == 0 || EcontactName.length()==0
                        || EcontactEmail.length()==0 || EcontactNumber.length()==0 || medicalCond.length() == 0 || bloodGroup.length() == 0) {
                    flag++;
                    Toast.makeText(SignUpActivity.this, "Fields(s) Empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (!pattern.matcher(mEmail).matches()) {
                        Toast.makeText(SignUpActivity.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                        flag++;
                    }

                    if (!pattern.matcher(EcontactEmail).matches()) {
                        Toast.makeText(SignUpActivity.this, "Invalid Emergency Email ID", Toast.LENGTH_SHORT).show();
                        flag++;
                    }

                    if (EcontactNumber.length() != 10) {
                        Toast.makeText(SignUpActivity.this, "Invalid Emergency Contact Number", Toast.LENGTH_SHORT).show();
                        flag++;
                    }

                    if (!mPasswordOne.equals(mPasswordTwo)) {
                        Toast.makeText(SignUpActivity.this, "Passwords Don't Match", Toast.LENGTH_SHORT).show();
                        flag++;
                    }

                }

                if (flag == 0) {
                    SignUpWithEmailAndPassword();
                }
            }
        });
    }

    void SignUpWithEmailAndPassword(){
        mAuth.createUserWithEmailAndPassword(mEmail,mPasswordOne).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUpActivity.this, "Email verification sent, please verify email", Toast.LENGTH_SHORT).show();

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("NAME")
                                            .setValue(mName);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("EMAIL")
                                            .setValue(mEmail);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("EMERGENCY NAME")
                                            .setValue(EcontactName);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("EMERGENCY EMAIL")
                                            .setValue(EcontactEmail);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("EMERGENCY NUMBER")
                                            .setValue(EcontactNumber);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("BLOOD GROUP")
                                            .setValue(bloodGroup);

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("MEDICAL CONDITION")
                                            .setValue(medicalCond);


                                    final Timer timer = new Timer();
                                    timer.scheduleAtFixedRate(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Log.e("Timer", "run: Not Verified" );
                                            mAuth.getCurrentUser().reload();
                                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                                Log.e("Timer", "run: Verified" );
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                timer.cancel();
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    }, 0, 1000);
                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}