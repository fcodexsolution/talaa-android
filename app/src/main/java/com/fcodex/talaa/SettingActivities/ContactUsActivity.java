package com.fcodex.talaa.SettingActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.NavigationActivities.SettingActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.Singleton.Singleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private MaterialButton contactUsSendButton;
    private TextInputEditText enterName;
    private TextInputEditText enterEmail;
    private TextInputEditText enterMessage;
    private String stringEnterName;
    private String stringEnterEmail;
    private String stringEnterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        id();
        onClick();
        sendData();

        customActionBarText.setText(R.string.contact_us);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        contactUsSendButton = findViewById(R.id.contactUsSendButton);
        enterName = findViewById(R.id.enterName);
        enterEmail = findViewById(R.id.enterEmail);
        enterMessage = findViewById(R.id.enterMessage);
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        contactUsSendButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactUsActivity.this, MainActivity.class);
            Toast.makeText(this, "Detail Send", Toast.LENGTH_SHORT).show();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        contactUsSendButton.setOnClickListener(v -> validation());
    }

    private void sendData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CONTACT_US_API, response -> {
            enterName.setText("");
            enterEmail.setText("");
            enterMessage.setText("");

        }, error -> Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("userEmail", String.valueOf(stringEnterEmail));
                hashmap.put("message", String.valueOf(stringEnterMessage));
                hashmap.put("name", String.valueOf(stringEnterName));
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
        stringEnterName = Objects.requireNonNull(enterName.getText()).toString().trim();
        stringEnterEmail = Objects.requireNonNull(enterEmail.getText()).toString().trim();
        stringEnterMessage = Objects.requireNonNull(enterMessage.getText()).toString().trim();

        if (stringEnterName.length() == 0 || stringEnterEmail.length() == 0 || stringEnterMessage.length() == 0) {
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            sendData();
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Query submitted")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.cancel()).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}