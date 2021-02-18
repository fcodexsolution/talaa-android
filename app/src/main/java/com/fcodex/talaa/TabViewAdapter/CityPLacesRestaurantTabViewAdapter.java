package com.fcodex.talaa.TabViewAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fcodex.talaa.City.CityPlaceFragment;
import com.fcodex.talaa.City.CityRestaurantFragment;

public class CityPLacesRestaurantTabViewAdapter extends FragmentStatePagerAdapter {

    public CityPLacesRestaurantTabViewAdapter(@NonNull FragmentManager fm, int i) {
        super(fm, i);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new CityPlaceFragment();
        else if (position == 1)
            return new CityRestaurantFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        if (position == 0)
            return "Places";
        else if (position == 1)
            return "Restaurant";
        else
            return "";
    }
}
