package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.CategoryItems.PlacesCategoryItemsActivity;
import com.fcodex.talaa.City.CityPlacesAndRestaurantActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class CitiesNameRecyclerViewAdapter extends RecyclerView.Adapter<CityName> {

    private final Context context;
    private final int j;
    private final List<Modal> citiesModalListAdapter;

    public CitiesNameRecyclerViewAdapter(Context context, List<Modal> citiesModalListAdapter, int j) {
        this.context = context;
        this.j = j;
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
        // Home Fragment
        if (j == 1) {
            holder.CityNameTextView.setText(citiesModalListAdapter.get(position).getCitiesName());
            holder.cat_image.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, CityPlacesAndRestaurantActivity.class);
                intent.putExtra("city_id", citiesModalListAdapter.get(position).getCitiesId());
                context.startActivity(intent);
            });
        }
        // Restaurant Category Fragment
        else if (j == 2) {
            holder.CityNameTextView.setText(citiesModalListAdapter.get(position).getRestaurantCatName());
            holder.cat_image.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, CityPlacesAndRestaurantActivity.class);
                intent.putExtra("city_id", citiesModalListAdapter.get(position).getRestaurantCatID());
                context.startActivity(intent);
            });
        }
        // Places Category Fragment
        else if (j == 3) {
            String imageUrl = API.ICON_BASE_URL_API + citiesModalListAdapter.get(position).getCityPlaceImage();
            Log.d("check_image_", imageUrl);
            Glide.with(holder.cat_image.getContext()).load(imageUrl)
                    .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cat_image);
            holder.CityNameTextView.setText(citiesModalListAdapter.get(position).getPlaceCatImage());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlacesCategoryItemsActivity.class);
                intent.putExtra("city_id", citiesModalListAdapter.get(position).getPlaceCatID());
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return citiesModalListAdapter.size();
    }
}

class CityName extends RecyclerView.ViewHolder {

    public TextView CityNameTextView;
    public ImageView cat_image;

    public CityName(@NonNull View itemView) {
        super(itemView);

        CityNameTextView = itemView.findViewById(R.id.cityNameTextView);
        cat_image = itemView.findViewById(R.id.cat_image);

    }
}
