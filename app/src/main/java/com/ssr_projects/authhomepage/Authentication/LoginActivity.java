package com.ssr_projects.authhomepage.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.ssr_projects.authhomepage.MainActivity;
import com.ssr_projects.authhomepage.R;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    String mEmail, mPasswordOne;
    int flag;
    EditText mEmailText, mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mEmailText = findViewById(R.id.user_email_text);
        mPasswordText = findViewById(R.id.user_password_text);

        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordOne = mPasswordText.getText().toString();
                mEmail = mEmailText.getText().toString();

                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if (mEmail.length() == 0 || mPasswordOne.length() == 0) {
                    flag++;
                    Toast.makeText(LoginActivity.this, "Fiels(s) Empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (!pattern.matcher(mEmail).matches()) {
                        Toast.makeText(LoginActivity.this, "Invalid Email ID", Toast.LENGTH_SHORT).show();
                        flag++;
                    }
                }

                if (flag == 0) {
                    SignInWithEmailAndPassword();
                }

            }
        });

    }

    void SignInWithEmailAndPassword(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail, mPasswordOne).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }
                                    String token = task.getResult();
                                    FirebaseDatabase.getInstance().getReference().child("TOKEN").child(token).setValue(FirebaseAuth.getInstance().getUid());
                                    Log.e("FIREBASE", "onComplete: " + token );
                                }
                            });


                    if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
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