package com.example.ebay_hw3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ebay_hw3.adapters.ItemDetailsViewPagerAdapter;
import com.example.ebay_hw3.response.FavouritesResponse;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.example.ebay_hw3.viewmodel.SearchResultsViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {

    ItemDetailsViewPagerAdapter itemDetailsViewPagerAdapter;

    FloatingActionButton cart;


    SharedPreferences mPrefs;
    Gson gson;
    MaterialToolbar tb;

    public String title, price, viewItemUrl;


    private SearchResultsViewModel searchResultsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Intent intent = getIntent();
        searchResultsViewModel = new ViewModelProvider(this).get(SearchResultsViewModel.class);
        cart = findViewById(R.id.cart);
        gson = new Gson();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        TabLayout tabLayout = findViewById(R.id.itemDetailsTab);
        ViewPager2 viewPager2 = findViewById(R.id.itemDetailsViewPager);
        final int[] ICONS = new int[]{
                R.drawable.information_variant,
                R.drawable.truck_delivery,
                R.drawable.google,
                R.drawable.equal};
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);
        String itemId = intent.getStringExtra("itemId");
        String shippingCost = intent.getStringExtra("shippingCost");
        title = intent.getStringExtra("title");
        String shippingInfo = intent.getStringExtra("shippingInfo");
        String sellingStatus = intent.getStringExtra("sellingStatus");
        String image = intent.getStringExtra("image");
        price = intent.getStringExtra("price");
        String condition = intent.getStringExtra("condition");
        String zipCode = intent.getStringExtra("zipCode");
        viewItemUrl = intent.getStringExtra("viewItemUrl");
        tb = findViewById(R.id.ItemToolbar);

        itemDetailsViewPagerAdapter = new ItemDetailsViewPagerAdapter(this,
                itemId,
                shippingCost,
                title,
                new Gson().fromJson(
                        shippingInfo,
                        new TypeToken<ArrayList<SearchResultsResponse>>(){}.getType()
                ),
                new Gson().fromJson(
                        sellingStatus,
                        new TypeToken<ArrayList<SearchResultsResponse.SellingStatus>>(){}.getType()
                ));
        viewPager2.setAdapter(itemDetailsViewPagerAdapter);
        initActionBar();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if(intent != null){
//            String itemId = intent.getStringExtra("itemId");
//            if (savedInstanceState == null) {
//                // Create a new instance of ItemDetailsFragment
//                ItemDetailsFragment fragment = new ItemDetailsFragment();
//
//                // Pass the itemId as an argument to the fragment
//                Bundle args = new Bundle();
//                args.putString(ItemDetailsFragment.ARG_PARAM1, itemId);
//                fragment.setArguments(args);
//
//                // Add the fragment to the activity
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.Container, fragment)
//                        .commit();
//            }
//        }

        if(isFavourite(itemId)) {
            cart.setImageDrawable(getDrawable(R.drawable.cart_remove));
        }else{
            cart.setImageDrawable(getDrawable(R.drawable.cart_plus));
        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavourite(itemId)) {
                    searchResultsViewModel.removeFromCart(itemId);
                    removeFavourite(itemId);
                    cart.setImageDrawable(getDrawable(R.drawable.cart_plus));
                    Toast.makeText(getApplicationContext() , title.substring(0,10)+"... was removed from wishlist", Toast.LENGTH_SHORT).show();
                }else{
                    searchResultsViewModel.addToCart(title, itemId, price, image, shippingCost, zipCode, condition);
                    addToFavorites(new FavouritesResponse(itemId, image, title, price, zipCode, shippingCost, condition, true));
                    cart.setImageDrawable(getDrawable(R.drawable.cart_remove));
                    Toast.makeText(getApplicationContext() , title.substring(0,10)+"... was added to wishlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    private void initActionBar() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Make sure this line is present
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.facebook) {

            String quote = "Buy " + title + " at " + price + " from " + viewItemUrl + " below.";
            String url = "https://www.facebook.com/sharer/sharer.php?u=" + viewItemUrl + "&quote=" + quote + "&hashtag=%23CSCI571Fall23AndroidApp";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            // Do something
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}