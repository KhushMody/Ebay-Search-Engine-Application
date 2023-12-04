package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.SearchResultsRepositiry;

public class SearchResultsViewModel extends ViewModel {
    private final SearchResultsRepositiry searchResultsRepositiry;

    public SearchResultsViewModel() {
        super();
        searchResultsRepositiry = new SearchResultsRepositiry();
    }

    public LiveData<ObjectModel> getSearch(String keyword, String category, String condition, String shippingOptions, String distance, String location) {
        return searchResultsRepositiry.getSearch(keyword, category, condition, shippingOptions, distance, location);
    }

    public void addToCart(String title,
                          String itemId,
                          String price,
                          String image,
                          String shippingCost,
                          String zipCode,
                          String condition) {
        searchResultsRepositiry.addToCart(title, itemId, price, image, shippingCost, zipCode, condition);
    }

    public void removeFromCart(String itemId) {
        searchResultsRepositiry.removeFromCart(itemId);
    }
}
