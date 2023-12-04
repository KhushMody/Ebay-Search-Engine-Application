package com.example.ebay_hw3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ebay_hw3.adapters.MyViewPagerAdapter;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.FavouritesResponse;
import com.example.ebay_hw3.viewmodel.WishlistViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    private WishlistViewModel wishlistViewModel;
    private Observer<ObjectModel> observerEvents;
    MaterialToolbar tb;

    SharedPreferences mPrefs;
    private boolean keep = true;
    private final int DELAY = 1250;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepVisibleCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return keep;
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(runner, DELAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tabs);



        tabLayout = findViewById(R.id.tabLayout);
        gson = new Gson();
        viewPager2 = findViewById(R.id.viewPager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tb = findViewById(R.id.toolbar);
        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                List<FavouritesResponse> favouritesResponse = (List<FavouritesResponse>) objectModel.getObj();
                for(FavouritesResponse f : favouritesResponse){
                    f.inCart = true;
                }
                List<FavouritesResponse> events = new ArrayList();
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                events.addAll(favouritesResponse);
                String json = gson.toJson(events);
                prefsEditor.putString("Favorites", json);
                prefsEditor.commit();

                // Extract the data you need from searchResponse


            } else {

            }
        };
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        wishlistViewModel.getFavourites().observe(this, observerEvents);
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

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    private void initActionBar() {
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Product Search");
    }

    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            keep = false;
        }
    };
}