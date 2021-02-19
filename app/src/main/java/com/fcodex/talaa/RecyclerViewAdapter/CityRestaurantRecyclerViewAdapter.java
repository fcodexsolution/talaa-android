package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
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
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class CityRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<CityRestaurant> {

    private final Context context;
    private final List<Modal> cityRestaurantModalList;

    public CityRestaurantRecyclerViewAdapter(Context context, List<Modal> cityRestaurantModalListAdapter) {
        this.context = context;
        this.cityRestaurantModalList = cityRestaurantModalListAdapter;
    }

    @NonNull
    @Override
    public CityRestaurant onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_rastuarant_view, parent, false);

        return new CityRestaurant(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityRestaurant holder, int position) {
        holder.cityRestaurantNameTextView.setText(cityRestaurantModalList.get(position).getCityPlaceName());
        Glide.with(context).load(cityRestaurantModalList.get(position).getCityPlaceImage()).centerCrop()
                .placeholder(R.drawable.loading).into(holder.cityRestaurantImageView);
        Log.d("image_", cityRestaurantModalList.get(position).getCityPlaceImage());

        holder.cityRestaurantLocationTitle.setText(cityRestaurantModalList.get(position).getCityPlaceLocationTitle());
        holder.cityRestaurantOpenTimeAmTextView.setText(cityRestaurantModalList.get(position).getOpeningTime());
        holder.cityRestaurantOpenTimePmTextView.setText(cityRestaurantModalList.get(position).getCloseingTime());
        holder.cityRestaurantPrizeTextView.setText(cityRestaurantModalList.get(position).getCityPlacePrize());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "id" + cityRestaurantModalList.get(position), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return cityRestaurantModalList.size();
    }
}
class CityRestaurant extends RecyclerView.ViewHolder {

    ImageView cityRestaurantImageView;
    TextView cityRestaurantNameTextView;
    TextView cityRestaurantLocationTitle;
    TextView cityRestaurantOpenTimeAmTextView;
    TextView cityRestaurantOpenTimePmTextView;
    TextView cityRestaurantPrizeTextView;

    public CityRestaurant(@NonNull View itemView) {
        super(itemView);

        cityRestaurantImageView = itemView.findViewById(R.id.cityRestaurantImageView);
        cityRestaurantNameTextView = itemView.findViewById(R.id.cityRestaurantNameTextView);
        cityRestaurantLocationTitle = itemView.findViewById(R.id.cityRestaurantLocationTitle);
        cityRestaurantOpenTimeAmTextView = itemView.findViewById(R.id.cityRestaurantOpenTimeAmTextView);
        cityRestaurantOpenTimePmTextView = itemView.findViewById(R.id.cityRestaurantOpenTimePmTextView);
        cityRestaurantPrizeTextView = itemView.findViewById(R.id.cityRestaurantPrizeTextView);
    }
}
