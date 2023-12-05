package com.example.ebay_hw3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.PhotosResponse;
import com.example.ebay_hw3.viewmodel.PhotosViewModel;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {

    private static final String ARG_PARAM1 = "title";

    String title;
    private PhotosViewModel photosViewModel;
    RelativeLayout relativeLayout;
    private Observer<ObjectModel> observerEvents;

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;

    public PhotosFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        recyclerView = view.findViewById(R.id.photoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout = view.findViewById(R.id.progressLoadingPhotos);

        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                PhotosResponse response = (PhotosResponse) objectModel.getObj();
                relativeLayout.setVisibility(View.GONE);
                ArrayList<PhotosResponse.Item> items = response.items;
                photoAdapter.setItems(items);
            } else {
                relativeLayout.setVisibility(View.GONE);
                Log.d("khushmody", objectModel.getMessage());
            }
        };

        photosViewModel = new ViewModelProvider(requireActivity()).get(PhotosViewModel.class);
        photosViewModel.getPhotos(title).observe(getViewLifecycleOwner(), observerEvents);
    }

    private static class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

        private ArrayList<PhotosResponse.Item> items = new ArrayList<>();

        public void setItems(ArrayList<PhotosResponse.Item> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            PhotosResponse.Item item = items.get(position);
            Glide.with(holder.itemView)
                    .load(item.link)
                    .into(holder.photoImageView);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class PhotoViewHolder extends RecyclerView.ViewHolder {
            ImageView photoImageView;

            public PhotoViewHolder(@NonNull View itemView) {
                super(itemView);
                photoImageView = itemView.findViewById(R.id.photoImageView);
            }
        }
    }
}