package com.example.ebay_hw3.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class SearchResultsResponse {
    public ArrayList<FindItemsAdvancedResponse> findItemsAdvancedResponse;

    public class BuyItNowPrice {
        public String currencyId;
        public String __value__;
    }

    public class Condition {
        public ArrayList<String> conditionId;
        public ArrayList<String> conditionDisplayName;
    }

    public class ConvertedBuyItNowPrice {
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }

    public class ConvertedCurrentPrice {
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }

    public class CurrentPrice {
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }

    public class Distance {
        @SerializedName("@unit")
        public String unit;
        public String __value__;
    }

    public class FindItemsAdvancedResponse {
        public ArrayList<String> ack;
        public ArrayList<String> version;
        public ArrayList<String> timestamp;
        public ArrayList<SearchResult> searchResult;
        public ArrayList<PaginationOutput> paginationOutput;
        public ArrayList<String> itemSearchURL;
    }

    public class Item {
        public ArrayList<String> itemId;
        public ArrayList<String> title;
        public ArrayList<String> globalId;
        public ArrayList<PrimaryCategory> primaryCategory;
        public ArrayList<String> galleryURL;
        public ArrayList<String> viewItemURL;
        public ArrayList<ProductId> productId;
        public ArrayList<String> autoPay;
        public ArrayList<String> postalCode;
        public ArrayList<String> location;
        public ArrayList<String> country;
        public ArrayList<ShippingInfo> shippingInfo;
        public ArrayList<SellingStatus> sellingStatus;
        public ArrayList<ListingInfo> listingInfo;
        public ArrayList<String> returnsAccepted;
        public ArrayList<Distance> distance;
        public ArrayList<Condition> condition;
        public ArrayList<String> isMultiVariationListing;
        public ArrayList<String> topRatedListing;

        public ArrayList<ShippingInfo> getShippingInfo() {
            return shippingInfo;
        }

        public ArrayList<SellingStatus> getSellingStatus() {
            return sellingStatus;
        }

        public boolean inCart;
    }

    public class ListingInfo {
        public ArrayList<String> bestOfferEnabled;
        public ArrayList<String> buyItNowAvailable;
        public ArrayList<String> startTime;
        public ArrayList<String> endTime;
        public ArrayList<String> listingType;
        public ArrayList<String> gift;
        public ArrayList<String> watchCount;
        public ArrayList<BuyItNowPrice> buyItNowPrice;
        public ArrayList<ConvertedBuyItNowPrice> convertedBuyItNowPrice;
    }

    public class PaginationOutput {
        public ArrayList<String> pageNumber;
        public ArrayList<String> entriesPerPage;
        public ArrayList<String> totalPages;
        public ArrayList<String> totalEntries;
    }

    public class PrimaryCategory {
        public ArrayList<String> categoryId;
        public ArrayList<String> categoryName;
    }

    public class ProductId {
        @SerializedName("@type")
        public String type;
        public String __value__;
    }

    public class SearchResult {
        @SerializedName("@count")
        public String count;
        public ArrayList<Item> item;
    }

    public class SellingStatus{
        public ArrayList<CurrentPrice> currentPrice;
        public ArrayList<ConvertedCurrentPrice> convertedCurrentPrice;
        public ArrayList<String> sellingState;
        public ArrayList<String> timeLeft;
        public ArrayList<String> bidCount;
    }

    public class ShippingInfo {
        public ArrayList<ShippingServiceCost> shippingServiceCost;
        public ArrayList<String> shippingType;
        public ArrayList<String> shipToLocations;
        public ArrayList<String> expeditedShipping;
        public ArrayList<String> oneDayShippingAvailable;
        public ArrayList<String> handlingTime;
    }

    public class ShippingServiceCost {
        @SerializedName("@currencyId")
        public String currencyId;
        public String __value__;
    }
}

