package com.fcodex.talaa.City;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class CityPlaceRestaurantFullViewActivity extends AppCompatActivity {

    private ImageView customActionBarBackImage;
    private TextView customActionBarText;

    private ImageView cityPlaceFullViewImageView;
    private TextView cityPlaceFullViewPlaceName;
    private TextView cityPlaceFullViewPlacePrize;
    private TextView cityPlaceFullViewContactNumber;
    private TextView cityPlaceFullViewStartTime;
    private TextView cityPlaceFullViewEndTime;
    private TextView cityPlaceFullViewDiscription;
    private ImageView cityPlaceFullViewInstagram;
    private ImageView cityPlaceFullViewWebsite;
    private double longitudeString;
    private double latitudeString;
    private Modal locationTitleString;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_place_restaurant_full_view);

        id();
        getData();


        customActionBarText.setText("View");
        customActionBarBackImage.setVisibility(View.INVISIBLE);

    }

    private void id() {
        customActionBarBackImage = findViewById(R.id.customActionBarBackImage);
        customActionBarText = findViewById(R.id.customActionBarText);

        cityPlaceFullViewImageView = findViewById(R.id.cityPlaceFullViewImageView);
        cityPlaceFullViewPlaceName = findViewById(R.id.cityPlaceFullViewPlaceName);
        cityPlaceFullViewPlacePrize = findViewById(R.id.cityPlaceFullViewPlacePrize);
        cityPlaceFullViewContactNumber = findViewById(R.id.cityPlaceFullViewContactNumber);
        cityPlaceFullViewStartTime = findViewById(R.id.cityPlaceFullViewStartTime);
        cityPlaceFullViewEndTime = findViewById(R.id.cityPlaceFullViewEndTime);
        cityPlaceFullViewDiscription = findViewById(R.id.cityPlaceFullViewDiscription);
        cityPlaceFullViewInstagram = findViewById(R.id.cityPlaceFullViewInstagram);
        cityPlaceFullViewWebsite = findViewById(R.id.cityPlaceFullViewWebsite);

    }

    @SuppressLint("MissingPermission")
    private void getData() {
        try {
            Modal placeNameString = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewPlaceName.setText(placeNameString.getCityPlaceName());
            Log.d("placeNameString___", String.valueOf(cityPlaceFullViewPlaceName));

            Modal modalImage = getIntent().getParcelableExtra("myModel");
            Glide.with(this).load(API.BASE_URL_FOR_IMAGES + modalImage.getCityPlaceImage()).centerCrop()
                    .error(R.drawable.image_error)
                    .placeholder(R.drawable.loading).into(cityPlaceFullViewImageView);
            Log.d("placeImage_", String.valueOf(modalImage));

            Modal modalStartTime = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewStartTime.setText(modalStartTime.getOpeningTime());
            Log.d("placeOpening_", String.valueOf(modalStartTime));

            Modal placeClosing = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewEndTime.setText(placeClosing.getCloseingTime());
            Log.d("placeClosing", String.valueOf(placeClosing));

            Modal placeLong = getIntent().getParcelableExtra("myModel");
            longitudeString = Double.parseDouble(placeLong.getLongitude());
            Log.d("placeLong", String.valueOf(longitudeString));

            Modal placeLat = getIntent().getParcelableExtra("myModel");
            latitudeString = Double.parseDouble(placeLat.getLatitude());
            Log.d("latitudeString", String.valueOf(latitudeString));

            cityPlaceFullViewInstagram.setOnClickListener(v -> {
                Modal instagram = getIntent().getParcelableExtra("myModel");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(instagram.getInstagramURL()));
                startActivity(i);
                Log.d("instagram__", String.valueOf(instagram));

            });

            Modal modalContact = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewContactNumber.setText(modalContact.getCityPlaceNumber());
            Log.d("placeContact__", String.valueOf(modalContact));

            Modal placeDescription = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewDiscription.setText(placeDescription.getCityPlaceDescription());
            Log.d("placeDescription", String.valueOf(placeDescription));

            Modal placePrize = getIntent().getParcelableExtra("myModel");
            cityPlaceFullViewPlacePrize.setText(placePrize.getCityPlacePrize());
            Log.d("price", String.valueOf(placePrize));

            cityPlaceFullViewWebsite.setOnClickListener(v -> {
                Modal websiteUrl = getIntent().getParcelableExtra("myModel");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(websiteUrl.getWebsiteURL()));
                startActivity(i);
                Log.d("instagram__", String.valueOf(websiteUrl));
            });

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.cityPlaceFullViewMapFragment);
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync(googleMap -> Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                googleMap.setMyLocationEnabled(true);

                                // For dropping a marker at a point on the Map
                                LatLng latLng = new LatLng(latitudeString,longitudeString);
                                locationTitleString = getIntent().getParcelableExtra("myModel");
                                googleMap.addMarker(new MarkerOptions().position(latLng)
                                        .title(locationTitleString.getCityPlaceLocationTitle()));


                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}