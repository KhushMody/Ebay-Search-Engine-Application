package com.example.ebay_hw3.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ebay_hw3.ItemDetailsFragment;
import com.example.ebay_hw3.PhotosFragment;
import com.example.ebay_hw3.ShippingFragment;
import com.example.ebay_hw3.SimilarFragment;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ItemDetailsViewPagerAdapter extends FragmentStateAdapter {
    String itemid;

    String shippingCost;

    String title;
    ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo;
    ArrayList<SearchResultsResponse.SellingStatus> sellingStatus;


    public ItemDetailsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String itemId, String shippingCost, String title,  ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo, ArrayList<SearchResultsResponse.SellingStatus> sellingStatus) {
        super(fragmentActivity);
        this.shippingCost = shippingCost;
        this.itemid = itemId;
        this.title = title;
        this.shippingInfo = shippingInfo;
        this.sellingStatus = sellingStatus;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ItemDetailsFragment(itemid, shippingCost);
            case 1:
                ShippingFragment shippingFragment = new ShippingFragment(itemid,shippingCost, shippingInfo, sellingStatus);
                Bundle shippingArgs = new Bundle();
                shippingArgs.putString(ShippingFragment.ARG_PARAM1, itemid);
                String shippingInfoParse = new Gson().toJson(shippingInfo);
                shippingArgs.putString(ShippingFragment.ARG_PARAM2, shippingInfoParse);
                shippingArgs.putString(ShippingFragment.ARG_PARAM3, new Gson().toJson(sellingStatus));
                shippingArgs.putString(ShippingFragment.ARG_PARAM4, shippingCost);
                shippingFragment.setArguments(shippingArgs);
                return shippingFragment;
            case 2:
                return new PhotosFragment(title);
            case 3:
                return new SimilarFragment(itemid);
            default:
                return new ItemDetailsFragment(itemid, shippingCost);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

