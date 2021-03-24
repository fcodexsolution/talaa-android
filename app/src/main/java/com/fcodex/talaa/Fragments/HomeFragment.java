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
import com.fcodex.talaa.RecyclerViewAdapter.CategoriesRecyclerViewAdapter;
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
    private TextView dataNotFoundTextView;
    private ShimmerRecyclerView citiesShimmerRecyclerView;
    private ImageView noDataFoundImageView;
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                // Fetching Status
                String status = jsonObject.getString("status");
                Log.d("status_city", status);
                String total = jsonObject.getString("total");
                totalCitiesTextView.setText(total);
                if (status.equals("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        String cityNameString = jsonObjectFetchData.getString("city_name");
                        int cityIdString = jsonObjectFetchData.getInt("city_id");

                        Modal cityNameModal = new Modal();
                        cityNameModal.setCitiesName(cityNameString);
                        cityNameModal.setCitiesId(cityIdString);

                        citiesModal.add(cityNameModal);
                    }
                    setUpRecyclerView(citiesModal, 1);

                } else if (status.equals("fail")) {
                    citiesShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundImageView.setVisibility(View.VISIBLE);
                    noDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundTextView.setVisibility(View.VISIBLE);
                    dataNotFoundTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error_home", String.valueOf(error));
            citiesShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundImageView.setVisibility(View.VISIBLE);
            dataNotFoundTextView.setVisibility(View.VISIBLE);
            dataNotFoundTextView.setText(R.string.server_error);
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

    public void setUpRecyclerView(List<Modal> citiesModal, int j) {
        CategoriesRecyclerViewAdapter categoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(getActivity(), citiesModal, j);
        citiesShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        citiesShimmerRecyclerView.setAdapter(categoriesRecyclerViewAdapter);
    }

    private void id() {
        totalCitiesTextView = view.findViewById(R.id.totalCitiesTextView);
        citiesShimmerRecyclerView = view.findViewById(R.id.citiesShimmerRecyclerView);
        noDataFoundImageView = view.findViewById(R.id.noDataFoundImageView);
        dataNotFoundTextView = view.findViewById(R.id.dataNotFoundTextView);
    }

}