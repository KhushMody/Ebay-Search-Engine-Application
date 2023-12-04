package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.ItemDetailsRepositiry;


public class ItemDetailsViewModel extends ViewModel {
    private final ItemDetailsRepositiry itemDetailsRepositiry;

    public ItemDetailsViewModel() {
        super();
        itemDetailsRepositiry = new ItemDetailsRepositiry();
    }

    public LiveData<ObjectModel> getItemDetails(String itemId) {
        return itemDetailsRepositiry.getItemDetails(itemId);
    }
}
