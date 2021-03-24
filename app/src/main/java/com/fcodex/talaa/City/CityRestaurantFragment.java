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
import androidx.annotation.Nullable;
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
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.fcodex.talaa.API.API;
import com.fcodex.talaa.Modal.Modal;
import com.fcodex.talaa.PlacesAndRestaurantCategory.RestaurantCategoriesActivity;
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

public class CityRestaurantFragment extends Fragment {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int city_id;
    private TextView totalCityRestaurantTextView;
    private TextView resCategoryFilterTextView;
    private LinearLayout linearLayoutResCat;
    private TextView dataNotFoundCityRestaurantTextView;
    private ShimmerRecyclerView cityRestaurantShimmerRecyclerView;
    private ImageView noDataFoundCityRestaurantImageView;
    private ImageView restaurantCategoryFilterImageView;
    private final List<Modal> cityRestaurantModal = new ArrayList<>();
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
        view = inflater.inflate(R.layout.fragment_city_restaurant, container, false);

        id();
        getData();
        jsonResponse();

        cityRestaurantShimmerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (purchasing.equals("0")) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                        builder1.setMessage("purchase the premium version to get access to 1000+ places and restaurants across all cities!");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
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
        return view;
    }

    private void getData() {
        try {
            city_id = Objects.requireNonNull(getActivity()).getIntent().getExtras().getInt("city_id", 0);
            Log.d("city_id_city", String.valueOf(city_id));
            restaurantCategoryFilterImageView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RestaurantCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            resCategoryFilterTextView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RestaurantCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            linearLayoutResCat.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RestaurantCategoriesActivity.class);
                intent.putExtra("city_id", city_id);
                Log.d("place_cat_city", String.valueOf(city_id));
                startActivity(intent);
            });
            if (city_id == 0) {
                cityRestaurantShimmerRecyclerView.setVisibility(View.GONE);
                noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
                noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
                dataNotFoundCityRestaurantTextView.setText(R.string.error_to_get_list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void id() {
        totalCityRestaurantTextView = view.findViewById(R.id.totalCityRestaurantTextView);
        cityRestaurantShimmerRecyclerView = view.findViewById(R.id.cityRestaurantShimmerRecyclerView);
        noDataFoundCityRestaurantImageView = view.findViewById(R.id.noDataFoundCityRestaurantImageView);
        restaurantCategoryFilterImageView = view.findViewById(R.id.restaurantCategoryFilterImageView);
        resCategoryFilterTextView = view.findViewById(R.id.resCategoryFilterTextView);
        linearLayoutResCat = view.findViewById(R.id.linearLayoutResCat);
        dataNotFoundCityRestaurantTextView = view.findViewById(R.id.dataNotFoundCityRestaurantTextView);
        sharedpreferences = view.getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedpreferences.edit();
        purchasing = sharedpreferences.getString("purchasing", "0");
        userid = sharedpreferences.getString("user_id", "0");


        billingClient = BillingClient.newBuilder(getContext())
                .enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
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
                    }
                }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
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

    private void update() {
        inialize_purchase();
    }

    private void inialize_purchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if (list != null && list.size() > 0) {

                        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(list.get(0))
                                .build();
                        billingClient.launchBillingFlow(getActivity(), flowParams);

                    } else {
                        Toast.makeText(getContext(), "Purchase Item Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error : " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void jsonResponse() {
        // Fetching Status
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.CITIES_RESTAURANTS_API, response -> {
            try {
                Log.d("responseCityPlace_", response);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("jsonObject", String.valueOf(jsonObject));
                // Fetching Status
                String status = jsonObject.getString("status");
                if (status.equals("success")) {
                    String total = jsonObject.getString("total");
                    totalCityRestaurantTextView.setText(total);
                    Log.d("statusFalse_", String.valueOf(status));
                    Log.d("statusTrue_", String.valueOf(status));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d("jsonArray_", String.valueOf(jsonArray));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectFetchData;
                        jsonObjectFetchData = jsonArray.getJSONObject(i);

                        Log.d("specific_response", response);
                        int resturantId = Integer.parseInt(jsonObjectFetchData.getString("resturantId"));
                        String resturantName = jsonObjectFetchData.getString("resturantName");
                        String resturantImage = jsonObjectFetchData.getString("resturantImage");
                        String resturantOpening = jsonObjectFetchData.getString("resturantOpening");
                        String resturantClosing = jsonObjectFetchData.getString("resturantClosing");
                        String resturantLong = jsonObjectFetchData.getString("resturantLong");
                        String resturantLat = jsonObjectFetchData.getString("resturantLat");
                        String instagram = jsonObjectFetchData.getString("instagram");
                        String resturantContact = jsonObjectFetchData.getString("resturantContact");
                        Log.d("cityPlaceContactString", resturantContact);
                        String resturantDescription = jsonObjectFetchData.getString("resturantDescription");
                        String price = jsonObjectFetchData.getString("price");
                        String locationTitle = jsonObjectFetchData.getString("locationTitle");
                        String cityPlaceWebsiteString = jsonObjectFetchData.getString("website");

                        Modal cityRestaurantAPiModal = new Modal();
                        cityRestaurantAPiModal.setCityPlaceId(resturantId);
                        cityRestaurantAPiModal.setCityPlaceName(resturantName);
                        cityRestaurantAPiModal.setCityPlaceImage(resturantImage);
                        Log.d("setImage_", resturantImage);
                        cityRestaurantAPiModal.setOpeningTime(resturantOpening);
                        cityRestaurantAPiModal.setCloseingTime(resturantClosing);
                        cityRestaurantAPiModal.setLongitude(resturantLong);
                        cityRestaurantAPiModal.setLatitude(resturantLat);
                        cityRestaurantAPiModal.setInstagramURL(instagram);
                        cityRestaurantAPiModal.setCityPlaceNumber(resturantContact);
                        cityRestaurantAPiModal.setCityPlaceDescription(resturantDescription);
                        cityRestaurantAPiModal.setCityPlacePrize(price);
                        cityRestaurantAPiModal.setCityPlaceLocationTitle(locationTitle);
                        cityRestaurantAPiModal.setWebsiteURL(cityPlaceWebsiteString);

                        cityRestaurantModal.add(cityRestaurantAPiModal);
                    }
                    setUpRecyclerView(cityRestaurantModal, 2);
                } else {
                    cityRestaurantShimmerRecyclerView.setVisibility(View.GONE);
                    noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
                    noDataFoundCityRestaurantImageView.setImageResource(R.drawable.ic_data_not_found);
                    dataNotFoundCityRestaurantTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("error__", String.valueOf(error));
            cityRestaurantShimmerRecyclerView.setVisibility(View.GONE);
            noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
            noDataFoundCityRestaurantImageView.setVisibility(View.VISIBLE);
            dataNotFoundCityRestaurantTextView.setText(R.string.server_error);
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

    private void setUpRecyclerView(List<Modal> cityPlaceModal, int i) {
        if(purchasing.equals("0") && cityPlaceModal.size() >= 10){
            lenth = 10;
        } else {
            lenth = cityPlaceModal.size();
        }
        CityPlaceRecyclerViewAdapter cityPlaceRecyclerViewAdapter = new CityPlaceRecyclerViewAdapter(getActivity(), cityPlaceModal, i, lenth);
        cityRestaurantShimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cityRestaurantShimmerRecyclerView.setAdapter(cityPlaceRecyclerViewAdapter);
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
                        sendData();
                        FragmentManager fm = getFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                            getActivity().finish();
                        } else {
                            getActivity().finish();
                        }
                       // Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        // getActivity().recreate();
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
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
                getActivity().finish();
            } else {
                getActivity().finish();
            }
           // Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
            //  getActivity().recreate();
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

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    private SharedPreferences getPreferenceObject() {
        return getActivity().getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getActivity().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://talaa.net/API/purchases.php", response -> {
            response = response.trim();

            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

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
}