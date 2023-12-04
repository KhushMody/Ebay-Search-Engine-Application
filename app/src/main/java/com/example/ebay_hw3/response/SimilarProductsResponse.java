package com.example.ebay_hw3.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SimilarProductsResponse {
    public GetSimilarItemsResponse getSimilarItemsResponse;
    public class BuyItNowPrice{
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }

    public class GetSimilarItemsResponse{
        public String ack;
        public String version;
        public String timestamp;
        public ItemRecommendations itemRecommendations;
        public Object diagnostic;
    }

    public class Item{
        public String itemId;
        public String title;
        public String viewItemURL;
        public String globalId;
        public String timeLeft;
        public String primaryCategoryId;
        public String primaryCategoryName;
        public String country;
        public String imageURL;
        public String shippingType;
        public BuyItNowPrice buyItNowPrice;
        public ShippingCost shippingCost;
    }

    public class ItemRecommendations{
        public ArrayList<Item> item;
    }

    public class ShippingCost{
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }


}
