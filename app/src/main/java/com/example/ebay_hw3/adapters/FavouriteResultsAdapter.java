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
import com.example.ebay_hw3.response.FavouritesResponse;

import java.util.ArrayList;

public class FavouriteResultsAdapter extends RecyclerView.Adapter<FavouriteResultsAdapter.ViewHolder> {

    //ArrayList<SearchResultsResponse.Item> items
    private final ArrayList<FavouritesResponse> mValues;
    private final ItemClicks itemClicks;

    public FavouriteResultsAdapter(ArrayList<FavouritesResponse> items, ItemClicks itemClicks) {
        this.mValues = items;
        this.itemClicks = itemClicks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouritesResponse model = mValues.get(position);
        holder.title.setText(model.title);
        holder.condition.setText(model.condition);
        String shippingPrice = model.shippingCost;
        if(shippingPrice.equals("0.0") || shippingPrice.equals("0.00") || shippingPrice.equals("0")){
            shippingPrice = "Free";
        }
        holder.shipping.setText(shippingPrice);
        holder.price.setText(model.price);
        holder.zipcode.setText(model.zipCode);
        if(model.inCart){
            holder.cart_add.setVisibility(View.GONE);
            holder.cart_remove.setVisibility(View.VISIBLE);
        } else {
            holder.cart_add.setVisibility(View.VISIBLE);
            holder.cart_remove.setVisibility(View.GONE);
        }
        Glide.with(holder.logo.getContext()).load(model.image).into(holder.logo);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClicks.itemClick(position);
            }
        });
        holder.cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClicks.likeClick(position);
            }
        });
        holder.cart_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClicks.dislikeClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView logo, cart_add, cart_remove;
        private TextView title,zipcode,shipping,condition,price;

        private RelativeLayout root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            logo = itemView.findViewById(R.id.logo);
            title = itemView.findViewById(R.id.title);
            zipcode = itemView.findViewById(R.id.zipcode);
            shipping = itemView.findViewById(R.id.shipping);
            condition = itemView.findViewById(R.id.condition);
            price = itemView.findViewById(R.id.price);
            cart_add = itemView.findViewById(R.id.cart_add);
            cart_remove = itemView.findViewById(R.id.cart_remove);
        }
    }


    public interface ItemClicks {
        void likeClick(int position);

        void dislikeClick(int position);

        void itemClick(int position);
    }
}
