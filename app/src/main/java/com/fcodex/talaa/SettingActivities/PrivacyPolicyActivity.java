package com.fcodex.talaa.SettingActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.NavigationActivities.SettingActivity;
import com.fcodex.talaa.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        id();
        onClick();

        customActionBarText.setText(R.string.privacy_policy);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
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