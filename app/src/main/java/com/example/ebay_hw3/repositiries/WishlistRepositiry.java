package com.example.ebay_hw3.repositiries;

import androidx.lifecycle.MutableLiveData;

import com.example.ebay_hw3.network.ApiEndpointInterface;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.network.RemoteDataSource;
import com.example.ebay_hw3.response.FavouritesResponse;

import org.json.JSONObject;

import java.util.List;

import kotlin.io.TextStreamsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistRepositiry {
    final MutableLiveData<ObjectModel> getFavouritesResponseLiveData;
    final ApiEndpointInterface authService;

    public WishlistRepositiry() {
        getFavouritesResponseLiveData = new MutableLiveData<>();
        authService = RemoteDataSource.createService(ApiEndpointInterface.class);
    }

    public MutableLiveData<ObjectModel> getFavourites() {
        authService.getFavouritesResponse().enqueue(new Callback<List<FavouritesResponse>>() {
            @Override
            public void onResponse(Call<List<FavouritesResponse>> call, Response<List<FavouritesResponse>> response) {
                if (response.isSuccessful()) {
                    getFavouritesResponseLiveData.postValue(new ObjectModel(true, response.body(), response.message()));
                } else {
                    try {
                        JSONObject errObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
                        getFavouritesResponseLiveData.postValue(new ObjectModel(false, null, errObj.optString("message")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        getFavouritesResponseLiveData.postValue(new ObjectModel(false, null, response.message()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavouritesResponse>> call, Throwable t) {
                getFavouritesResponseLiveData.postValue(new ObjectModel(false, null, t.getMessage()));
            }
        });
        return getFavouritesResponseLiveData;
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


