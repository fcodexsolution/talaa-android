package com.fcodex.talaa.SettingActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fcodex.talaa.NavigationActivities.MainActivity;
import com.fcodex.talaa.NavigationActivities.SettingActivity;
import com.fcodex.talaa.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private MaterialButton contactUsSendButton;
    private TextInputEditText enterName;
    private TextInputEditText enterEmail;
    private TextInputEditText enterMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        id();
        onClick();

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

    private void validation() {
        String stringEnterName = Objects.requireNonNull(enterName.getText()).toString().trim();
        String stringEnterEmail = Objects.requireNonNull(enterEmail.getText()).toString().trim();
        String stringEnterNumber = Objects.requireNonNull(enterMessage.getText()).toString().trim();

        if (stringEnterName.length() == 0 || stringEnterEmail.length() == 0 || stringEnterNumber.length() == 0) {
            Toast.makeText(this, "Fill the Required Fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Detail Send", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}