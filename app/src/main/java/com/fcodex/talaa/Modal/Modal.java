package com.fcodex.talaa.Modal;

import com.fcodex.talaa.API.API;

public class Modal {

    String citiesName;
    int citiesId;
    String cityPlaceImage;
    String cityPlaceLocationTitle;
    String cityPlaceName;
    String cityPlacePrize;
    String cityPlaceDescription;
    String openingTime;
    String closeingTime;
    int latitude;
    int longitude;
    String facebookURL;
    String instagramURL;



    // City Name Getter Setter
    public String getCitiesName() {
        return citiesName;
    }

    public void setCitiesName(String citiesName) {
        this.citiesName = citiesName;
    }

    // City ID Getter Setter
    public int getCitiesId() {
        return citiesId;
    }

    public void setCitiesId(int citiesId) {
        this.citiesId = citiesId;
    }

    public String getCityPlaceImage() {
        return API.BASE_URL_FOR_IMAGES + cityPlaceImage;
    }

    public void setCityPlaceImage(String cityPlaceImage) {
        this.cityPlaceImage = cityPlaceImage;
    }

    public String getCityPlaceLocationTitle() {
        return cityPlaceLocationTitle;
    }

    public void setCityPlaceLocationTitle(String cityPlaceLocationTitle) {
        this.cityPlaceLocationTitle = cityPlaceLocationTitle;
    }

    public String getCityPlaceName() {
        return cityPlaceName;
    }

    public void setCityPlaceName(String cityPlaceName) {
        this.cityPlaceName = cityPlaceName;
    }

    public String getCityPlacePrize() {
        return cityPlacePrize;
    }

    public void setCityPlacePrize(String cityPlacePrize) {
        this.cityPlacePrize = cityPlacePrize;
    }

    public String getCityPlaceDescription() {
        return cityPlaceDescription;
    }

    public void setCityPlaceDescription(String cityPlaceDescription) {
        this.cityPlaceDescription = cityPlaceDescription;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getCloseingTime() {
        return closeingTime;
    }

    public void setCloseingTime(String closeingTime) {
        this.closeingTime = closeingTime;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getFacebookURL() {
        return facebookURL;
    }

    public void setFacebookURL(String facebookURL) {
        this.facebookURL = facebookURL;
    }

    public String getInstagramURL() {
        return instagramURL;
    }

    public void setInstagramURL(String instagramURL) {
        this.instagramURL = instagramURL;
    }


}
