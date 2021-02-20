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
import com.fcodex.talaa.RecyclerViewAdapter.CityPlaceRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityPlaceFragment extends Fragment {

    private int city_id;
    private TextView totalCityPlaceTextView;
    private TextView dataNotFoundCityPlaceTextView;
    private ShimmerRecyclerView cityPlaceShimmerRecyclerView;
    private ImageView noDataFoundCityPlaceImageView;
    private final List<Modal> cityPlaceModal = new ArrayList<>();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_city_place, container, false);

        id();
        getData();
        jsonResponse();

        return view;
    }

    private void id() {
        totalCityPlaceTextView = view.findViewById(R.id.totalCityPlaceTextView);
        cityPlaceShimmerRecyclerView = view.findViewById(R.id.cityPlaceShimmerRecyclerView);
        noDataFoundCityPlaceImageView = view.findViewById(R.id.noDataFoundCityPlaceImageView);
        dataNotFoundCityPlaceTextView = view.findViewById(R.id.dataNotFoundCityPlaceTextView);
    }

    private void getData() {
        try {
            city_id = getActivity().getIntent().getExtras().getInt("city_id", 0);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_PLACES_API, response -> {
            try {
                Log.d("responseCityPlace_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObject", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                String total = jsonObject.getString("total");
                totalCityPlaceTextView.setText(total);
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
                        int cityPlaceIdString = Integer.parseInt(jsonObjectFetchData.getString("placeId"));
                        String cityPlaceNameString = jsonObjectFetchData.getString("placeName");
                        String cityPlaceImageString = jsonObjectFetchData.getString("placeImage");
                        String cityPlaceLocationTileString = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceOpenTimeString = jsonObjectFetchData.getString("placeOpening");
                        String cityPlaceCloseTimeString = jsonObjectFetchData.getString("placeClosing");
                        String cityPlacePrizeString = jsonObjectFetchData.getString("price");
                        Log.d("prize_", cityPlacePrizeString);

                        Modal cityPlaceAPiModal = new Modal();
                        cityPlaceAPiModal.setCityPlaceId(cityPlaceIdString);
                        cityPlaceAPiModal.setCityPlaceName(cityPlaceNameString);
                        cityPlaceAPiModal.setCityPlaceImage(cityPlaceImageString);
                        cityPlaceAPiModal.setCityPlaceLocationTitle(cityPlaceLocationTileString);
                        cityPlaceAPiModal.setOpeningTime(cityPlaceOpenTimeString);
                        cityPlaceAPiModal.setCloseingTime(cityPlaceCloseTimeString);
                        cityPlaceAPiModal.setCityPlacePrize(cityPlacePrizeString);

                        cityPlaceModal.add(cityPlaceAPiModal);
                    }
                    setUpRecyclerView(cityPlaceModal);
                } else {
                    cityPlaceShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundCityPlaceImageView.setVisibility(View.VISIBLE);
                    noDataFoundCityPlaceImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundCityPlaceTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            cityPlaceShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundCityPlaceImageView.setVisibility(View.VISIBLE);
            dataNotFoundCityPlaceTextView.setVisibility(View.VISIBLE);
            dataNotFoundCityPlaceTextView.setText(R.string.server_error);
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

    private void setUpRecyclerView(List<Modal> cityPlaceModal) {
        CityPlaceRecyclerViewAdapter cityPlaceRecyclerViewAdapter = new CityPlaceRecyclerViewAdapter(getActivity(), cityPlaceModal);
        cityPlaceShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cityPlaceShimmerRecyclerView.setAdapter(cityPlaceRecyclerViewAdapter);
    }

}