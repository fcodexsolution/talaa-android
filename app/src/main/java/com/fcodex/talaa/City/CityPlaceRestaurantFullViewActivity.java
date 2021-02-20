package com.fcodex.talaa.City;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CityPlaceRestaurantFullViewRecyclerAdapter;
import com.fcodex.talaa.RecyclerViewAdapter.CityRestaurantRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CityPlaceRestaurantFullViewActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private int city_place_id;
    private TextView cityPlaceFullViewDataNotFoundTextView;
    private ShimmerRecyclerView cityPlaceFullViewShimmerRecyclerView;
    private ImageView cityPlaceFullViewNoDataFoundImageView;
    private final List<Modal> cityPlaceModal = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_place_restaurant_full_view);

        id();
        getData();
        jsonResponse();

        customActionBarText.setText("View");
        customActionBarBackImage.setVisibility(View.INVISIBLE);
        
    }

    private void id() {
        cityPlaceFullViewShimmerRecyclerView = findViewById(R.id.cityPlaceFullViewShimmerRecyclerView);
        cityPlaceFullViewNoDataFoundImageView = findViewById(R.id.cityPlaceFullViewNoDataFoundImageView);
        cityPlaceFullViewDataNotFoundTextView = findViewById(R.id.cityPlaceFullViewDataNotFoundTextView);
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
    }

    private void getData() {
        try {
            city_place_id = this.getIntent().getExtras().getInt("city_place_id", 0);
            Log.d("city_place_id", String.valueOf(city_place_id));

            if (city_place_id == 0)
                Toast.makeText(this, "Data is unavaliable ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_PLACES_API, response -> {
            try {
                Log.d("responseCityPlace_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObject", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                Log.d("statusFalse_", String.valueOf(status));
                if (status.equals("success")) {
                    Log.d("statusTrue_", String.valueOf(status));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d("jsonArray_", String.valueOf(jsonArray));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        // Checking specific response
                        Log.d("specific_response", response);
                        String cityPlaceFullViewNameString = jsonObjectFetchData.getString("placeName");
                        String cityPlaceFullViewImageString = jsonObjectFetchData.getString("placeImage");
                        String cityPlaceFullViewLocationTileString = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceFullViewOpenTimeString = jsonObjectFetchData.getString("placeOpening");
                        String cityPlaceFullViewCloseTimeString = jsonObjectFetchData.getString("placeClosing");
                        String cityPlaceFullViewPrizeString = jsonObjectFetchData.getString("price");

                        Modal cityPlaceFullViewAPiModal = new Modal();
                        cityPlaceFullViewAPiModal.setCityPlaceName(cityPlaceFullViewNameString);
                        cityPlaceFullViewAPiModal.setCityPlaceImage(cityPlaceFullViewImageString);
                        cityPlaceFullViewAPiModal.setCityPlaceLocationTitle(cityPlaceFullViewLocationTileString);
                        cityPlaceFullViewAPiModal.setOpeningTime(cityPlaceFullViewOpenTimeString);
                        cityPlaceFullViewAPiModal.setCloseingTime(cityPlaceFullViewCloseTimeString);
                        cityPlaceFullViewAPiModal.setCityPlacePrize(cityPlaceFullViewPrizeString);

                        cityPlaceModal.add(cityPlaceFullViewAPiModal);
                    }
                    setUpRecyclerView(cityPlaceModal);
                } else {
                    cityPlaceFullViewShimmerRecyclerView.setVisibility(View.GONE);
                    cityPlaceFullViewNoDataFoundImageView.setVisibility(View.VISIBLE);
                    cityPlaceFullViewNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    cityPlaceFullViewDataNotFoundTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            cityPlaceFullViewShimmerRecyclerView.setVisibility(View.GONE);
            cityPlaceFullViewNoDataFoundImageView.setVisibility(View.VISIBLE);
            cityPlaceFullViewDataNotFoundTextView.setVisibility(View.VISIBLE);
            cityPlaceFullViewDataNotFoundTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("city_place_id", String.valueOf(city_place_id));
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
        CityPlaceRestaurantFullViewRecyclerAdapter cityPlaceRestaurantFullViewRecyclerAdapter = new
                                    CityPlaceRestaurantFullViewRecyclerAdapter(this, cityPlaceModal);
        cityPlaceFullViewShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityPlaceFullViewShimmerRecyclerView.setAdapter(cityPlaceRestaurantFullViewRecyclerAdapter);
    }
}