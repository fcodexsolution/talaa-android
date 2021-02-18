package com.fcodex.talaa.City;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fcodex.talaa.R;

public class CityPlaceFragment extends Fragment {

    private int cityNameId_;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_place, container, false);

        getData();

        text = view.findViewById(R.id.text);

        return view;
    }

    private void getData() {
        try {
            cityNameId_ = getActivity().getIntent().getExtras().getInt("cityNameId_", 0);
            Log.d("cityNameId_", String.valueOf(cityNameId_));
            text.setText(String.valueOf(cityNameId_));

            if (cityNameId_ == 0)
                Toast.makeText(getActivity(), "Data is unavaliable ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}