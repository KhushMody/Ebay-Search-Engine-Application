package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.PhotosResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosRepositiry {
    final MutableLiveData<ObjectModel> getPhotosLiveData;
    final ApiEndpointInterface authService;

    public PhotosRepositiry() {
        getPhotosLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getPhotos(String title) {
        authService.getPhotosResponse(title).enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                if (response.isSuccessful()) {
                    getPhotosLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getPhotosLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getPhotosLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                getPhotosLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getPhotosLiveData;
    }
}
