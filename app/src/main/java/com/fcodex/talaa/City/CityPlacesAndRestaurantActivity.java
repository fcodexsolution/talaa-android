package com.fcodex.talaa.City;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.TabViewAdapter.CityPLacesRestaurantTabViewAdapter;
import com.google.android.material.tabs.TabLayout;

public class CityPlacesAndRestaurantActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private TabLayout cityPlacesAndRestaurantTabLayout;
    private ViewPager cityPlacesAndRestaurantViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_places_and_restuarent);

        id();
        onClick();
        tabLayout();

        customActionBarText.setText(R.string.city_places_and_restaurant);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        cityPlacesAndRestaurantTabLayout = findViewById(R.id.cityPlacesAndRestaurantTabLayout);
        cityPlacesAndRestaurantViewPager = findViewById(R.id.cityPlacesAndRestaurantViewPager);
    }

    private void tabLayout() {
        CityPLacesRestaurantTabViewAdapter cityPLacesRestaurantTabViewAdapter = new CityPLacesRestaurantTabViewAdapter(getSupportFragmentManager(), 0);
        cityPlacesAndRestaurantViewPager.setAdapter(cityPLacesRestaurantTabViewAdapter);
        cityPlacesAndRestaurantViewPager.setCurrentItem(0);
        cityPlacesAndRestaurantTabLayout.setupWithViewPager(cityPlacesAndRestaurantViewPager);
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