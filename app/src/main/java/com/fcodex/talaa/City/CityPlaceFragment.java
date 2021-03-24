package com.fcodex.talaa.City;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.PlacesAndRestaurantCategory.PlacesCategoriesActivity;
import com.fcodex.talaa.R;
import com.fcodex.talaa.RecyclerViewAdapter.CityPlaceRecyclerViewAdapter;
import com.fcodex.talaa.Singleton.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.android.billingclient.api.BillingClient.SkuType.INAPP;

public class CityPlaceFragment extends Fragment {

    private SharedPreferences.Editor editor;
    public int city_id;
    private TextView totalCityPlaceTextView;
    private TextView dataNotFoundCityPlaceTextView;
    private ShimmerRecyclerView cityPlaceShimmerRecyclerView;
    private ImageView noDataFoundCityPlaceImageView;
    private ImageView placeCategoryFilterImageView;
    private TextView placeCategoryFilterTextView;
    private LinearLayout linearLayoutPlaceCat;
    private final List<Modal> cityPlaceModal = new ArrayList<>();
    private View view;
    private String purchasing, userid;
    private String msg;
    private int lenth;
    public static final String PREF_FILE = "MyPref";
    public static final String PURCHASE_KEY = "purchas";
    public static final String PRODUCT_ID = "purchase_tala_app1";
    private BillingClient billingClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_city_place, container, false);

        id();
        getData();
        jsonResponse();
        popupAfterTenPlaces();

        return view;
    }


    private void id() {
        totalCityPlaceTextView = view.findViewById(R.id.totalCityPlaceTextView);
        linearLayoutPlaceCat = view.findViewById(R.id.linearLayoutPlaceCat);
        placeCategoryFilterTextView = view.findViewById(R.id.placeCategoryFilterTextView);
        cityPlaceShimmerRecyclerView = view.findViewById(R.id.cityPlaceShimmerRecyclerView);
        noDataFoundCityPlaceImageView = view.findViewById(R.id.noDataFoundCityPlaceImageView);
        placeCategoryFilterImageView = view.findViewById(R.id.placeCategoryFilterImageView);
        dataNotFoundCityPlaceTextView = view.findViewById(R.id.dataNotFoundCityPlaceTextView);

        SharedPreferences sharedpreferences = view.getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.apply();
        purchasing = sharedpreferences.getString("purchasing", "0");
        userid = sharedpreferences.getString("user_id", "0");

        billingClient = BillingClient.newBuilder(Objects.requireNonNull(getContext()))
                .enablePendingPurchases().setListener((billingResult, list) -> {
                    //if item newly purchased
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                        handlePurchases(list);
                    }
                    //if item already purchased then check and reflect changes
                    else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                        Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(INAPP);
                        List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
                        if (alreadyPurchases != null) {
                            handlePurchases(alreadyPurchases);
                        }
                    }
                    //if purchase cancelled
                    else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                        Toast.makeText(getContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
                    }
                    // Handle any other error msgs
                    else {
                        Toast.makeText(getContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(INAPP);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else {
                        savePurchaseValueToPref(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void getData() {
        try {
            city_id = Objects.requireNonNull(getActivity()).getIntent().getExtras().getInt("city_id", 0);
            Log.d("city_id_city", String.valueOf(city_id));
            placeCategoryFilterImageView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PlacesCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            placeCategoryFilterTextView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PlacesCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            linearLayoutPlaceCat.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PlacesCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            if (city_id == 0) {
                cityPlaceShimmerRecyclerView.setVisibility(View.GONE);
                noDataFoundCityPlaceImageView.setVisibility(View.VISIBLE);
                dataNotFoundCityPlaceTextView.setVisibility(View.VISIBLE);
                dataNotFoundCityPlaceTextView.setText(R.string.empty_data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonResponse() {
        // Fetching Status
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_PLACES_API, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                // Fetching Status
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    String total = jsonObject.getString("total");
                    totalCityPlaceTextView.setText(total);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        // Checking specific response
                        int cityPlaceIdString = Integer.parseInt(jsonObjectFetchData.getString("placeId"));
                        String cityPlaceNameString = jsonObjectFetchData.getString("placeName");
                        String cityPlaceImageString = jsonObjectFetchData.getString("placeImage");
                        String cityPlaceOpenTimeString = jsonObjectFetchData.getString("placeOpening");
                        String cityPlaceCloseTimeString = jsonObjectFetchData.getString("placeClosing");
                        String cityPlacelongString = jsonObjectFetchData.getString("placeLong");
                        String cityPlaceLatString = jsonObjectFetchData.getString("placeLat");
                        String cityPlaceInstagramString = jsonObjectFetchData.getString("instagram");
                        String cityPlaceContactString = jsonObjectFetchData.getString("placeContact");
                        String cityPlaceDescriptionString = jsonObjectFetchData.getString("placeDescription");
                        String cityPlacePrizeString = jsonObjectFetchData.getString("price");
                        String cityPlaceLocationTileString = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceWebsiteString = jsonObjectFetchData.getString("website");

                        Modal cityPlaceAPiModal = new Modal();
                        cityPlaceAPiModal.setCityPlaceId(cityPlaceIdString);
                        cityPlaceAPiModal.setCityPlaceName(cityPlaceNameString);
                        cityPlaceAPiModal.setCityPlaceImage(cityPlaceImageString);
                        cityPlaceAPiModal.setOpeningTime(cityPlaceOpenTimeString);
                        cityPlaceAPiModal.setCloseingTime(cityPlaceCloseTimeString);
                        cityPlaceAPiModal.setLongitude(cityPlacelongString);
                        cityPlaceAPiModal.setLatitude(cityPlaceLatString);
                        cityPlaceAPiModal.setInstagramURL(cityPlaceInstagramString);
                        cityPlaceAPiModal.setCityPlaceNumber(cityPlaceContactString);
                        cityPlaceAPiModal.setCityPlaceDescription(cityPlaceDescriptionString);
                        cityPlaceAPiModal.setCityPlacePrize(cityPlacePrizeString);
                        cityPlaceAPiModal.setCityPlaceLocationTitle(cityPlaceLocationTileString);
                        cityPlaceAPiModal.setWebsiteURL(cityPlaceWebsiteString);

                        cityPlaceModal.add(cityPlaceAPiModal);
                    }
                    setUpRecyclerView(cityPlaceModal);
                } else {
                    cityPlaceShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundCityPlaceImageView.setVisibility(View.VISIBLE);
                    noDataFoundCityPlaceImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundCityPlaceTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            cityPlaceShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundCityPlaceImageView.setVisibility(View.VISIBLE);
            dataNotFoundCityPlaceTextView.setVisibility(View.VISIBLE);
            dataNotFoundCityPlaceTextView.setText(R.string.server_error);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("city_id", String.valueOf(city_id));
                return hashmap;
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getInstance(getContext()).addToRequestQueue(stringRequest, "");
    }

    private void setUpRecyclerView(List<Modal> cityPlaceModal) {
        if (purchasing.equals("0") && cityPlaceModal.size() >= 10 ) {
            lenth = 10;
        } else if (purchasing.equals("1")) {
            lenth = cityPlaceModal.size();

        }
        CityPlaceRecyclerViewAdapter cityPlaceRecyclerViewAdapter = new CityPlaceRecyclerViewAdapter
                (getActivity(), cityPlaceModal, 1, lenth);
        cityPlaceShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cityPlaceShimmerRecyclerView.setAdapter(cityPlaceRecyclerViewAdapter);
    }

    // Pop Up appears after specific item.
    private void popupAfterTenPlaces() {
        cityPlaceShimmerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (purchasing.equals("0")) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                        builder1.setMessage("purchase the premium version to get access to 1000+ places and restaurants across all cities!");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Buy",
                                (dialog, id) -> update());

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }
        });
    }

    private void update() {
        inialize_purchase();
    }

    private void inialize_purchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                if (list != null && list.size() > 0) {

                    BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(list.get(0))
                            .build();
                    billingClient.launchBillingFlow(Objects.requireNonNull(getActivity()), flowParams);

                } else {
                    Toast.makeText(getContext(), "Purchase Item Not Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error : " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.PURCHASE_API, response -> {
            response = response.trim();

            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                assert obj != null;
                msg = obj.getString("msg");
                if (msg.equals("Purchasing status updated")) {
                    editor.putString("purchasing", "1");
                    editor.apply();
                    Toast.makeText(view.getContext(), "Thanks For Purchasing premium Offer", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(view.getContext(), "Some Thing wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> Toast.makeText(view.getContext(), "Server Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashmap = new HashMap<>();
                hashmap.put("userId", userid);
                hashmap.put("purchasing", "1");
                return hashmap;
            }
        };

        // if server is not getting the response then it shows hits the API in every 5 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getInstance(view.getContext()).addToRequestQueue(stringRequest, "");

    }

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    private SharedPreferences getPreferenceObject() {
        return Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            //if item is purchased
            if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(getContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if (!getPurchaseValueFromPref()) {
                        savePurchaseValueToPref(true);
                        FragmentManager fm = getFragmentManager();
                        assert fm != null;
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                            Objects.requireNonNull(getActivity()).finish();
                        } else {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        sendData();
                        //Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();

                        //  getActivity().recreate();
                    }
                }
            }
            //if purchase is pending
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false);
                Toast.makeText(getContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AcknowledgePurchaseResponseListener ackPurchase = billingResult -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            //if purchase is acknowledged
            // Grant entitlement to the user. and restart activity
            savePurchaseValueToPref(true);
            sendData();
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                getActivity().finish();
            } else {
                getActivity().finish();
            }
           // Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();

            // getActivity().recreate();
        }
    };

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgqVHcvvdgmQPJe7cR34IWRHer3OYeRZY43v3xPIMQqfzkkhrTairlVCGP1p5Ydtb6PXAApWOnvQzkeRf7lkm7xh6sziECXnirEF4diTnkhwGNKOFDZCTLBDomJfhh1eNbMqqm2SF8sMGWA2ZNi3mCI0FvgezUByWFackbjBUi7/9wrk23Mo9CCnNIJ2TGGvSDGRC3NwHfqaFaFYsQ9ZlUXQkryG5/VCvTUrdTcaGvZ9BgVLpNlrl0Iqz5AKCzRbf+lKR6COWyOxdUW3ctHEGjvscsKxF1YIEbC927oD8X6QD3+yNcDKLydgKmc//S/WIeLy2xyz5qm5/vT+FTr/fvQIDAQAB";
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }


}