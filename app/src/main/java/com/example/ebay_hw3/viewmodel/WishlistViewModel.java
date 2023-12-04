package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.WishlistRepositiry;

public class WishlistViewModel extends ViewModel {
    private final WishlistRepositiry favouritesRepositiry;

    public WishlistViewModel() {
        super();
        favouritesRepositiry = new WishlistRepositiry();
    }


    public LiveData<ObjectModel> getFavourites() {
        return favouritesRepositiry.getFavourites();
    }


    public void addToCart(String title,
                          String itemId,
                          String price,
                          String image,
                          String shippingCost,
                          String zipCode,
                          String condition) {
        favouritesRepositiry.addToCart(title, itemId, price, image, shippingCost, zipCode, condition);
    }

    public void removeFromCart(String itemId) {
        favouritesRepositiry.removeFromCart(itemId);
    }
}

