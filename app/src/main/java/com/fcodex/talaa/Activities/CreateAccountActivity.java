package com.fcodex.talaa.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fcodex.talaa.R;
import com.fcodex.talaa.Singleton.Singleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TextInputEditText enterSignupFirstname;
    private TextInputEditText enterSignupLastname;
    private TextInputEditText enterSignupEmail;
    private TextInputEditText enterSignupPassword;
    private MaterialButton loginButton;
    private MaterialButton loginSignUpButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        id();
        onClick();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClick() {

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        });

        loginSignUpButton.setOnClickListener(view -> {
            String fName = enterSignupFirstname.getText().toString().trim();
            String LName = enterSignupLastname.getText().toString().trim();
            email = enterSignupEmail.getText().toString().trim();
            String password = enterSignupPassword.getText().toString().trim();
            String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || password.length() < 8 || TextUtils.isEmpty(LName)
                    || TextUtils.isEmpty(fName)
            ) {
                Toast.makeText(CreateAccountActivity.this, "Please Fill all Fields", Toast.LENGTH_SHORT).show();
            } else if (!email.matches(emailValidation)) {
                Toast.makeText(CreateAccountActivity.this, "Please Enter Correct Email", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                register(email, password, fName, LName);
            }
        });

    }

    private void id() {
        enterSignupFirstname = findViewById(R.id.enterfirstname);
        enterSignupLastname = findViewById(R.id.enterlastname);
        enterSignupEmail = findViewById(R.id.enteremail);
        enterSignupPassword = findViewById(R.id.enterpasswprd);
        loginButton = findViewById(R.id.loginButton1);
        loginSignUpButton = findViewById(R.id.loginSignUpButton1);
        SharedPreferences sharedpreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.apply();
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void register(String email, String password, String fName, String lName) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://talaa.net/API/signup.php", response -> {
            progressDialog.dismiss();
            response = response.trim();

            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String msg = obj.getString("msg");
                //Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                if (msg.equals("Verify your email")) {
                    Intent intent = new Intent(this, VarificationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Please Try Another Email again", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("first_name", fName);
                hashmap.put("last_name", lName);
                hashmap.put("email", email);
                hashmap.put("password", password);
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