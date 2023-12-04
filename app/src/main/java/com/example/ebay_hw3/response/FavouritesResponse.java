package com.example.ebay_hw3.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FavouritesResponse {
    public String _id;
    public String itemId;
    public String image;
    public String title;
    public String price;
    public String shippingOptions;
    public String favoriteDetails;
    public String zipCode;

    public String condition;
    public ShippingInfo shippingInfo;
    public String shippingCost;

    public Boolean inCart;

    public FavouritesResponse(String itemId, String image, String title, String price, String zipCode, String condition, String shippingCost, Boolean inCart) {
        this.itemId = itemId;
        this.image = image;
        this.title = title;
        this.price = price;
        this.zipCode = zipCode;
        this.condition = condition;
        this.shippingCost = shippingCost;
        this.inCart = inCart;
    }


    public class ShippingCost{
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }

    public class ShippingInfo{
        public ArrayList<ShippingServiceCost> shippingServiceCost;
        public ArrayList<String> shippingType;
        public ArrayList<String> shipToLocations;
        public ArrayList<String> expeditedShipping;
        public ArrayList<String> oneDayShippingAvailable;
        public ArrayList<String> handlingTime;
    }

    public class ShippingServiceCost{
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }


}
