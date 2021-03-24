package com.fcodex.talaa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.Singleton.Singleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private TextInputEditText enterLoginEmail;
    private TextInputEditText enterLoginPassword;
    private MaterialButton loginButton;
    private MaterialButton loginSignUpButton;
    private String stringLoginEmail;
    private String stringLoginPassword;
    String status,msg,user_id,first_name,last_name,email,verified,purchasing;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id();
        onClick();
        //validation();
        int flagLogin = sharedpreferences.getInt("flagLogin",0);
        if (flagLogin==1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void id() {
        enterLoginEmail = findViewById(R.id.enterLoginEmail);
        enterLoginPassword = findViewById(R.id.enterLoginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginSignUpButton = findViewById(R.id.verify);
        sharedpreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGIN_API, response -> {
            enterLoginEmail.setText("");
            enterLoginPassword.setText("");
            Log.d("chklogin_", response);
            response = response.trim();
            progressDialog.dismiss();
            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                 status = obj.getString("status");
                 msg = obj.getString("msg");
                JSONArray jsonArray = obj.getJSONArray("data");
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                         user_id = jsonObject.getString("user_id");
                         first_name = jsonObject.getString("first_name");
                         last_name = jsonObject.getString("last_name");
                         email = jsonObject.getString("email");
                         verified = jsonObject.getString("verified");
                         purchasing = jsonObject.getString("purchasing");
                        msg = jsonObject.getString("msg");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (msg.equals("success")) {
                    editor.putInt("flagLogin", 1);
                    editor.putString("status", status);
                    editor.putString("msg", msg);
                    editor.putString("user_id", user_id);
                    editor.putString("purchasing", purchasing);
                    editor.putString("first_name", first_name);
                    editor.putString("last_name", last_name);
                    editor.putString("email", email);
                    editor.putString("verified", verified);

                    editor.apply();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please Enter Correct email and password", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("email", stringLoginEmail);
                hashmap.put("password", stringLoginPassword);
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
        stringLoginEmail = Objects.requireNonNull(enterLoginEmail.getText()).toString().trim();
        stringLoginPassword = Objects.requireNonNull(enterLoginPassword.getText()).toString().trim();

        if (stringLoginEmail.length() == 0 || stringLoginPassword.length() == 0) {
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            sendData();
        }
    }

    private void onClick() {
        loginButton.setOnClickListener(v -> validation());

        loginSignUpButton.setOnClickListener(v -> {
            this.finish();
            Intent intent = new Intent(this, CreateAccountActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}