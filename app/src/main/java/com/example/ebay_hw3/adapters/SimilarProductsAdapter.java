package com.example.ebay_hw3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ebay_hw3.R;
import com.example.ebay_hw3.response.SimilarProductsResponse;

import java.util.ArrayList;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ViewHolder> {

    private final ArrayList<SimilarProductsResponse.Item> mValues;

    public SimilarProductsAdapter(ArrayList<SimilarProductsResponse.Item> items) {
        this.mValues = items;
    }

    @NonNull
    @Override
    public SimilarProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_similar_item, parent, false);
        return new SimilarProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimilarProductsResponse.Item model = mValues.get(position);
        holder.similarTitle.setText(model.title);
        String timeLeft = model.timeLeft;
        int startIndex = timeLeft.indexOf("P") + 1;  // Find the index of "P" and move to the next character
        int endIndex = timeLeft.indexOf("D");         // Find the index of "D"

        String daysLeft = timeLeft.substring(startIndex, endIndex) + " Days Left";
        holder.similarDaysLeft.setText(daysLeft);
        holder.similarPrice.setText(model.buyItNowPrice.__value__);
        String shippingCost = model.shippingCost.__value__;

        holder.similarShippingCost.setText(shippingCost);
        Glide.with(holder.similarLogo.getContext()).load(model.imageURL).into(holder.similarLogo);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView similarLogo;
        private TextView similarTitle,similarShippingCost,similarDaysLeft, similarPrice;

        private RelativeLayout root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            similarLogo = itemView.findViewById(R.id.similarLogo);
            similarTitle = itemView.findViewById(R.id.similarTitle);
            similarPrice = itemView.findViewById(R.id.similarPrice);
            similarShippingCost = itemView.findViewById(R.id.similarShippingCost);
            similarDaysLeft = itemView.findViewById(R.id.similarDaysLeft);
        }
    }
}
