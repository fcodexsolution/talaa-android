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

public class PlacesCategoryItemsActivity extends AppCompatActivity {

    private TextView categoryItemsTotalTextView;
    private TextView categoryItemsDataNotFoundTextView;
    private ShimmerRecyclerView categoryItemsShimmerRecyclerView;
    private ImageView categoryItemsNoDataFoundImageView;
    private final List<Modal> categoriesItemModal = new ArrayList<>();
    private int categoryPlaceID;
    private StringRequest stringRequest;

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    private int cityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_category_items);

        id();
        getData();
        jsonResponse();

        SharedPreferences sharedPreferences = this.getSharedPreferences("cityCatId", MODE_PRIVATE);
        cityId = sharedPreferences.getInt("cat_id", 0);

        customActionBarText.setText(R.string.category_items);
        customActionBarBackImage.setVisibility(View.INVISIBLE);

    }

    private void id() {
        categoryItemsTotalTextView = findViewById(R.id.categoryItemsTotalTextView);
        categoryItemsShimmerRecyclerView = findViewById(R.id.categoryItemsShimmerRecyclerView);
        categoryItemsNoDataFoundImageView = findViewById(R.id.categoryItemsNoDataFoundImageView);
        categoryItemsDataNotFoundTextView = findViewById(R.id.categoryItemsDataNotFoundTextView);

        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
    }

    private void getData() {
        try {
            categoryPlaceID = this.getIntent().getExtras().getInt("category", 0);
            Log.d("category_place_cat_", String.valueOf(categoryPlaceID));
            if (categoryPlaceID == 0) {
                categoryItemsShimmerRecyclerView.setVisibility(View.GONE);
                categoryItemsNoDataFoundImageView.setVisibility(View.VISIBLE);
                categoryItemsDataNotFoundTextView.setVisibility(View.VISIBLE);
                categoryItemsDataNotFoundTextView.setText(R.string.empty_data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
         stringRequest = new StringRequest(Request.Method.POST, API.PLACES_CATEGORIES_DATA_API, response -> {
            Log.d("stringRequest_", String.valueOf(stringRequest));
            try {
                Log.d("response_cat_place", response);
                JSONObject jsonObject = new JSONObject(response);
                // Fetching Status
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    String total = jsonObject.getString("total");
                    categoryItemsTotalTextView.setText(total);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        int cityPlaceIdString = Integer.parseInt(jsonObjectFetchData.getString("placeId"));
                        String cityPlaceNameString = jsonObjectFetchData.getString("placeName");
                        String cityPlaceImageString = jsonObjectFetchData.getString("placeImage");
                        String cityPlaceOpenTimeString = jsonObjectFetchData.getString("placeOpening");
                        String cityPlaceCloseTimeString = jsonObjectFetchData.getString("placeClosing");
                        String cityPlacelongString = jsonObjectFetchData.getString("placeLong");
                        String cityPlaceLatString = jsonObjectFetchData.getString("placeLat");
                        String cityPlaceInstagramString = jsonObjectFetchData.getString("instagram");
                        String cityPlaceContactString = jsonObjectFetchData.getString("placeContact");
                        String cityPlaceDescriptionString = jsonObjectFetchData.getString("placeDescription");
                        String cityPlacePrizeString = jsonObjectFetchData.getString("price");
                        String cityPlaceLocationTileString = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceWebsiteString = jsonObjectFetchData.getString("website");

                        Modal categoryItemAPiModal = new Modal();
                        categoryItemAPiModal.setCityPlaceId(cityPlaceIdString);
                        categoryItemAPiModal.setCityPlaceName(cityPlaceNameString);
                        categoryItemAPiModal.setCityPlaceImage(cityPlaceImageString);
                        categoryItemAPiModal.setOpeningTime(cityPlaceOpenTimeString);
                        categoryItemAPiModal.setCloseingTime(cityPlaceCloseTimeString);
                        categoryItemAPiModal.setLongitude(cityPlacelongString);
                        categoryItemAPiModal.setLatitude(cityPlaceLatString);
                        categoryItemAPiModal.setInstagramURL(cityPlaceInstagramString);
                        categoryItemAPiModal.setCityPlaceNumber(cityPlaceContactString);
                        categoryItemAPiModal.setCityPlaceDescription(cityPlaceDescriptionString);
                        categoryItemAPiModal.setCityPlacePrize(cityPlacePrizeString);
                        categoryItemAPiModal.setCityPlaceLocationTitle(cityPlaceLocationTileString);
                        categoryItemAPiModal.setWebsiteURL(cityPlaceWebsiteString);

                        categoriesItemModal.add(categoryItemAPiModal );
                    }
                    setUpRecyclerView(categoriesItemModal);
                } else if (status.equals("fail")) {
                    categoryItemsShimmerRecyclerView.setVisibility(View.GONE);
                    categoryItemsNoDataFoundImageView.setVisibility(View.VISIBLE);
                    categoryItemsNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    categoryItemsDataNotFoundTextView.setVisibility(View.VISIBLE);
                    categoryItemsDataNotFoundTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            categoryItemsShimmerRecyclerView.setVisibility(View.GONE);
            categoryItemsNoDataFoundImageView.setVisibility(View.VISIBLE);
            categoryItemsDataNotFoundTextView.setVisibility(View.VISIBLE);
            categoryItemsDataNotFoundTextView.setText(R.string.server_error);
        }) {
            @Override
                protected Map<String, String> getParams() {
                    Map<String, String> hashmap = new HashMap<>();
                    hashmap.put("category", String.valueOf(categoryPlaceID));
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

    public void setUpRecyclerView(List<Modal> categoriesItemModal) {
        CategoryItemsRecyclerViewAdapter categoryItemsRecyclerViewAdapter = new CategoryItemsRecyclerViewAdapter
                                                                                (this, categoriesItemModal, 1);
        categoryItemsShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryItemsShimmerRecyclerView.setAdapter(categoryItemsRecyclerViewAdapter);
    }

}