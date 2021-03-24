package com.fcodex.talaa.NavigationActivities;

import android.content.Intent;
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

public class RestaurantCategoriesActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private int city_id_restaurant_category;
    private TextView customActionBarText;
    private TextView placesCategoriesTotalTextView;
    private ShimmerRecyclerView restaurantCategoriesShimmerRecyclerView;
    private ImageView restaurantCategoriesNoDataFoundImageView;
    private TextView restaurantCategoriesDataNotFoundTextView;
    private final List<Modal> restaurantCategoriesModal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_categories);

        id();
        //onClick();
        jsonResponse();
        getData();

        customActionBarText.setText(R.string.menu_restaurant_categories);
        customActionBarBackImage.setVisibility(View.INVISIBLE);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);

        placesCategoriesTotalTextView = findViewById(R.id.placesCategoriesTotalTextView);
        restaurantCategoriesShimmerRecyclerView = findViewById(R.id.restaurantCategoriesShimmerRecyclerView);
        restaurantCategoriesNoDataFoundImageView = findViewById(R.id.restaurantCategoriesNoDataFoundImageView);
        restaurantCategoriesDataNotFoundTextView = findViewById(R.id.restaurantCategoriesDataNotFoundTextView);
    }

    private void getData() {
        try {
            city_id_restaurant_category = this.getIntent().getExtras().getInt("city_id", 0);
            Log.d("city_id_res_cat", String.valueOf(city_id_restaurant_category));
            if (city_id_restaurant_category == 0) {
                restaurantCategoriesShimmerRecyclerView.setVisibility(View.GONE);
                restaurantCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
                restaurantCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
                restaurantCategoriesDataNotFoundTextView.setText(R.string.error_to_get_list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.RESTAURANTS_CATEGORIES_API, response -> {
            try {
                Log.d("res_cat_response", response);
                JSONObject jsonObject = new JSONObject(response);
                // Fetching Status
                String status = jsonObject.getString("status");
                String total = jsonObject.getString("total");
                placesCategoriesTotalTextView.setText(total);
                if (status.equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        // Checking specific response
                        String resCatNameString = jsonObjectFetchData.getString("resturantCategoryName");
                        int resCatIdString = jsonObjectFetchData.getInt("resturantCategoryId");

                        Modal resCatFetchModal = new Modal();
                        resCatFetchModal.setRestaurantCatName(resCatNameString);
                        resCatFetchModal.setRestaurantCatID(resCatIdString);

                        restaurantCategoriesModal.add(resCatFetchModal);
                    }
                    setUpRecyclerView(restaurantCategoriesModal, 2);

                } else if (status.equals("fail")) {
                    restaurantCategoriesShimmerRecyclerView.setVisibility(View.GONE);
                    restaurantCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
                    restaurantCategoriesNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    restaurantCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
                    restaurantCategoriesDataNotFoundTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            restaurantCategoriesShimmerRecyclerView.setVisibility(View.GONE);
            restaurantCategoriesNoDataFoundImageView.setVisibility(View.VISIBLE);
            restaurantCategoriesDataNotFoundTextView.setVisibility(View.VISIBLE);
            restaurantCategoriesDataNotFoundTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("cityId", String.valueOf(city_id_restaurant_category));
                Log.d("hashMap_cat_id", String.valueOf(city_id_restaurant_category));
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
        restaurantCategoriesShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantCategoriesShimmerRecyclerView.setAdapter(categoriesRecyclerViewAdapter);
    }
}