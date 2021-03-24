package com.fcodex.talaa.CategoryItems;

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
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CategoryItemsRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantCategoryItemsActivity extends AppCompatActivity {

    private TextView totalRestaurantCatItemsTextView;
    private TextView dataNotFoundRestaurantCatItemsTextView;
    private ShimmerRecyclerView restaurantCatItemsShimmerRecyclerView;
    private ImageView noDataFoundRestaurantCatItemsImageView;
    private final List<Modal> restaurantCatItemModal = new ArrayList<>();

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    private int restaurantCategoryId;

    private int cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_category_items);

        id();
        getData();
        jsonResponse();

        SharedPreferences sharedPreferences = this.getSharedPreferences("cityCatId", MODE_PRIVATE);
        cityId = sharedPreferences.getInt("cat_id", 0);

        customActionBarText.setText(R.string.restaurant);
        customActionBarBackImage.setVisibility(View.INVISIBLE);

    }

    private void getData() {
        try {
            restaurantCategoryId = this.getIntent().getExtras().getInt("category", 0);
            Log.d("restaurantCategory", String.valueOf(restaurantCategoryId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.RESTAURANTS_CATEGORY_ITEM_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("response_res_cat", response);
                String status = jsonObject.getString("status");
                Log.d("status_res_CAt", status);
                if (status.equals("success")) {
                    String total = jsonObject.getString("total");
                    totalRestaurantCatItemsTextView.setText(total);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        int resturantId = Integer.parseInt(jsonObjectFetchData.getString("resturantId"));
                        String resturantName = jsonObjectFetchData.getString("resturantName");
                        String resturantImage = jsonObjectFetchData.getString("resturantImage");
                        String resturantOpening = jsonObjectFetchData.getString("resturantOpening");
                        String resturantClosing = jsonObjectFetchData.getString("resturantClosing");
                        String resturantLong = jsonObjectFetchData.getString("resturantLong");
                        String resturantLat = jsonObjectFetchData.getString("resturantLat");
                        String instagram = jsonObjectFetchData.getString("instagram");
                        String resturantContact = jsonObjectFetchData.getString("resturantContact");
                        String resturantDescription = jsonObjectFetchData.getString("resturantDescription");
                        String price = jsonObjectFetchData.getString("price");
                        String locationTitle = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceWebsiteString = jsonObjectFetchData.getString("website");

                        Modal restaurantCatItemsModal = new Modal();
                        restaurantCatItemsModal.setCityPlaceId(resturantId);
                        restaurantCatItemsModal.setCityPlaceName(resturantName);
                        restaurantCatItemsModal.setCityPlaceImage(resturantImage);
                        restaurantCatItemsModal.setOpeningTime(resturantOpening);
                        restaurantCatItemsModal.setCloseingTime(resturantClosing);
                        restaurantCatItemsModal.setLongitude(resturantLong);
                        restaurantCatItemsModal.setLatitude(resturantLat);
                        restaurantCatItemsModal.setInstagramURL(instagram);
                        restaurantCatItemsModal.setCityPlaceNumber(resturantContact);
                        restaurantCatItemsModal.setCityPlaceDescription(resturantDescription);
                        restaurantCatItemsModal.setCityPlacePrize(price);
                        restaurantCatItemsModal.setCityPlaceLocationTitle(locationTitle);
                        restaurantCatItemsModal.setWebsiteURL(cityPlaceWebsiteString);

                        restaurantCatItemModal.add(restaurantCatItemsModal);
                    }
                    setUpRecyclerView(restaurantCatItemModal);

                } else if (status.equals("fail")) {
                    restaurantCatItemsShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundRestaurantCatItemsImageView.setVisibility(View.VISIBLE);
                    noDataFoundRestaurantCatItemsImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundRestaurantCatItemsTextView.setVisibility(View.VISIBLE);
                    dataNotFoundRestaurantCatItemsTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error_home", String.valueOf(error));
            restaurantCatItemsShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundRestaurantCatItemsImageView.setVisibility(View.VISIBLE);
            dataNotFoundRestaurantCatItemsTextView.setVisibility(View.VISIBLE);
            dataNotFoundRestaurantCatItemsTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("category", String.valueOf(restaurantCategoryId));
                hashmap.put("city", String.valueOf(cityId));
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

    private void setUpRecyclerView(List<Modal> cityPlaceModal) {
        CategoryItemsRecyclerViewAdapter categoryItemsRecyclerViewAdapter = new CategoryItemsRecyclerViewAdapter(this,
                                                                                                        cityPlaceModal, 2);
        restaurantCatItemsShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantCatItemsShimmerRecyclerView.setAdapter(categoryItemsRecyclerViewAdapter);
    }

    private void id() {
        totalRestaurantCatItemsTextView = findViewById(R.id.totalRestaurantCatItemsTextView);
        dataNotFoundRestaurantCatItemsTextView = findViewById(R.id.dataNotFoundRestaurantCatItemsTextView);
        restaurantCatItemsShimmerRecyclerView = findViewById(R.id.restaurantCatItemsShimmerRecyclerView);
        noDataFoundRestaurantCatItemsImageView = findViewById(R.id.noDataFoundRestaurantCatItemsImageView);

        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
    }
}