package com.fcodex.talaa.SettingActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.NavigationActivities.SettingActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CitiesNameRecyclerViewAdapter;
import com.fcodex.talaa.RecyclerViewAdapter.PrivacyPolicyRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private ShimmerRecyclerView privacyPolicyShimmerRecyclerView;
    private ImageView privacyPolicyNoDataFoundImageView;
    private TextView privacyPolicyDataNotFoundTextView;
    private final List<Modal> privacyPolicyModal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        id();
        onClick();
        jsonResponse();

        customActionBarText.setText(R.string.privacy_policy);

    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.PRIVACY_POLICY_API, response -> {
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
                        String privacyPolicyString = jsonObjectFetchData.getString("description");
                        Log.d("privacyPolicyString_", privacyPolicyString);

                        Modal privacyPolicyModalFetch = new Modal();
                        privacyPolicyModalFetch.setPrivacyPolicy(privacyPolicyString);

                        privacyPolicyModal.add(privacyPolicyModalFetch);
                    }
                    setUpRecyclerView(privacyPolicyModal);

                } else if (status.equals("fail")) {
                    privacyPolicyShimmerRecyclerView.setVisibility(View.GONE);
                    privacyPolicyNoDataFoundImageView.setVisibility(View.VISIBLE);
                    privacyPolicyNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    privacyPolicyDataNotFoundTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            privacyPolicyShimmerRecyclerView.setVisibility(View.GONE);
            privacyPolicyNoDataFoundImageView.setVisibility(View.VISIBLE);
            privacyPolicyDataNotFoundTextView.setVisibility(View.VISIBLE);
            privacyPolicyDataNotFoundTextView.setText(R.string.server_error);
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
        Singleton.getInstance(this).addToRequestQueue(stringRequest, "");

    }

    public void setUpRecyclerView(List<Modal> privacyPolicyModal) {
        PrivacyPolicyRecyclerViewAdapter citiesRecyclerViewAdapter = new PrivacyPolicyRecyclerViewAdapter(this, privacyPolicyModal);
        privacyPolicyShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        privacyPolicyShimmerRecyclerView.setAdapter(citiesRecyclerViewAdapter);
    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        privacyPolicyShimmerRecyclerView = findViewById(R.id.privacyPolicyShimmerRecyclerView);
        privacyPolicyNoDataFoundImageView = findViewById(R.id.privacyPolicyNoDataFoundImageView);
        privacyPolicyDataNotFoundTextView = findViewById(R.id.privacyPolicyDataNotFoundTextView);
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}