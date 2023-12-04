package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.ZipCodeResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZipCodeRepositiry {
    final MutableLiveData<ObjectModel> getZipCodeResponseLiveData;
    final ApiEndpointInterface authService;

    public ZipCodeRepositiry() {
        getZipCodeResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getZipCode(String query) {
        authService.getZipCodeResponse(query).enqueue(new Callback<ZipCodeResponse>() {
            @Override
            public void onResponse(Call<ZipCodeResponse> call, Response<ZipCodeResponse> response) {
                if (response.isSuccessful()) {
                    getZipCodeResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getZipCodeResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getZipCodeResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ZipCodeResponse> call, Throwable t) {
                getZipCodeResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getZipCodeResponseLiveData;
    }
}
