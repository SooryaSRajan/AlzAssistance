package com.ssr_projects.authhomepage.Adapter.NavigationAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ssr_projects.authhomepage.Fragment.HomePageFragment;
import com.ssr_projects.authhomepage.Fragment.ProfileFragment;

import java.util.ArrayList;

public class NavigationAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public NavigationAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        fragments = new ArrayList<>();
        fragments.add(new HomePageFragment());
        fragments.add(new ProfileFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
