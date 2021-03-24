package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.fcodex.talaa.CategoryItems.RestaurantCategoryItemsActivity;
import com.fcodex.talaa.City.CityPlacesAndRestaurantActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CityName> {

    private final Context context;
    private final int j;
    private final List<Modal> categoriesModalListAdapter;
    private SharedPreferences sharedPreferences;

    public CategoriesRecyclerViewAdapter(Context context, List<Modal> categoriesModalListAdapter, int j) {
        this.context = context;
        this.j = j;
        this.categoriesModalListAdapter = categoriesModalListAdapter;
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
            holder.cat_image.setVisibility(View.GONE);
            holder.CityNameTextView.setText(categoriesModalListAdapter.get(position).getCitiesName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CityPlacesAndRestaurantActivity.class);
                intent.putExtra("city_id", categoriesModalListAdapter.get(position).getCitiesId());
                intent.putExtra("city_name", categoriesModalListAdapter.get(position).getCitiesName());
                Log.d("city_name_cat_", categoriesModalListAdapter.get(position).getCitiesName());

                sharedPreferences = context.getSharedPreferences("cityCatId", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.apply();
                editor.putInt("cat_id", categoriesModalListAdapter.get(position).getCitiesId());
                editor.commit();

                context.startActivity(intent);
            });
        }
        // Restaurant Category Fragment
        else if (j == 2) {
            holder.CityNameTextView.setText(categoriesModalListAdapter.get(position).getRestaurantCatName());
            holder.cat_image.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RestaurantCategoryItemsActivity.class);
                intent.putExtra("category", categoriesModalListAdapter.get(position).getRestaurantCatID());
                Log.d("restaurant_plceCat_", String.valueOf(categoriesModalListAdapter.get(position).getRestaurantCatID()));
                context.startActivity(intent);
            });
        }
        // Places Category Fragment
        else if (j == 3) {
            String imageUrl = API.ICON_BASE_URL_API + categoriesModalListAdapter.get(position).getPlaceCatImage();
            Log.d("check_image_place_cat", imageUrl);
            Glide.with(holder.cat_image.getContext()).load(imageUrl)
                    .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cat_image);
            holder.CityNameTextView.setText(categoriesModalListAdapter.get(position).getPlaceCatName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlacesCategoryItemsActivity.class);
                intent.putExtra("category", categoriesModalListAdapter.get(position).getPlaceCatID());
                Log.d("category_plceCat_", String.valueOf(categoriesModalListAdapter.get(position).getPlaceCatID()));
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return categoriesModalListAdapter.size();
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
