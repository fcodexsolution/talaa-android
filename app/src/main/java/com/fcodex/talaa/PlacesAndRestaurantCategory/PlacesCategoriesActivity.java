package com.fcodex.talaa.PlacesAndRestaurantCategory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.City.CityPlacesAndRestaurantActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CategoriesRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesCategoriesActivity extends AppCompatActivity {

    private int city_id_place_category;
    private TextView placesCategoriesTotalTextView;
    private TextView placesCategoriesDataNotFoundTextView;
    private ShimmerRecyclerView placesCategoriesShimmerRecyclerView;
    private ImageView placesCategoriesNoDataFoundImageView;
    private final List<Modal> placesCategoriesModal = new ArrayList<>();

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_categories);

        id();
        sharedPreferences();
        jsonResponse();
        getData();

        customActionBarText.setText(R.string.menu_places_categories);
        customActionBarBackImage.setVisibility(View.INVISIBLE);

    }

    private void sharedPreferences() {

    }

    private void id() {
        placesCategoriesTotalTextView = findViewById(R.id.placesCategoriesTotalTextView);
        placesCategoriesShimmerRecyclerView = findViewById(R.id.placesCategoriesShimmerRecyclerView);
        placesCategoriesNoDataFoundImageView = findViewById(R.id.placesCategoriesNoDataFoundImageView);
        placesCategoriesDataNotFoundTextView = findViewById(R.id.placesCategoriesDataNotFoundTextView);

        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
    }

    private void getData() {
        try {
            city_id_place_category = this.getIntent().getExtras().getInt("city_id", 0);
            Log.d("city_id_place_cat", String.valueOf(city_id_place_category));
            if (city_id_place_category == 0) {
                placesCategoriesShimmerRecyclerView.setVisibility(View.GONE);
                placesCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
                placesCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
                placesCategoriesDataNotFoundTextView.setText(R.string.error_to_get_list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.PLACES_CATEGORIES_API, response -> {
            try {
                Log.d("response_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObj_", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                Log.d("status___", status);
                String total = jsonObject.getString("total");
                Log.d("total___", total);
                placesCategoriesTotalTextView.setText(total);
                if (status.equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        int placeCategoryId = jsonObjectFetchData.getInt("placeCategoryId");
                        String placeCategoryName = jsonObjectFetchData.getString("placeCategoryName");
                        String placeCategoryIcon = jsonObjectFetchData.getString("icon");

                        Modal placeCategoryFetch = new Modal();
                        placeCategoryFetch.setPlaceCatID(placeCategoryId);
                        placeCategoryFetch.setPlaceCatName(placeCategoryName);
                        placeCategoryFetch.setPlaceCatImage(placeCategoryIcon);

                        placesCategoriesModal.add(placeCategoryFetch);
                    }
                    setUpRecyclerView(placesCategoriesModal, 3);

                } else if (status.equals("fail")) {
                    placesCategoriesShimmerRecyclerView.setVisibility(View.GONE);
                    placesCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
                    placesCategoriesNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    placesCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
                    placesCategoriesDataNotFoundTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            placesCategoriesShimmerRecyclerView.setVisibility(View.GONE);
            placesCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
            placesCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
            placesCategoriesDataNotFoundTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("cityId", String.valueOf(city_id_place_category));
                Log.d("hashMap_cat_id", String.valueOf(city_id_place_category));
                return hashmap;
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getInstance(this).addToRequestQueue(stringRequest, "");

    }

    public void setUpRecyclerView(List<Modal> citiesModal, int j) {
        CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(this, citiesModal, j);
        placesCategoriesShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesCategoriesShimmerRecyclerView.setAdapter(categoriesRecyclerViewAdapter);
    }

}