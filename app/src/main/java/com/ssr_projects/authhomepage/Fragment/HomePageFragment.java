package com.ssr_projects.authhomepage.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.ssr_projects.authhomepage.Adapter.ListAdapter.HomeListAdapter;
import com.ssr_projects.authhomepage.ColorQuiz.ColorActivity;
import com.ssr_projects.authhomepage.Emergency.EmergencyActivity;
import com.ssr_projects.authhomepage.MapsQuiz.MapsActivity;
import com.ssr_projects.authhomepage.MathQuiz.BrainTimerActivity;
import com.ssr_projects.authhomepage.Quiz.QuizActivity;
import com.ssr_projects.authhomepage.R;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePageFragment extends Fragment {


    public HomePageFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        Button emergencyButton = view.findViewById(R.id.emergency);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmergencyActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = view.findViewById(R.id.list_view);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("TITLE", "NULL");
        hashMap.put("SUB", "NULL");

        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("TITLE", "Map Quiz");
        hashMap.put("SUB", "Test your remembering skills with the map quiz");

        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("TITLE", "Image Color Quiz");
        hashMap.put("SUB", "Test your color recognition skills with the image quiz");

        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("TITLE", "Math Quiz");
        hashMap.put("SUB", "Test your mathematical and analytical skills with the math quiz");

        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("TITLE", "Word Quiz");
        hashMap.put("SUB", "Test your memory and general knowledge with this quiz");

        arrayList.add(hashMap);


        HomeListAdapter adapter = new HomeListAdapter(arrayList, getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1){
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    startActivity(intent);
                }

                else if(i == 2){
                    Intent intent = new Intent(getActivity(), ColorActivity.class);
                    startActivity(intent);
                }

                else if(i == 3){
                    Intent intent = new Intent(getActivity(), BrainTimerActivity.class);
                    startActivity(intent);
                }

                else if(i == 4){
                    Intent intent = new Intent(getActivity(), QuizActivity.class);
                    startActivity(intent);

                }
            }
        });

        return view;
    }
}