package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.ZipCodeRepositiry;

public class ZipCodeViewModel extends ViewModel {
    private final ZipCodeRepositiry zipCodeRepositiry;

    public ZipCodeViewModel() {
        super();
        zipCodeRepositiry = new ZipCodeRepositiry();
    }

    public LiveData<ObjectModel> getZipCode(String query) {
        return zipCodeRepositiry.getZipCode(query);
    }
}
