package com.fcodex.talaa.City;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
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

public class CityRestaurantFragment extends Fragment {

    private int city_id;
    private TextView totalCityRestaurantTextView;
    private TextView dataNotFoundCityRestaurantTextView;
    private ShimmerRecyclerView cityRestaurantShimmerRecyclerView;
    private ImageView noDataFoundCityRestaurantImageView;
    private final List<Modal> cityRestaurantModal = new ArrayList<>();
    private View view;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_city_restaurant, container, false);

       id();
       getData();
       jsonResponse();

       return view;
    }

    private void id() {
        totalCityRestaurantTextView = view.findViewById(R.id.totalCityRestaurantTextView);
        cityRestaurantShimmerRecyclerView = view.findViewById(R.id.cityRestaurantShimmerRecyclerView);
        noDataFoundCityRestaurantImageView = view.findViewById(R.id.noDataFoundCityRestaurantImageView);
        dataNotFoundCityRestaurantTextView = view.findViewById(R.id.dataNotFoundCityRestaurantTextView);
    }

    private void getData() {
        try {
            city_id = Objects.requireNonNull(getActivity()).getIntent().getExtras().getInt("city_id", 0);
            Log.d("city_id_city", String.valueOf(city_id));

            if (city_id == 0)
                Toast.makeText(getActivity(), "Data is unavaliable ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_RESTAURANTS_API, response -> {
            try {
                Log.d("responseCityPlace_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObject", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                String total = jsonObject.getString("total");
                totalCityRestaurantTextView.setText(total);
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
                        String cityPlaceNameString = jsonObjectFetchData.getString("resturantName");
                        String cityPlaceImageString = jsonObjectFetchData.getString("resturantImage");
                        String cityPlaceLocationTileString = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceOpenTimeString = jsonObjectFetchData.getString("resturantOpening");
                        String cityPlaceCloseTimeString = jsonObjectFetchData.getString("resturantClosing");
                        String cityPlacePrizeString = jsonObjectFetchData.getString("price");
                        Log.d("prize_", cityPlacePrizeString);

                        Modal cityPlaceAPiModal = new Modal();
                        cityPlaceAPiModal.setCityPlaceName(cityPlaceNameString);
                        cityPlaceAPiModal.setCityPlaceImage(cityPlaceImageString);
                        cityPlaceAPiModal.setCityPlaceLocationTitle(cityPlaceLocationTileString);
                        cityPlaceAPiModal.setOpeningTime(cityPlaceOpenTimeString);
                        cityPlaceAPiModal.setCloseingTime(cityPlaceCloseTimeString);
                        cityPlaceAPiModal.setCityPlacePrize(cityPlacePrizeString);

                        cityRestaurantModal.add(cityPlaceAPiModal);
                    }
                    setUpRecyclerView(cityRestaurantModal);
                } else {
                    cityRestaurantShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
                    noDataFoundCityRestaurantImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundCityRestaurantTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            cityRestaurantShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
            noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
            dataNotFoundCityRestaurantTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("city_id", String.valueOf(city_id));
                return hashmap;
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest, "");

    }

    private void setUpRecyclerView(List<Modal> cityRestaurantModal) {
        CityRestaurantRecyclerViewAdapter cityRestaurantRecyclerViewAdapter = new CityRestaurantRecyclerViewAdapter(getActivity(), cityRestaurantModal);
        cityRestaurantShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cityRestaurantShimmerRecyclerView.setAdapter(cityRestaurantRecyclerViewAdapter);
    }
}