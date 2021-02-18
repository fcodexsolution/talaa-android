package com.fcodex.talaa.NavigationActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

    private LinearLayout translatorLinearLayout;
    private LinearLayout contactUsLinearLayout;
    private LinearLayout faqLinearLayout;
    private LinearLayout privacyPolicyLinearLayout;
    private ImageView translatorIconImageView;
    private ImageView contactUsIconImageView;
    private ImageView faqIconImageView;
    private ImageView privacyPolicyIconImageView;
    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private Locale myLocale;
    private static final String SELECTED_LANGUAGE_ID = "Selected_language_ID";
    private static final String SELECTED_LANGUAGE = "Selected_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        languageTranslateChecker();
        id();
        onClick();

        customActionBarText.setText(R.string.menu_setting);
    }

    private void id() {
        translatorLinearLayout = findViewById(R.id.translatorLinearLayout);
        translatorIconImageView = findViewById(R.id.translatorIconImageView);
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
        translatorLinearLayout.setOnClickListener(v -> languageTranslate());
        translatorIconImageView.setOnClickListener(v -> languageTranslate());
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

    private void languageTranslate() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.language_dialog, null);
        ImageView dismiss = mView.findViewById(R.id.dismiss_btn);
        RadioGroup language = mView.findViewById(R.id.language_radio);
        language.check(Integer.parseInt(TextUtils.isEmpty(LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID)) ? "0" :
                LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID))
                == 0 ? R.id.english : Integer.parseInt(LanguageTranslate.getString(this, SELECTED_LANGUAGE_ID)));
        language.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.english:
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE_ID, String.valueOf(R.id.english));
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE, "en");
                    setLocale("en");
                    break;
                case R.id.arabic:
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE_ID, String.valueOf(R.id.arabic));
                    LanguageTranslate.putString(this, SELECTED_LANGUAGE, "ar");
                    setLocale("ar");
                    break;
            }
        });
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        dismiss.setOnClickListener(v1 -> alertDialog.dismiss());
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);

        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
    }

    private void languageTranslateChecker() {
        myLocale = new Locale(TextUtils.isEmpty(LanguageTranslate.getString(this, SELECTED_LANGUAGE)) ? "en"
                : LanguageTranslate.getString(this, SELECTED_LANGUAGE));
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}