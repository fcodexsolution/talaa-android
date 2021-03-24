package com.fcodex.talaa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class VarificationActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextInputEditText code;
    private MaterialButton verify;
    String status,msg,email;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);
        id();

        verify.setOnClickListener(view -> {
            Intent intent = getIntent();
            email =intent.getStringExtra("email");
            String code1 = code.getText().toString();
            if (code1.length() == 0) {
                Toast.makeText(VarificationActivity.this, "Please Enter Confirmation Code", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                sendData(email,code1);

            }
        });
    }

    private void id() {
        code = findViewById(R.id.enterCodeEmail);
        verify = findViewById(R.id.verify);
        progressDialog = new ProgressDialog(VarificationActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void sendData(String email1,String code) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://talaa.net/API/verify.php", response -> {
            progressDialog.dismiss();
            response = response.trim();

            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                status = obj.getString("status");
                msg = obj.getString("msg");
                if(msg.equals("success")) {
                    Intent intent = new Intent(VarificationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Register successfully.Now LogIn", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("code", code);
                hashmap.put("email", email1);
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
}