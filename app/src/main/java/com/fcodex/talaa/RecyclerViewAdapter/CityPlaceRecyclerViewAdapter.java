package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fcodex.talaa.City.CityPlaceRestaurantFullViewActivity;
import com.fcodex.talaa.City.CityPlacesAndRestaurantActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;
import com.google.android.material.transition.Hold;

import java.util.List;

public class CityPlaceRecyclerViewAdapter extends RecyclerView.Adapter<CityPlace> {

    private final Context context;
    private final List<Modal> cityPlaceModalList;

    public CityPlaceRecyclerViewAdapter(Context context, List<Modal> cityPlaceModalListAdapter) {
        this.context = context;
        this.cityPlaceModalList = cityPlaceModalListAdapter;
    }

    @NonNull
    @Override
    public CityPlace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_places_view, parent, false);

        return new CityPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityPlace holder, int position) {
        Glide.with(context).load(cityPlaceModalList.get(position).getCityPlaceImage()).centerCrop()
                .placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
        Log.d("image_", cityPlaceModalList.get(position).getCityPlaceImage());

        holder.cityPlaceNameTextView.setText(cityPlaceModalList.get(position).getCityPlaceName());
        holder.cityPlaceLocationTitle.setText(cityPlaceModalList.get(position).getCityPlaceLocationTitle());
        holder.cityPlaceOpenTimeAmTextView.setText(cityPlaceModalList.get(position).getOpeningTime());
        holder.cityPlaceOpenTimePmTextView.setText(cityPlaceModalList.get(position).getCloseingTime());
        holder.cityPlacePrizeTextView.setText(cityPlaceModalList.get(position).getCityPlacePrize());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
            intent.putExtra("city_place_id", cityPlaceModalList.get(position).getCityPlaceId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cityPlaceModalList.size();
    }
}

class CityPlace extends RecyclerView.ViewHolder {

    ImageView cityPlaceImageView;
    TextView cityPlaceNameTextView;
    TextView cityPlaceLocationTitle;
    TextView cityPlaceOpenTimeAmTextView;
    TextView cityPlaceOpenTimePmTextView;
    TextView cityPlacePrizeTextView;

    public CityPlace(@NonNull View itemView) {
        super(itemView);

        cityPlaceImageView = itemView.findViewById(R.id.cityPlaceImageView);
        cityPlaceNameTextView = itemView.findViewById(R.id.cityPlaceNameTextView);
        cityPlaceLocationTitle = itemView.findViewById(R.id.cityPlaceLocationTitle);
        cityPlaceOpenTimeAmTextView = itemView.findViewById(R.id.cityPlaceOpenTimeAmTextView);
        cityPlaceOpenTimePmTextView = itemView.findViewById(R.id.cityPlaceOpenTimePmTextView);
        cityPlacePrizeTextView = itemView.findViewById(R.id.cityPlacePrizeTextView);
    }
}
