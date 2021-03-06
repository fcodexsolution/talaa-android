package com.fcodex.talaa.SettingActivities;

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
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.NavigationActivities.SettingActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.FaqRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaqActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private ShimmerRecyclerView faqShimmerRecyclerView;
    private ImageView faqNoDataFoundImageView;
    private TextView faqDataNotFoundTextView;
    private final List<Modal> faqModal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        id();
        onClick();
        jsonResponse();

        customActionBarText.setText(R.string.faq);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        faqShimmerRecyclerView = findViewById(R.id.faqShimmerRecyclerView);
        faqNoDataFoundImageView = findViewById(R.id.faqNoDataFoundImageView);
        faqDataNotFoundTextView = findViewById(R.id.faqDataNotFoundTextView);
    }

    private void jsonResponse() {
        // Fetching Status
        // Checking specific response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.FAQ_API, response -> {
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
                        String faqQuestionString = jsonObjectFetchData.getString("questions");
                        String faqAnswerString = jsonObjectFetchData.getString("answers");

                        Modal faqModalFetch = new Modal();
                        faqModalFetch.setFaqQuestion(faqQuestionString);
                        faqModalFetch.setFaqAnswer(faqAnswerString);

                        faqModal.add(faqModalFetch);
                    }
                    setUpRecyclerView(faqModal);

                } else if (status.equals("fail")) {
                    faqShimmerRecyclerView.setVisibility(View.GONE);
                    faqNoDataFoundImageView.setVisibility(View.VISIBLE);
                    faqNoDataFoundImageView.setImageResource(R.drawable.ic_data_not_found);
                    faqDataNotFoundTextView.setVisibility(View.VISIBLE);
                    faqDataNotFoundTextView.setText(R.string.empty_data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            faqShimmerRecyclerView.setVisibility(View.GONE);
            faqNoDataFoundImageView.setVisibility(View.VISIBLE);
            faqDataNotFoundTextView.setVisibility(View.VISIBLE);
            faqDataNotFoundTextView.setText(R.string.server_error);
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
        FaqRecyclerViewAdapter faqRecyclerViewAdapter = new FaqRecyclerViewAdapter(this, privacyPolicyModal);
        faqShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqShimmerRecyclerView.setAdapter(faqRecyclerViewAdapter);
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {

        });
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