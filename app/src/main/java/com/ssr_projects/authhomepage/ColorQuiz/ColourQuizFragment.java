package com.ssr_projects.authhomepage.ColorQuiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssr_projects.authhomepage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ColourQuizFragment extends Fragment {

    ImageView imageView;
    final String TAG = getClass().getName();

    CheckBox ch1;
    CheckBox ch2;
    CheckBox ch3;
    CheckBox ch4;
    CheckBox ch5;
    ProgressBar progressBar;
    ArrayList<Boolean> res = new ArrayList<>();
    HashMap<String, String> colourValues = new HashMap();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colour_quiz, container, false);

        imageView = view.findViewById(R.id.imageView);
        progressBar = view.findViewById(R.id.progress_circular_bar);

        ch1 = view.findViewById(R.id.checkbox1);
        ch2 = view.findViewById(R.id.checkbox2);
        ch3 = view.findViewById(R.id.checkbox3);
        ch4 = view.findViewById(R.id.checkbox4);
        ch5 = view.findViewById(R.id.checkbox5);
        final Button doneButton = view.findViewById(R.id.ResultButton);
        doneButton.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final StringRequest request = new StringRequest(
                Request.Method.GET,
                "http://alzquiz.pythonanywhere.com/imgs_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, "onResponse: " + jsonObject );
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            String imageURL = "Data_arr/" + jsonObject.getString("Image_Name");
                            StorageReference imageRef = storageRef.child(imageURL);
                            final long ONE_MEGABYTE = 1024 * 1024;

                            colourValues.put("Blue", jsonObject.getString("Blue"));
                            colourValues.put("Green", jsonObject.getString("Green"));
                            colourValues.put("Red", jsonObject.getString("Red"));
                            colourValues.put("Violet", jsonObject.getString("Violet"));
                            colourValues.put("Yellow", jsonObject.getString("Yellow"));

                            imageRef.getBytes(ONE_MEGABYTE)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            doneButton.setVisibility(View.VISIBLE);
                                            Log.e(TAG, "onSuccess: Success" );
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            progressBar.setVisibility(View.GONE);
                                            imageView.setImageBitmap(bitmap);

                                            Set<String> keys = colourValues.keySet();
                                            Iterator<String> it = keys.iterator();

                                            ch1.setText(it.next());
                                            ch2.setText(it.next());
                                            ch3.setText(it.next());
                                            ch4.setText(it.next());
                                            ch5.setText(it.next());

                                            doneButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String s = "";
                                                    if (ch1.isChecked() && colourValues.get(ch1.getText()).equals("1")) {
                                                        s = s + "You got " + ch1.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else if(!ch1.isChecked() && colourValues.get(ch1.getText()).equals("0")){
                                                        s = s + "You got " + ch1.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else {
                                                        s = s + "You got " + ch1.getText() + " wrong!\n";
                                                    }


                                                    if (ch2.isChecked() && colourValues.get(ch2.getText()).equals("1")) {
                                                        s = s + "You got " + ch2.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else if(!ch2.isChecked() && colourValues.get(ch2.getText()).equals("0")){
                                                        s = s + "You got " + ch2.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else {
                                                        s = s + "You got " + ch2.getText() + " wrong!\n";
                                                        res.add(false);
                                                    }


                                                    if (ch3.isChecked() && colourValues.get(ch3.getText()).equals("1")) {
                                                        s = s + "You got " + ch3.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else if(!ch3.isChecked() && colourValues.get(ch3.getText()).equals("0")){
                                                        s = s + "You got " + ch3.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else {
                                                        s = s + "You got " + ch3.getText() + " wrong!\n";
                                                        res.add(false);
                                                    }


                                                    if (ch4.isChecked() && colourValues.get(ch4.getText()).equals("1")) {
                                                        s = s + "You got " + ch4.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else if(!ch4.isChecked() && colourValues.get(ch4.getText()).equals("0")){
                                                        s = s + "You got " + ch4.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else {
                                                        s = s + "You got " + ch4.getText() + " wrong!\n";
                                                        res.add(false);
                                                    }
                                                    if (ch5.isChecked() && colourValues.get(ch5.getText()).equals("1")) {
                                                        s = s + "You got " + ch5.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else if(!ch5.isChecked() && colourValues.get(ch5.getText()).equals("0")){
                                                        s = s + "You got " + ch5.getText() + " correct!\n";
                                                        res.add(true);
                                                    }
                                                    else {
                                                        s = s + "You got " + ch5.getText() + " wrong!\n";
                                                        res.add(false);
                                                    }

                                                    Fragment fragment = new ColourResultFragment(s, res);
                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                                                }
                                            });



                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: " + e );
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Image Download Failed", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    });




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponseError: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error);
                        try {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                        catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

        queue.add(request);
        queue.start();


        return view;
    }
}