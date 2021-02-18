package com.fcodex.talaa.TabViewAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fcodex.talaa.TabViewAdapterFragment.RestaurantCategoryDineInFragment;
import com.fcodex.talaa.TabViewAdapterFragment.RestaurantCategoryTakeAwayFragment;

public class RestaurantCategoriesTabViewAdapter extends FragmentStatePagerAdapter {

    public RestaurantCategoriesTabViewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new RestaurantCategoryDineInFragment();
        else if (position == 1)
            return new RestaurantCategoryTakeAwayFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        if (position == 0)
            return "Dine In";
        else if (position == 1)
            return "Take Away";
        else
            return "";
    }
}