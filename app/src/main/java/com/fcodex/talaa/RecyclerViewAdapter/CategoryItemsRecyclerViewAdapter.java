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
import com.fcodex.talaa.City.CityPlaceRestaurantFullViewActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class CategoryItemsRecyclerViewAdapter extends RecyclerView.Adapter<categoryItem> {

    private final Context context;
    private final int i;
    private final List<Modal> categoryItemsModalList;

    public CategoryItemsRecyclerViewAdapter(Context context, List<Modal> cityPlaceModalListAdapter, int i) {
        this.context = context;
        this.categoryItemsModalList = cityPlaceModalListAdapter;
        this.i = i;
    }

    @NonNull
    @Override
    public categoryItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_places_view, parent, false);

        return new categoryItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryItem holder, int position) {
        // Places Category Item
        if (i == 1){
            String imageUrl = API.BASE_URL_FOR_IMAGES + categoryItemsModalList.get(position).getCityPlaceImage();
            Log.d("check_image_", imageUrl);
            Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                    .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
            holder.cityPlaceNameTextView.setText(categoryItemsModalList.get(position).getCityPlaceName());
            holder.cityPlaceLocationTitle.setText(categoryItemsModalList.get(position).getCityPlaceLocationTitle());
            holder.cityPlaceOpenTimeAmTextView.setText(categoryItemsModalList.get(position).getOpeningTime());
            holder.cityPlaceOpenTimePmTextView.setText(categoryItemsModalList.get(position).getCloseingTime());
            holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
                intent.putExtra("myModel", categoryItemsModalList.get(position));
                context.startActivity(intent);
            });

        } else
            // Restaurant Category Item
            if (i == 2){
            String imageUrl = API.BASE_URL_FOR_IMAGES + categoryItemsModalList.get(position).getCityPlaceImage();
            Log.d("check_image_", imageUrl);
            Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                    .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
            holder.cityPlaceNameTextView.setText(categoryItemsModalList.get(position).getCityPlaceName());
            holder.cityPlaceLocationTitle.setText(categoryItemsModalList.get(position).getCityPlaceLocationTitle());
            holder.cityPlaceOpenTimeAmTextView.setText(categoryItemsModalList.get(position).getOpeningTime());
            holder.cityPlaceOpenTimePmTextView.setText(categoryItemsModalList.get(position).getCloseingTime());
            holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
                intent.putExtra("myModel", categoryItemsModalList.get(position));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryItemsModalList.size();
    }
}

class categoryItem extends RecyclerView.ViewHolder {

    ImageView cityPlaceImageView;
    TextView cityPlaceNameTextView;
    TextView cityPlaceLocationTitle;
    TextView cityPlaceOpenTimeAmTextView;
    TextView cityPlaceOpenTimePmTextView;
    TextView cityPlaceShowMoreTextView;

    public categoryItem(@NonNull View itemView) {
        super(itemView);

        cityPlaceImageView = itemView.findViewById(R.id.cityPlaceImageView);
        cityPlaceNameTextView = itemView.findViewById(R.id.cityPlaceNameTextView);
        cityPlaceLocationTitle = itemView.findViewById(R.id.cityPlaceLocationTitle);
        cityPlaceOpenTimeAmTextView = itemView.findViewById(R.id.cityPlaceOpenTimeAmTextView);
        cityPlaceOpenTimePmTextView = itemView.findViewById(R.id.cityPlaceOpenTimePmTextView);
        cityPlaceShowMoreTextView = itemView.findViewById(R.id.cityPlaceShowMoreTextView);
    }
}
