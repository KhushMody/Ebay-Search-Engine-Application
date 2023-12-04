package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.SimilarProductsResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarProductsRepositiry {
    final MutableLiveData<ObjectModel> getSimilatProductsResponseLiveData;
    final ApiEndpointInterface authService;

    public SimilarProductsRepositiry() {
        getSimilatProductsResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getSimilarProducts(String itemId) {
        authService.getSimilarProductsResponse(itemId).enqueue(new Callback<SimilarProductsResponse>() {
            @Override
            public void onResponse(Call<SimilarProductsResponse> call, Response<SimilarProductsResponse> response) {
                if (response.isSuccessful()) {
                    getSimilatProductsResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getSimilatProductsResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getSimilatProductsResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<SimilarProductsResponse> call, Throwable t) {
                getSimilatProductsResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getSimilatProductsResponseLiveData;
    }
}