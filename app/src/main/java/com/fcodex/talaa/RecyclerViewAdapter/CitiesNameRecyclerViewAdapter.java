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

public class CitiesNameRecyclerViewAdapter extends RecyclerView.Adapter<VipsTips> {

    private final Context context;
    private final List<Modal> citiesModalListAdapter;

    public CitiesNameRecyclerViewAdapter(Context context, List<Modal> citiesModalListAdapter) {
        this.context = context;
        this.citiesModalListAdapter = citiesModalListAdapter;
    }

    @NonNull
    @Override
    public VipsTips onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_name_view, parent, false);

        return new VipsTips(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VipsTips holder, int position) {

        holder.cityNameTextView.setText(citiesModalListAdapter.get(position).getCitiesName());
        holder.itemView.setOnClickListener(v -> {
            Log.d("cityNameId_", String.valueOf(citiesModalListAdapter.get(position).getCitiesId()));

            Intent intent = new Intent(context, CityPlacesAndRestaurantActivity.class);
            intent.putExtra("cityNameId_", citiesModalListAdapter.get(position).getCitiesId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return citiesModalListAdapter.size();
    }
}

class VipsTips extends RecyclerView.ViewHolder {

    public TextView cityNameTextView;

    public VipsTips(@NonNull View itemView) {
        super(itemView);

        cityNameTextView = itemView.findViewById(R.id.cityNameTextView);

    }
}
