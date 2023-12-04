package com.example.ebay_hw3.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemDetailsResponse{
    @SerializedName("Timestamp")
    public String timestamp;
    @SerializedName("Ack")
    public String ack;
    @SerializedName("Build")
    public String build;
    @SerializedName("Version")
    public String version;
    @SerializedName("Item")
    public Item item;

    public class ConvertedCurrentPrice{
        @SerializedName("Value")
        public double value;
        @SerializedName("CurrencyID")
        public String currencyID;
    }

    public class CurrentPrice{
        @SerializedName("Value")
        public double value;
        @SerializedName("CurrencyID")
        public String currencyID;
    }

    public class Item{
        @SerializedName("BestOfferEnabled")
        public boolean bestOfferEnabled;
        @SerializedName("Description")
        public String description;
        @SerializedName("ItemID")
        public String itemID;
        @SerializedName("EndTime")
        public String endTime;
        @SerializedName("StartTime")
        public String startTime;
        @SerializedName("ViewItemURLForNaturalSearch")
        public String viewItemURLForNaturalSearch;
        @SerializedName("ListingType")
        public String listingType;
        @SerializedName("Location")
        public String location;
        @SerializedName("PaymentMethods")
        public ArrayList<Object> paymentMethods;
        @SerializedName("PictureURL")
        public ArrayList<String> pictureURL;
        @SerializedName("PostalCode")
        public String postalCode;
        @SerializedName("PrimaryCategoryID")
        public String primaryCategoryID;
        @SerializedName("PrimaryCategoryName")
        public String primaryCategoryName;
        @SerializedName("Quantity")
        public int quantity;
        @SerializedName("Seller")
        public Seller seller;
        @SerializedName("BidCount")
        public int bidCount;
        @SerializedName("ConvertedCurrentPrice")
        public ConvertedCurrentPrice convertedCurrentPrice;
        @SerializedName("CurrentPrice")
        public CurrentPrice currentPrice;
        @SerializedName("ListingStatus")
        public String listingStatus;
        @SerializedName("QuantitySold")
        public int quantitySold;
        @SerializedName("ShipToLocations")
        public ArrayList<String> shipToLocations;
        @SerializedName("Site")
        public String site;
        @SerializedName("TimeLeft")
        public String timeLeft;
        @SerializedName("Title")
        public String title;
        @SerializedName("ItemSpecifics")
        public ItemSpecifics itemSpecifics;
        @SerializedName("PrimaryCategoryIDPath")
        public String primaryCategoryIDPath;
        @SerializedName("Storefront")
        public Storefront storefront;
        @SerializedName("Country")
        public String country;
        @SerializedName("ReturnPolicy")
        public ReturnPolicy returnPolicy;
        @SerializedName("AutoPay")
        public boolean autoPay;
        @SerializedName("PaymentAllowedSite")
        public ArrayList<Object> paymentAllowedSite;
        @SerializedName("IntegratedMerchantCreditCardEnabled")
        public boolean integratedMerchantCreditCardEnabled;
        @SerializedName("HandlingTime")
        public int handlingTime;
        @SerializedName("ConditionID")
        public int conditionID;
        @SerializedName("ConditionDisplayName")
        public String conditionDisplayName;
        @SerializedName("ExcludeShipToLocation")
        public ArrayList<String> excludeShipToLocation;
        @SerializedName("GlobalShipping")
        public boolean globalShipping;
        @SerializedName("ConditionDescription")
        public String conditionDescription;
        @SerializedName("QuantitySoldByPickupInStore")
        public int quantitySoldByPickupInStore;
        @SerializedName("NewBestOffer")
        public boolean newBestOffer;

        public boolean inCart;
    }

    public class ItemSpecifics{
        @SerializedName("NameValueList")
        public ArrayList<NameValueList> nameValueList;
    }

    public class NameValueList{
        @SerializedName("Name")
        public String name;
        @SerializedName("Value")
        public ArrayList<String> value;
    }

    public class ReturnPolicy{
        @SerializedName("Refund")
        public String refund;
        @SerializedName("ReturnsWithin")
        public String returnsWithin;
        @SerializedName("ReturnsAccepted")
        public String returnsAccepted;
        @SerializedName("ShippingCostPaidBy")
        public String shippingCostPaidBy;
        @SerializedName("InternationalReturnsAccepted")
        public String internationalReturnsAccepted;
    }



    public class Seller{
        @SerializedName("UserID")
        public String userID;
        @SerializedName("FeedbackRatingStar")
        public String feedbackRatingStar;
        @SerializedName("FeedbackScore")
        public int feedbackScore;
        @SerializedName("PositiveFeedbackPercent")
        public double positiveFeedbackPercent;
    }

    public class Storefront{
        @SerializedName("StoreURL")
        public String storeURL;
        @SerializedName("StoreName")
        public String storeName;
    }
}
