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
import com.fcodex.talaa.City.CityPlaceRestaurantFullViewActivity;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class LastVisitLocationLocationRecyclerViewAdapter extends RecyclerView.Adapter<lastVisitLocation> {

    private final Context context;
    private final List<Modal> lastVisitLocationModal;

    public LastVisitLocationLocationRecyclerViewAdapter(Context context, List<Modal> lastVisitLocationModal) {
        this.context = context;
        this.lastVisitLocationModal = lastVisitLocationModal;
    }

    @NonNull
    @Override
    public lastVisitLocation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_city_places_view,  parent, false);
        return new lastVisitLocation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lastVisitLocation holder, int position) {
        String imageUrl = API.BASE_URL_FOR_IMAGES + lastVisitLocationModal.get(position).getCityPlaceImage();
        Log.d("check_image_", imageUrl);
        Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
        holder.cityPlaceNameTextView.setText(lastVisitLocationModal.get(position).getCityPlaceName());
        holder.cityPlaceLocationTitle.setText(lastVisitLocationModal.get(position).getCityPlaceLocationTitle());
        holder.cityPlaceOpenTimeAmTextView.setText(lastVisitLocationModal.get(position).getOpeningTime());
        holder.cityPlaceOpenTimePmTextView.setText(lastVisitLocationModal.get(position).getCloseingTime());

        holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
            intent.putExtra("myModel",lastVisitLocationModal.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lastVisitLocationModal.size();
    }
}

class lastVisitLocation extends RecyclerView.ViewHolder {

    ImageView cityPlaceImageView;
    TextView cityPlaceNameTextView;
    TextView cityPlaceLocationTitle;
    TextView cityPlaceOpenTimeAmTextView;
    TextView cityPlaceOpenTimePmTextView;
    TextView cityPlaceShowMoreTextView;

    public lastVisitLocation(@NonNull View itemView) {
        super(itemView);

        cityPlaceImageView = itemView.findViewById(R.id.cityPlaceImageView);
        cityPlaceNameTextView = itemView.findViewById(R.id.cityPlaceNameTextView);
        cityPlaceLocationTitle = itemView.findViewById(R.id.cityPlaceLocationTitle);
        cityPlaceOpenTimeAmTextView = itemView.findViewById(R.id.cityPlaceOpenTimeAmTextView);
        cityPlaceOpenTimePmTextView = itemView.findViewById(R.id.cityPlaceOpenTimePmTextView);
        cityPlaceShowMoreTextView = itemView.findViewById(R.id.cityPlaceShowMoreTextView);
    }
}
