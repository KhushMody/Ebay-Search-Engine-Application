package com.example.ebay_hw3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebay_hw3.adapters.FavouriteResultsAdapter;
import com.example.ebay_hw3.response.FavouritesResponse;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.example.ebay_hw3.viewmodel.WishlistViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;



public class WishlistFragment extends Fragment implements FavouriteResultsAdapter.ItemClicks{

    public RecyclerView recyclerView;
    public FavouriteResultsAdapter searchResultsAdapter;
    public ProgressBar loader;
    private TextView totalPriceTextView;
    private TextView totalCount;
    private LinearLayout linearLayout;
    SharedPreferences mPrefs;
    Gson gson;

    public ArrayList<FavouritesResponse> items;

    private WishlistViewModel wishlistViewModel;

    private CardView noItemsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerView = view.findViewById(R.id.wishlist);
        loader = view.findViewById(R.id.loaderWishlist);
        linearLayout = view.findViewById(R.id.wishlistTotal);
        totalPriceTextView = view.findViewById(R.id.wishlistItemsTotal);
        totalCount = view.findViewById(R.id.wishlistItemsCnt);
        gson = new Gson();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        items = new ArrayList<>();
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        searchResultsAdapter = new FavouriteResultsAdapter(items, this);
        recyclerView.setAdapter(searchResultsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        noItemsTextView = view.findViewById(R.id.noItemsTextView);
        // Initialize UI components and handle logic here...
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        items.addAll(getFavorites());
        searchResultsAdapter.notifyDataSetChanged();
        updateNoItemsVisibility();
        updateTotalPrice();
    }
    void updateTotalPrice() {
        double total = 0;
        int len = items.size();
        for (FavouritesResponse item : items) {
            try {
                // Assuming the price is a string representing a double
                double price = Double.parseDouble(item.price);
                total += price;
            } catch (NumberFormatException e) {
                // Handle invalid price format if necessary
                e.printStackTrace();
            }
        }
        totalCount.setText("Wishlist Total("+String.valueOf(len)+" items)");
        totalPriceTextView.setText("$" + String.valueOf(total));
    }

    void updateNoItemsVisibility() {
        if (items.size() == 0) {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noItemsTextView.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            noItemsTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void likeClick(int position) {
        FavouritesResponse clickedItem = items.get(position);
        clickedItem.inCart = true;
        String title = clickedItem.title;
        String itemId = clickedItem.itemId;
        String price = clickedItem.price;
        String image = clickedItem.image;
        String shippingCost = clickedItem.shippingCost;
        String zipCode = clickedItem.zipCode;
        String condition = clickedItem.condition;
        Toast.makeText(getContext() , title.substring(0,10)+"... was added to wishlist", Toast.LENGTH_SHORT).show();
        wishlistViewModel.addToCart(title, itemId, price, image, shippingCost, zipCode, condition);
        searchResultsAdapter.notifyDataSetChanged();
        updateNoItemsVisibility();
        updateTotalPrice();
    }

    @Override
    public void dislikeClick(int position) {
        FavouritesResponse clickedItem = items.get(position);
        items.remove(position);
        String title = clickedItem.title;
        searchResultsAdapter.notifyDataSetChanged();
        saveFavorites();
        wishlistViewModel.removeFromCart(clickedItem.itemId);
        Toast.makeText(getContext() , title.substring(0,10)+"... was removed from wishlist", Toast.LENGTH_SHORT).show();
        updateNoItemsVisibility();
        updateTotalPrice();
    }

    public void itemClick(int position) {
        FavouritesResponse clickedItem = items.get(position);

        // Extract itemId from the clicked item
        String itemId = clickedItem.itemId;
        String shippingCost = clickedItem.shippingCost;
        ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo = null;
        ArrayList<SearchResultsResponse.SellingStatus> sellingStatus = null;
        String title = clickedItem.title;


        Intent intent = new Intent(requireActivity(), ItemDetailsActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("shippingCost", shippingCost);
        intent.putExtra("shippingInfo", new Gson().toJson(shippingInfo));
        intent.putExtra("sellingStatus", new Gson().toJson(sellingStatus));
        intent.putExtra("title", title);
        startActivity(intent);
    }

    void saveFavorites() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(items);
        prefsEditor.putString("Favorites", json);
        prefsEditor.commit();
    }

    List<FavouritesResponse> getFavorites() {
        List<FavouritesResponse> empty = new ArrayList<>();
        String json = mPrefs.getString("Favorites", gson.toJson(empty));
        return gson.fromJson(json, new TypeToken<List<FavouritesResponse>>() {}.getType());
    }
}
