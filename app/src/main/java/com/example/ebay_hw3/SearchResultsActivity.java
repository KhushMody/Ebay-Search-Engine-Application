package com.example.ebay_hw3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebay_hw3.adapters.SearchResultsAdapter;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.FavouritesResponse;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.example.ebay_hw3.viewmodel.SearchResultsViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.ItemClicks {
    private SearchResultsViewModel searchResultsViewModel;
    private Observer<ObjectModel> observerEvents;
    public SearchResultsAdapter searchResultsAdapter;
    public RecyclerView recyclerView;

    RelativeLayout relativeLayout;
    public ProgressBar loader;
    public SearchResultsResponse searchResultsResponse;
    public ArrayList<SearchResultsResponse.Item> items;

    MaterialToolbar tb;
    SharedPreferences mPrefs;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchResultsViewModel = new ViewModelProvider(this).get(SearchResultsViewModel.class);
        items = new ArrayList<>();
        Intent intent = getIntent();
        gson = new Gson();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        searchResultsAdapter = new SearchResultsAdapter(items, this);
        recyclerView = findViewById(R.id.list);
        tb = findViewById(R.id.SRtoolbar);
        loader = findViewById(R.id.loader);
        relativeLayout = findViewById(R.id.progressLoading);
        recyclerView.setAdapter(searchResultsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                Log.d("khushmody", ((SearchResultsResponse) objectModel.getObj()).findItemsAdvancedResponse.get(0).searchResult.get(0).count.toString());
                searchResultsResponse = (SearchResultsResponse) objectModel.getObj();

                // Extract the data you need from searchResponse

                String count = searchResultsResponse.findItemsAdvancedResponse.get(0).searchResult.get(0).count;
                items.clear();
                for(SearchResultsResponse.Item item : searchResultsResponse.findItemsAdvancedResponse.get(0).searchResult.get(0).item) {
                    item.inCart = isFavourite(item.itemId.get(0));
                    items.add(item);
                }
                relativeLayout.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                searchResultsAdapter.notifyDataSetChanged();
                Log.d("khushmody", "Count: " + count);
                Log.d("khushmody", "Items: " + items.size());

                // Create an Intent

                // Put the extracted data into the Intent

                // Start the SearchResultsActivity with the Intent
            } else {
                relativeLayout.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                Log.d("khushmody", objectModel.getMessage());
            }
        };
        initActionBar();

        if (intent != null) {
            String keyword = intent.getStringExtra("keyword");
            String category = intent.getStringExtra("category");
            String condition = intent.getStringExtra("condition");
            String shipping = intent.getStringExtra("shipping");
            String milesFrom = intent.getStringExtra("milesFrom");
            String zipCode = intent.getStringExtra("zipCode");

            // Use the data as needed
            Log.d("SearchResultsActivity", "Keyword: " + keyword);
            Log.d("SearchResultsActivity", "Category: " + category);
            Log.d("SearchResultsActivity", "Condition: " + condition);
            Log.d("SearchResultsActivity", "Shipping: " + shipping);
            Log.d("SearchResultsActivity", "Miles From: " + milesFrom);
            Log.d("SearchResultsActivity", "Zip Code: " + zipCode);

            searchResultsViewModel.getSearch(keyword, category, condition, shipping, milesFrom, zipCode).observe(this, observerEvents);
        }
    }

    @Override
    public void likeClick(int position) {
        SearchResultsResponse.Item clickedItem = items.get(position);
        clickedItem.inCart = true;
        String title = clickedItem.title.get(0);
        String itemId = clickedItem.itemId.get(0);
        String price = clickedItem.sellingStatus.get(0).currentPrice.get(0).__value__;
        String image = clickedItem.galleryURL.get(0);
        String shippingCost = clickedItem.shippingInfo.get(0).shippingServiceCost.get(0).__value__;
        String zipCode = clickedItem.postalCode.get(0);
        String condition = clickedItem.condition.get(0).conditionDisplayName.get(0);
        searchResultsViewModel.addToCart(title, itemId, price, image, shippingCost, zipCode, condition);
        searchResultsAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext() , title.substring(0,10)+"... was added to wishlist", Toast.LENGTH_SHORT).show();
        addToFavorites(new FavouritesResponse(itemId, image, title, price, zipCode, condition, shippingCost, true));
    }

    @Override
    public void dislikeClick(int position) {
        SearchResultsResponse.Item clickedItem = items.get(position);
        clickedItem.inCart = false;
        searchResultsAdapter.notifyDataSetChanged();
        String title = clickedItem.title.get(0);
        removeFavourite(clickedItem.itemId.get(0));
        Toast.makeText(getApplicationContext() , title.substring(0,10)+"... was removed from wishlist", Toast.LENGTH_SHORT).show();
        searchResultsViewModel.removeFromCart(clickedItem.itemId.get(0));
    }

    @Override
    public void itemClick(int position) {
        SearchResultsResponse.Item clickedItem = items.get(position);

        // Extract itemId from the clicked item
        String itemId = clickedItem.itemId.get(0);
        String shippingCost = clickedItem.shippingInfo.get(0).shippingServiceCost.get(0).__value__;
        String price = clickedItem.sellingStatus.get(0).currentPrice.get(0).__value__;
        String image = clickedItem.galleryURL.get(0);
        String zipCode = clickedItem.postalCode.get(0);
        String condition = clickedItem.condition.get(0).conditionDisplayName.get(0);
        String itemUrl = clickedItem.viewItemURL.get(0);
        ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo = clickedItem.getShippingInfo();
        ArrayList<SearchResultsResponse.SellingStatus> sellingStatus = clickedItem.getSellingStatus();
        String title = clickedItem.title.get(0);
        if(shippingCost.equals("0.00")){
            shippingCost = "Free";
        }



        Intent intent = new Intent(SearchResultsActivity.this, ItemDetailsActivity.class);
        intent.putExtra("itemId", itemId);
        intent.putExtra("shippingCost", shippingCost);
        intent.putExtra("shippingInfo", new Gson().toJson(shippingInfo));
        intent.putExtra("sellingStatus", new Gson().toJson(sellingStatus));
        intent.putExtra("title", title);
        intent.putExtra("price", price);
        intent.putExtra("zipCode", zipCode);
        intent.putExtra("image", image);
        intent.putExtra("condition", condition);
        intent.putExtra("viewItemUrl", itemUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void addToFavorites(FavouritesResponse item) {
        List<FavouritesResponse> fav = getFavorites();
        fav.add(item);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(fav);
        prefsEditor.putString("Favorites", json);
        prefsEditor.commit();
    }

    List<FavouritesResponse> getFavorites() {
        List<FavouritesResponse> empty = new ArrayList<>();
        String json = mPrefs.getString("Favorites", gson.toJson(empty));
        return gson.fromJson(json, new TypeToken<List<FavouritesResponse>>() {}.getType());
    }

    Boolean isFavourite(String itemId){
        for(FavouritesResponse f : getFavorites()){
            if(f.itemId.equals(itemId)) return true;
        }
        return false;
    }

    void removeFavourite(String itemId) {
        List<FavouritesResponse> fav = getFavorites();
        for(int i=0;i<fav.size();i++){
            if(fav.get(i).itemId.equals(itemId)){
                fav.remove(i);
            }
        }
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(fav);
        prefsEditor.putString("Favorites", json);
        prefsEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for(SearchResultsResponse.Item item : items) {
            item.inCart = isFavourite(item.itemId.get(0));
        }
        searchResultsAdapter.notifyDataSetChanged();
    }

    private void initActionBar() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void onBackPressed() {
        super.onBackPressed(); // Make sure this line is present
    }
}