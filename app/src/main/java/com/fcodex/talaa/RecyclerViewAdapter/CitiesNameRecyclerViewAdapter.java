package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fcodex.talaa.City.CityPlacesAndRestaurantActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class CitiesNameRecyclerViewAdapter extends RecyclerView.Adapter<CityName> {

    private final Context context;
    private final List<Modal> citiesModalListAdapter;

    public CitiesNameRecyclerViewAdapter(Context context, List<Modal> citiesModalListAdapter) {
        this.context = context;
        this.citiesModalListAdapter = citiesModalListAdapter;
    }

    @NonNull
    @Override
    public CityName onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_name_view, parent, false);

        return new CityName(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityName holder, int position) {

        holder.CityNameTextView.setText(citiesModalListAdapter.get(position).getCitiesName());

        holder.itemView.setOnClickListener(v -> {
            Log.d("city_id", String.valueOf(citiesModalListAdapter.get(position).getCitiesId()));

            Intent intent = new Intent(context, CityPlacesAndRestaurantActivity.class);
            intent.putExtra("city_id", citiesModalListAdapter.get(position).getCitiesId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return citiesModalListAdapter.size();
    }
}

class CityName extends RecyclerView.ViewHolder {

    public TextView CityNameTextView;

    public CityName(@NonNull View itemView) {
        super(itemView);

        CityNameTextView = itemView.findViewById(R.id.cityNameTextView);

    }
}
