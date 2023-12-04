package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.SearchResultsResponse;

import org.json.JSONObject;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsRepositiry {
        final MutableLiveData<ObjectModel> getSearchResponseLiveData;
        final ApiEndpointInterface authService;

        public SearchResultsRepositiry() {
            getSearchResponseLiveData = new MutableLiveData<>();
            authService = RemoteDataSource.createService(ApiEndpointInterface.class);
        }

        public MutableLiveData<ObjectModel> getSearch(String keyword, String category, String condition, String shippingOptions, String distance, String location) {
            authService.getSearchResponses(keyword, category, condition, shippingOptions, distance, location).enqueue(new Callback<SearchResultsResponse>() {
                @Override
                public void onResponse(Call<SearchResultsResponse> call, Response<SearchResultsResponse> response) {
                    if (response.isSuccessful()) {
                        getSearchResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                    } else {
                        try {
                            JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                            getSearchResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                        } catch (Exception e) {
                            e.printStackTrace();
                            getSearchResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchResultsResponse> call, Throwable t) {
                    getSearchResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
                }
            });
            return getSearchResponseLiveData;
        }

    public void addToCart(
            String title,
            String itemId,
            String price,
            String image,
            String shippingCost,
            String zipCode,
            String condition) {
        authService.getAddToCart(title, itemId, price, image, shippingCost, zipCode, condition).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void removeFromCart(
            String itemId
    ) {
        authService.getRemoveFromCart(itemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
