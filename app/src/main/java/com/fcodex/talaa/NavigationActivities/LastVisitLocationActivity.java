package com.fcodex.talaa.NavigationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.LastVisitLocationLocationRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.LastVisitLocationSingleton;

import java.util.List;

public class LastVisitLocationActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;
    private ShimmerRecyclerView lastVisitLocationShimmerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_visit_location);

        id();
        onClick();

        customActionBarText.setText(R.string.last_visit_location);

    }

    public void setUpRecyclerView(List<Modal> lastVisitLocationModal) {
        LastVisitLocationLocationRecyclerViewAdapter lastVisitLocationLocationRecyclerViewAdapter = new
                            LastVisitLocationLocationRecyclerViewAdapter(this, lastVisitLocationModal);
        lastVisitLocationShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lastVisitLocationShimmerRecyclerView.setAdapter(lastVisitLocationLocationRecyclerViewAdapter);
    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);
        lastVisitLocationShimmerRecyclerView = findViewById(R.id.lastVisitLocationShimmerRecyclerView);
        setUpRecyclerView(LastVisitLocationSingleton.instance().getmRecentClicks());
    }

    private void onClick() {
        customActionBarBackImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}