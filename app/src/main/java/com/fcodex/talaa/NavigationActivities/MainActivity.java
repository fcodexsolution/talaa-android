package com.fcodex.talaa.NavigationActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.fcodex.talaa.Fragments.HomeFragment;
import com.fcodex.talaa.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar navigationToolBar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        id();
        //dexter();

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

    private void id() {
        navigationToolBar = findViewById(R.id.navigationToolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
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
                        finish();
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
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
            case R.id.nav_setting:
                Intent SettingIntent = new Intent(this, SettingActivity.class);
                SettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(SettingIntent);
                break;
            case R.id.nav_exit:
                new MaterialAlertDialogBuilder(MainActivity.this).setIcon(R.drawable.ic_navigation_exit)
                        .setTitle("Exit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialogInterface, i) -> finish())
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}