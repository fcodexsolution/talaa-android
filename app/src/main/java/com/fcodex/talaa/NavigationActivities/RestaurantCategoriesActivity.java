package com.fcodex.talaa.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcodex.talaa.R;
import com.fcodex.talaa.TabViewAdapter.RestaurantCategoriesTabViewAdapter;
import com.google.android.material.tabs.TabLayout;

public class RestaurantCategoriesActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private TabLayout restaurantCategoryDineInTabLayout;
    private ViewPager restaurantCategoryTakeAwayViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_categories);

        id();
        onClick();
        tabLayout();

        customActionBarText.setText(R.string.menu_restaurant_categories);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        restaurantCategoryDineInTabLayout = findViewById(R.id.restaurantCategoryDineInTabLayout);
        restaurantCategoryTakeAwayViewPager = findViewById(R.id.restaurantCategoryTakeAwayViewPager);
    }

    private void tabLayout() {
        RestaurantCategoriesTabViewAdapter restaurantCategoriesTabViewAdapter = new RestaurantCategoriesTabViewAdapter(getSupportFragmentManager());
        restaurantCategoryTakeAwayViewPager.setAdapter(restaurantCategoriesTabViewAdapter);
        restaurantCategoryTakeAwayViewPager.setCurrentItem(0);
        restaurantCategoryDineInTabLayout.setupWithViewPager(restaurantCategoryTakeAwayViewPager);
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}