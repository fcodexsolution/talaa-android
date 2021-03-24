package com.fcodex.talaa.NavigationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SuggestedPlacesActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private MaterialButton suggestionSendButton;
    private TextInputEditText enterPlaceOrRestaurantName;
    private TextInputEditText enterCityName;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String stringPlaceOrRestaurantName;
    private String stringCityName;
    private String stringPlaceOrRestaurantCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_places);

        id();
        onClick();
        sendData();

        customActionBarText.setText(R.string.suggested_places);

    }

    private void id() {
        suggestionSendButton = findViewById(R.id.suggestionSendButton);
        enterPlaceOrRestaurantName = findViewById(R.id.enterPlaceOrRestaurantName);
        enterCityName = findViewById(R.id.enterCityName);
        radioGroup = findViewById(R.id.radioGroup);
        //enterPlaceOrRestaurantCategory = findViewById(R.id.enterPlaceOrRestaurantCategory);

        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
    }

    public void checkRadio(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Log.d("radioButton__", String.valueOf(radioButton));
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        suggestionSendButton.setOnClickListener(v -> validation());
    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.Suggested, response -> {
            enterPlaceOrRestaurantName.setText("");
            enterCityName.setText("");

        }, error -> Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("placeName", String.valueOf(stringPlaceOrRestaurantName));
                hashmap.put("cityName", String.valueOf(stringCityName));
                hashmap.put("categoriesType", String.valueOf(radioButton));
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

    private void validation() {
        stringPlaceOrRestaurantName = Objects.requireNonNull(enterPlaceOrRestaurantName.getText()).toString().trim();
        stringCityName = Objects.requireNonNull(enterCityName.getText()).toString().trim();
        //stringPlaceOrRestaurantCategory = Objects.requireNonNull(enterPlaceOrRestaurantCategory.getText()).toString().trim();

        if (stringPlaceOrRestaurantName.length() == 0 || stringCityName.length() == 0 /*||
                stringPlaceOrRestaurantCategory.length() == 0*/) {
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            sendData();
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Suggestion submitted")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}