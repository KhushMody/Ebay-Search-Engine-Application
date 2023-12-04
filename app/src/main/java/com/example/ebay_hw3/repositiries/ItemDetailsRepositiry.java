package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.ItemDetailsResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsRepositiry {
    final MutableLiveData<ObjectModel> getItemDetailsResponseLiveData;
    final ApiEndpointInterface authService;

    public ItemDetailsRepositiry() {
        getItemDetailsResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getItemDetails(String itemId) {
        authService.getItemDetailsResponses(itemId).enqueue(new Callback<ItemDetailsResponse>() {
            @Override
            public void onResponse(Call<ItemDetailsResponse> call, Response<ItemDetailsResponse> response) {
                if (response.isSuccessful()) {
                    getItemDetailsResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getItemDetailsResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getItemDetailsResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemDetailsResponse> call, Throwable t) {
                getItemDetailsResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getItemDetailsResponseLiveData;
    }
}

