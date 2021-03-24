package com.fcodex.talaa.NavigationActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fcodex.talaa.Fragments.HomeFragment;
import com.fcodex.talaa.PlacesAndRestaurantCategory.PlacesCategoriesActivity;
import com.fcodex.talaa.PlacesAndRestaurantCategory.RestaurantCategoriesActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.SharedPreferences.LanguageTranslate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences.Editor editor;
    private DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationToolBar;
    private Locale myLocale;
    private static final String SELECTED_LANGUAGE_ID = "Selected_language_ID";
    private static final String SELECTED_LANGUAGE = "Selected_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        id();
        languageTranslateChecker();

        initializeDefaultFragment(savedInstanceState);
        setSupportActionBar(navigationToolBar);
        // Apply navigation drawer icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragmentReplacement, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @SuppressLint("CommitPrefEdits")
    private void id() {
        navigationToolBar = findViewById(R.id.navigationToolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        SharedPreferences sharedpreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    // By default fragment shows
    private void initializeDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            MenuItem menuItem = navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new MaterialAlertDialogBuilder(MainActivity.this).setIcon(R.drawable.ic_navigation_exit)
                    .setTitle("Exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        finishAffinity();
                        System.exit(0);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Navigation Onclick Listener
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentTransaction.replace(R.id.frameLayoutFragmentReplacement, new HomeFragment()).commit();
                break;
            case R.id.nav_last_visit_location:
                Intent LastVisitLocationIntent = new Intent(this, LastVisitLocationActivity.class);
                LastVisitLocationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LastVisitLocationIntent);
                break;
            case R.id.nav_places_categories:
                Intent PlacesCategoriesIntent = new Intent(this, PlacesCategoriesActivity.class);
                PlacesCategoriesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(PlacesCategoriesIntent);
                break;
            case R.id.nav_restaurant_categories:
                Intent RestaurantCategoriesIntent = new Intent(this, RestaurantCategoriesActivity.class);
                RestaurantCategoriesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(RestaurantCategoriesIntent);
                break;
            case R.id.nav_suggested_places:
                Intent SuggestedPlacesIntent = new Intent(this, SuggestedPlacesActivity.class);
                SuggestedPlacesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(SuggestedPlacesIntent);
                break;
            case R.id.nav_translater:
                languageTranslate();
                break;
            case R.id.nav_setting:
                Intent SettingIntent = new Intent(this, SettingActivity.class);
                SettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(SettingIntent);
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Do you want log out");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        (dialog, id) -> {
                            editor.putInt("flagLogin", 0);
                            editor.apply();
                            finish();
                        });

                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
                case R.id.nav_exit:
                    new MaterialAlertDialogBuilder(MainActivity.this).setIcon(R.drawable.ic_navigation_exit)
                            .setTitle("Exit")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                finishAffinity();
                                System.exit(0);
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    private void languageTranslate() {
        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.language_dialog, null);
        ImageView dismiss = mView.findViewById(R.id.dismiss_btn);
        RadioGroup language = mView.findViewById(R.id.language_radio);
        language.check(Integer.parseInt(TextUtils.isEmpty(LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID)) ? "0" :
                LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID))
                == 0 ? R.id.english : Integer.parseInt(LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID)));
        language.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.english:
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE_ID, String.valueOf(R.id.english));
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE, "en");
                    setLocale("en");
                    break;
                case R.id.arabic:
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE_ID, String.valueOf(R.id.arabic));
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE, "ar");
                    setLocale("ar");
                    break;
            }
        });
        alert.setView(mView);
        final androidx.appcompat.app.AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        dismiss.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);

        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
    }

    private void languageTranslateChecker() {
        myLocale = new Locale(TextUtils.isEmpty(LanguageTranslate.getString(this, SELECTED_LANGUAGE)) ? "en"
                : LanguageTranslate.getString(this, SELECTED_LANGUAGE));
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}