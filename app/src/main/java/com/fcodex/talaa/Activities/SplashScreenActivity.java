package com.fcodex.talaa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.fcodex.talaa.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fullScreen();
        progressMethod();

    }

    private void fullScreen() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        // to make status bar transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void progressMethod() {
        //Code to start timer and take action after the timer ends
        //This is 3 seconds
        int SPLASH_TIME = 3000;
        new Handler().postDelayed(() -> {
            //Do any action here. Now we are moving to next page
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            //This 'finish()' is for exiting the app when back button pressed from HomeFragment page which is ActivityHome
            finish();
        }, SPLASH_TIME);
    }

}