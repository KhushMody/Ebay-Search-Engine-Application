package com.example.ebay_hw3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.repositiries.PhotosRepositiry;

public class PhotosViewModel extends ViewModel {
    private final PhotosRepositiry photosRepositiry;

    public PhotosViewModel() {
        super();
        photosRepositiry = new PhotosRepositiry();
    }

    public LiveData<ObjectModel> getPhotos(String title) {
        return photosRepositiry.getPhotos(title);
    }
}

