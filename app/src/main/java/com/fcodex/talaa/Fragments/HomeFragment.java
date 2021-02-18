package com.fcodex.talaa.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CitiesNameRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private TextView totalCitiesTextView;
    private ShimmerRecyclerView citiesShimmerRecyclerView;
    private ImageView noDataFound;
    private final List<Modal> citiesModal = new ArrayList<>();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        id();
        jsonResponse();

        return view;
    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.CITIES_API, response -> {
            try {
                Log.d("response_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObject", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                Log.d("statusFalse_", String.valueOf(status));
                if (status.equals("success")) {
                    Log.d("statusTrue_", String.valueOf(status));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        // Checking specific response
                        Log.d("specific_response", response);
                        String cityNameString = jsonObjectFetchData.getString("city_name");
                        int cityIdString = jsonObjectFetchData.getInt("city_id");
                        Log.d("city_name_", cityNameString);

                        Modal cityNameModal = new Modal();
                        cityNameModal.setCitiesName(cityNameString);
                        cityNameModal.setCitiesId(cityIdString);

                        citiesModal.add(cityNameModal);
                    }
                    setUpRecyclerView(citiesModal);
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                    citiesShimmerRecyclerView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            noDataFound.setVisibility(View.VISIBLE);
            citiesShimmerRecyclerView.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest, "");

    }

    public void setUpRecyclerView(List<Modal> citiesModal) {
        CitiesNameRecyclerViewAdapter citiesRecyclerViewAdapter = new CitiesNameRecyclerViewAdapter(getActivity(), citiesModal);
        citiesShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        citiesShimmerRecyclerView.setAdapter(citiesRecyclerViewAdapter);
    }

    private void id() {
        totalCitiesTextView = view.findViewById(R.id.totalCitiesTextView);
        citiesShimmerRecyclerView = view.findViewById(R.id.citiesShimmerRecyclerView);
        noDataFound = view.findViewById(R.id.noDataFound);
    }

}