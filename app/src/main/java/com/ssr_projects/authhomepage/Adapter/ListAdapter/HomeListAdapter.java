package com.ssr_projects.authhomepage.Adapter.ListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssr_projects.authhomepage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class HomeListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> arrayList;
    private Activity activity;

    public HomeListAdapter(ArrayList<HashMap<String, String>> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(activity);

        if(i == 0){
            view = inflater.inflate(R.layout.top_item, viewGroup, false);
            final TextView textView = view.findViewById(R.id.home_page_greeting);


            FirebaseDatabase.getInstance().getReference().child("USER").child(FirebaseAuth.getInstance().getUid())
                    .child("NAME")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            int currentTime = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));

                            if(currentTime >= 4 && currentTime < 12){
                                textView.setText("Good Morning\n" + snapshot.getValue());
                            }

                            else if(currentTime >= 12 && currentTime < 18){
                                textView.setText("Good AfterNoon\n" + snapshot.getValue());

                            }

                            else if(currentTime >= 18){
                                textView.setText("Good Evening\n" + snapshot.getValue());
                            }

                            Log.e("TIME ", "onCreateView: " + currentTime );


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            return view;
        }

        view = inflater.inflate(R.layout.list_item , viewGroup, false);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {getRandomColor(),getRandomColor()});

        TextView title = view.findViewById(R.id.title);
        TextView subTitle = view.findViewById(R.id.sub_title);

        title.setText(arrayList.get(i).get("TITLE"));
        subTitle.setText(arrayList.get(i).get("SUB"));

        view.findViewById(R.id.card_view).setBackground(gd);


        return view;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
