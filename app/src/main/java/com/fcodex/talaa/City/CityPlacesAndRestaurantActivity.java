package com.fcodex.talaa.City;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.TabViewAdapter.CityPLacesRestaurantTabViewAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

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

        String cityName = this.getIntent().getExtras().getString("city_name");
        Log.d("city_name__", cityName);

        customActionBarText.setText(cityName);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        cityPlacesAndRestaurantTabLayout = findViewById(R.id.cityPlacesAndRestaurantTabLayout);
        cityPlacesAndRestaurantViewPager = findViewById(R.id.cityPlacesAndRestaurantViewPager);
    }

    private void sharedPreferencesRemove(){
        SharedPreferences sharedPreferences = getSharedPreferences("cityCatId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains("cat_id")){
            editor.remove("cat_id");
            editor.apply();
        }
    }

    private void tabLayout() {
        CityPLacesRestaurantTabViewAdapter cityPLacesRestaurantTabViewAdapter = new CityPLacesRestaurantTabViewAdapter(getSupportFragmentManager(), 0);
        cityPlacesAndRestaurantViewPager.setAdapter(cityPLacesRestaurantTabViewAdapter);
        cityPlacesAndRestaurantViewPager.setCurrentItem(0);
        cityPlacesAndRestaurantTabLayout.setupWithViewPager(cityPlacesAndRestaurantViewPager);
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            sharedPreferencesRemove();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        sharedPreferencesRemove();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}