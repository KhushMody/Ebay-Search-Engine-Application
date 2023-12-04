package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.SimilarProductsRepositiry;

public class SimilarProductsViewModel extends ViewModel {
    private final SimilarProductsRepositiry similarProductsRepositiry;

    public SimilarProductsViewModel() {
        super();
        similarProductsRepositiry = new SimilarProductsRepositiry();
    }

    public LiveData<ObjectModel> getSimilarProducts(String itemId) {
        return similarProductsRepositiry.getSimilarProducts(itemId);
    }
}
