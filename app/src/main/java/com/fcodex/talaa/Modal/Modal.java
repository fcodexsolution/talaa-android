package com.fcodex.talaa.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.fcodex.talaa.API.API;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Modal implements Parcelable {

    public Modal() { }

    int placeRestaurant;

    int citiesId;
    int cityPlaceId;
    int totalCities;
    int restaurantCatID;
    int placeCatID;
    String citiesName;
    String cityPlaceName;
    String placeCatImage;
    String placeCatName;
    String restaurantCatName;
    String cityPlaceImage;
    String cityPlaceNumber;
    String cityPlacePrize;
    String cityPlaceDescription;
    String openingTime;
    String closeingTime;
    String latitude;
    String longitude;
    String facebookURL;
    String instagramURL;
    String websiteURL;
    String cityPlaceLocationTitle;
    String privacyPolicy;
    String faqQuestion;
    String faqAnswer;

    protected Modal(Parcel in) {
        citiesName = in.readString();
        restaurantCatName = in.readString();
        placeCatImage = in.readString();
        placeCatName = in.readString();
        restaurantCatID = in.readInt();
        placeCatID = in.readInt();
        citiesId = in.readInt();
        cityPlaceId = in.readInt();
        totalCities = in.readInt();
        cityPlaceName = in.readString();
        cityPlaceImage = in.readString();
        cityPlaceNumber = in.readString();
        cityPlacePrize = in.readString();
        cityPlaceDescription = in.readString();
        openingTime = in.readString();
        closeingTime = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        facebookURL = in.readString();
        instagramURL = in.readString();
        websiteURL = in.readString();
        cityPlaceLocationTitle = in.readString();
        privacyPolicy = in.readString();
        faqQuestion = in.readString();
        faqAnswer = in.readString();
    }

    public static final Creator<Modal> CREATOR = new Creator<Modal>() {
        @Override
        public Modal createFromParcel(Parcel in) {
            return new Modal(in);
        }

        @Override
        public Modal[] newArray(int size) {
            return new Modal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(citiesName);
        dest.writeString(restaurantCatName);
        dest.writeString(placeCatImage);
        dest.writeString(placeCatName);
        dest.writeInt(restaurantCatID);
        dest.writeInt(placeCatID);
        dest.writeInt(citiesId);
        dest.writeInt(cityPlaceId);
        dest.writeInt(totalCities);
        dest.writeString(cityPlaceName);
        dest.writeString(cityPlaceImage);
        dest.writeString(cityPlaceNumber);
        dest.writeString(cityPlacePrize);
        dest.writeString(cityPlaceDescription);
        dest.writeString(openingTime);
        dest.writeString(closeingTime);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(facebookURL);
        dest.writeString(instagramURL);
        dest.writeString(websiteURL);
        dest.writeString(cityPlaceLocationTitle);
        dest.writeString(privacyPolicy);
        dest.writeString(faqQuestion);
        dest.writeString(faqAnswer);
    }
}
