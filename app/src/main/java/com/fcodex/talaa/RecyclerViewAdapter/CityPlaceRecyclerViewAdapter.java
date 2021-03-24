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
import com.fcodex.talaa.Singleton.LastVisitLocationSingleton;

import java.util.List;

public class CityPlaceRecyclerViewAdapter extends RecyclerView.Adapter<CityPlace> {

    private final Context context;
    private final int i;
    private final List<Modal> cityPlaceModalList;
    private int lenth;

    public CityPlaceRecyclerViewAdapter(Context context, List<Modal> cityPlaceModalListAdapter, int i,int lenth ) {
        this.context = context;
        this.cityPlaceModalList = cityPlaceModalListAdapter;
        this.i = i;
        this.lenth = lenth;
    }
    public CityPlaceRecyclerViewAdapter(Context context, List<Modal> cityPlaceModalListAdapter, int i ) {
        this.context = context;
        this.cityPlaceModalList = cityPlaceModalListAdapter;
        this.i = i;
        this.lenth = 0;

    }

    @NonNull
    @Override
    public CityPlace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_places_view, parent, false);

        return new CityPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityPlace holder, int position) {
        // City Place Fragment
        if (i == 1) {
            String imageUrl = API.BASE_URL_FOR_IMAGES + cityPlaceModalList.get(position).getCityPlaceImage();
            Log.d("check_image_", imageUrl);
            Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                    .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
            holder.cityPlaceNameTextView.setText(cityPlaceModalList.get(position).getCityPlaceName());
            holder.cityPlaceLocationTitle.setText(cityPlaceModalList.get(position).getCityPlaceLocationTitle());
            holder.cityPlaceOpenTimeAmTextView.setText(cityPlaceModalList.get(position).getOpeningTime());
            holder.cityPlaceOpenTimePmTextView.setText(cityPlaceModalList.get(position).getCloseingTime());

            holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
                intent.putExtra("myModel", cityPlaceModalList.get(position));
                if (!isExist(cityPlaceModalList.get(position)))
                    intent.putExtra("myModel", cityPlaceModalList.get(position));
                LastVisitLocationSingleton.instance().getmRecentClicks().add(0, cityPlaceModalList.get(position));
                context.startActivity(intent);
            });
        } else
            // Restaurant Place Fragment
            if (i == 2) {
                String imageUrl = API.BASE_URL_FOR_IMAGES + cityPlaceModalList.get(position).getCityPlaceImage();
                Log.d("check_image_", imageUrl);
                Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                        .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
                holder.cityPlaceNameTextView.setText(cityPlaceModalList.get(position).getCityPlaceName());
                holder.cityPlaceLocationTitle.setText(cityPlaceModalList.get(position).getCityPlaceLocationTitle());
                holder.cityPlaceOpenTimeAmTextView.setText(cityPlaceModalList.get(position).getOpeningTime());
                holder.cityPlaceOpenTimePmTextView.setText(cityPlaceModalList.get(position).getCloseingTime());
                holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
                    intent.putExtra("myModel", cityPlaceModalList.get(position));
                    if (!isExist(cityPlaceModalList.get(position)))
                        intent.putExtra("myModel", cityPlaceModalList.get(position));
                    LastVisitLocationSingleton.instance().getmRecentClicks().add(0, cityPlaceModalList.get(position));
                    context.startActivity(intent);
                });
            } else
                // Suggested Fragment
                if (i == 4) {
                    String imageUrl = API.BASE_URL_FOR_IMAGES + cityPlaceModalList.get(position).getCityPlaceImage();
                    Log.d("check_image_", imageUrl);
                    Glide.with(holder.cityPlaceImageView.getContext()).load(imageUrl)
                            .error(R.drawable.image_error).centerCrop().placeholder(R.drawable.loading).into(holder.cityPlaceImageView);
                    holder.cityPlaceNameTextView.setText(cityPlaceModalList.get(position).getCityPlaceName());
                    holder.cityPlaceLocationTitle.setText(cityPlaceModalList.get(position).getCityPlaceLocationTitle());
                    holder.cityPlaceOpenTimeAmTextView.setText(cityPlaceModalList.get(position).getOpeningTime());
                    holder.cityPlaceOpenTimePmTextView.setText(cityPlaceModalList.get(position).getCloseingTime());
                    holder.cityPlaceShowMoreTextView.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CityPlaceRestaurantFullViewActivity.class);
                        intent.putExtra("myModel", cityPlaceModalList.get(position));
                        context.startActivity(intent);
                    });
                }
    }

    private boolean isExist(Modal modal) {
        for (Modal modal1 : LastVisitLocationSingleton.instance().getmRecentClicks())
            if (modal1.getCityPlaceId() == modal.getCityPlaceId())
                return true;
        return false;
    }

    @Override
    public int getItemCount() {
        if (lenth==0){
            return cityPlaceModalList.size();
        }
        else {
            return lenth;
        }
    }
}

class CityPlace extends RecyclerView.ViewHolder {

    ImageView cityPlaceImageView;
    TextView cityPlaceNameTextView;
    TextView cityPlaceLocationTitle;
    TextView cityPlaceOpenTimeAmTextView;
    TextView cityPlaceOpenTimePmTextView;
    TextView cityPlaceShowMoreTextView;

    public CityPlace(@NonNull View itemView) {
        super(itemView);

        cityPlaceImageView = itemView.findViewById(R.id.cityPlaceImageView);
        cityPlaceNameTextView = itemView.findViewById(R.id.cityPlaceNameTextView);
        cityPlaceLocationTitle = itemView.findViewById(R.id.cityPlaceLocationTitle);
        cityPlaceOpenTimeAmTextView = itemView.findViewById(R.id.cityPlaceOpenTimeAmTextView);
        cityPlaceOpenTimePmTextView = itemView.findViewById(R.id.cityPlaceOpenTimePmTextView);
        cityPlaceShowMoreTextView = itemView.findViewById(R.id.cityPlaceShowMoreTextView);
    }
}
