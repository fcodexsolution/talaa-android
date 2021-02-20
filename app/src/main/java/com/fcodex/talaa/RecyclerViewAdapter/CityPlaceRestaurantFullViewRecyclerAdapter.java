package com.fcodex.talaa.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.R;

import java.util.List;

public class CityPlaceRestaurantFullViewRecyclerAdapter extends RecyclerView.Adapter<cityPlaceRestaurantFullView> {

    private final Context context;
    private final List<Modal> cityPlaceModal;

    public CityPlaceRestaurantFullViewRecyclerAdapter(Context context, List<Modal> cityPlaceModal) {
        this.context = context;
        this.cityPlaceModal = cityPlaceModal;
    }

    @NonNull
    @Override
    public cityPlaceRestaurantFullView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_places_full_view, parent, false);

        return new cityPlaceRestaurantFullView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cityPlaceRestaurantFullView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cityPlaceModal.size();
    }
}

class cityPlaceRestaurantFullView extends RecyclerView.ViewHolder {

    ImageView cityPlaceFullViewImageView;
    TextView cityPlaceFullViewPlaceName;
    TextView cityPlaceFullViewPlacePrize;
    TextView cityPlaceFullViewContactNumber;
    TextView cityPlaceFullViewStartTime;
    TextView cityPlaceFullViewEndTime;
    TextView cityPlaceFullViewDiscription;
    ImageView cityPlaceFullViewFacebook;
    ImageView cityPlaceFullViewInstagram;
    ImageView cityPlaceFullViewWebsite;

    public cityPlaceRestaurantFullView(@NonNull View itemView) {
        super(itemView);

        cityPlaceFullViewImageView = itemView.findViewById(R.id.cityPlaceFullViewImageView);
        cityPlaceFullViewPlaceName = itemView.findViewById(R.id.cityPlaceFullViewPlaceName);
        cityPlaceFullViewPlacePrize = itemView.findViewById(R.id.cityPlaceFullViewPlacePrize);
        cityPlaceFullViewContactNumber = itemView.findViewById(R.id.cityPlaceFullViewContactNumber);
        cityPlaceFullViewStartTime = itemView.findViewById(R.id.cityPlaceFullViewStartTime);
        cityPlaceFullViewEndTime = itemView.findViewById(R.id.cityPlaceFullViewEndTime);
        cityPlaceFullViewDiscription = itemView.findViewById(R.id.cityPlaceFullViewDiscription);
        cityPlaceFullViewFacebook = itemView.findViewById(R.id.cityPlaceFullViewFacebook);
        cityPlaceFullViewInstagram = itemView.findViewById(R.id.cityPlaceFullViewInstagram);
        cityPlaceFullViewWebsite = itemView.findViewById(R.id.cityPlaceFullViewWebsite);

    }
}
