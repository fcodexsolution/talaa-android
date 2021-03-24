package com.fcodex.talaa.NavigationActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fcodex.talaa.R;
import com.fcodex.talaa.SettingActivities.ContactUsActivity;
import com.fcodex.talaa.SettingActivities.FaqActivity;
import com.fcodex.talaa.SettingActivities.PrivacyPolicyActivity;
import com.fcodex.talaa.SharedPreferences.LanguageTranslate;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private CardView translaterCard;
    private LinearLayout contactUsLinearLayout;
    private LinearLayout faqLinearLayout;
    private LinearLayout privacyPolicyLinearLayout;
    private ImageView contactUsIconImageView;
    private ImageView faqIconImageView;
    private ImageView privacyPolicyIconImageView;
    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        id();
        onClick();

        customActionBarText.setText(R.string.menu_setting);
        translaterCard.setVisibility(View.INVISIBLE);

    }

    private void id() {
        translaterCard = findViewById(R.id.translaterCard);
        contactUsLinearLayout = findViewById(R.id.contactUsLinearLayout);
        contactUsIconImageView = findViewById(R.id.contactUsIconImageView);
        faqLinearLayout = findViewById(R.id.faqLinearLayout);
        faqIconImageView = findViewById(R.id.faqIconImageView);
        customActionBarText = findViewById(R.id.customActionBarText);
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        privacyPolicyLinearLayout = findViewById(R.id.privacyPolicyLinearLayout);
        privacyPolicyIconImageView = findViewById(R.id.privacyPolicyIconImageView);
    }

    private void onClick() {
        contactUsLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });
        contactUsIconImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });
        faqLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });
        faqIconImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });
        privacyPolicyLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });
        privacyPolicyIconImageView.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}