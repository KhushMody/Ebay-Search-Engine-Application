package com.example.ebay_hw3.network;

import com.example.ebay_hw3.response.FavouritesResponse;
import com.example.ebay_hw3.response.ItemDetailsResponse;
import com.example.ebay_hw3.response.PhotosResponse;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.example.ebay_hw3.response.SimilarProductsResponse;
import com.example.ebay_hw3.response.ZipCodeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpointInterface {


    @GET("/api/search")
    Call<SearchResultsResponse> getSearchResponses(@Query("keyword") String keyword,
                                                   @Query("category") String category,
                                                   @Query("condition") String condition,
                                                   @Query("shippingOptions") String shippingOptions,
                                                   @Query("distance") String distance,
                                                   @Query("location") String location);

    @GET("/api/ebayShoppingApi")
    Call<ItemDetailsResponse> getItemDetailsResponses(@Query("itemId") String itemId);

    @GET("/api/photos")
    Call<PhotosResponse> getPhotosResponse(@Query("title") String title);

    @GET("/api/similar-products")
    Call<SimilarProductsResponse> getSimilarProductsResponse(@Query("itemID") String itemId);

    @GET("/api/favorites")
    Call<List<FavouritesResponse>> getFavouritesResponse();

    @GET("/api/mobileAddToCart")
    Call<Void> getAddToCart(
            @Query("title") String title,
            @Query("itemId") String itemId,
            @Query("price") String price,
            @Query("image") String image,
            @Query("shippingCost") String shippingCost,
            @Query("zipCode") String zipCode,
            @Query("condition") String condition
    );
    @GET("/api/zipcode-suggestions")
    Call<ZipCodeResponse> getZipCodeResponse(@Query("query") String query);

    @GET("/api/removeFromCart")
    Call<Void> getRemoveFromCart(@Query("itemId") String itemId);

}