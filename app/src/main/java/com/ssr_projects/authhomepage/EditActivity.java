package com.ssr_projects.authhomepage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.authhomepage.Authentication.SignUpActivity;

import java.util.Objects;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    String mName, EcontactNumber, EcontactEmail, EcontactName, medicalCond, bloodGroup;
    int flag;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        final EditText mNameText = findViewById(R.id.username_signup_text);
        final EditText EcontactNameText = findViewById(R.id.emergencyContactName_text);
        final EditText EcontactEmailText = findViewById(R.id.emergencyContactEmail_text);
        final EditText EcontactNumberText = findViewById(R.id.emergencyContactNumber_text);
        final EditText medicalcondText = findViewById(R.id.medicalConditions_text);
        final EditText bloodGroupText = findViewById(R.id.bloodGroup_text);

        FirebaseDatabase.getInstance().getReference().child("USER")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        mNameText.setText(snapshot.child("NAME").getValue().toString());
                        EcontactNameText.setText(snapshot.child("EMERGENCY NAME").getValue().toString());
                        EcontactEmailText.setText(snapshot.child("EMERGENCY EMAIL").getValue().toString());
                        EcontactNumberText.setText(snapshot.child("EMERGENCY NUMBER").getValue().toString());
                        medicalcondText.setText(snapshot.child("MEDICAL CONDITION").getValue().toString());
                        bloodGroupText.setText(snapshot.child("BLOOD GROUP").getValue().toString());

                        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flag = 0;
                                mName = mNameText.getText().toString();
                                EcontactName = EcontactNameText.getText().toString();
                                EcontactNumber = EcontactNumberText.getText().toString();
                                EcontactEmail = EcontactEmailText.getText().toString();
                                medicalCond = medicalcondText.getText().toString();
                                bloodGroup = bloodGroupText.getText().toString();

                                Pattern pattern = Patterns.EMAIL_ADDRESS;

                                if (mName.length() == 0 || EcontactName.length()==0
                                        || EcontactEmail.length()==0 || EcontactNumber.length()==0 || medicalCond.length() == 0 || bloodGroup.length() == 0) {
                                    flag++;
                                    Toast.makeText(EditActivity.this, "Fields(s) Empty", Toast.LENGTH_SHORT).show();
                                }

                                else {

                                    if (!pattern.matcher(EcontactEmail).matches()) {
                                        Toast.makeText(EditActivity.this, "Invalid Emergency Email ID", Toast.LENGTH_SHORT).show();
                                        flag++;
                                    }

                                    if (EcontactNumber.length() != 10) {
                                        Toast.makeText(EditActivity.this, "Invalid Emergency Contact Number", Toast.LENGTH_SHORT).show();
                                        flag++;
                                    }


                                }
                                if(flag == 0){

                                    Toast.makeText(EditActivity.this, "Changes have been updated" +
                                            "", Toast.LENGTH_SHORT).show();

                                    FirebaseDatabase.getInstance().getReference().child("USER")
                                            .child(Objects.requireNonNull(mAuth.getUid()))
                                            .child("NAME")
                                            .setValue(mName);

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

                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}