package com.example.ebay_hw3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ebay_hw3.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> urls;

    private OnClick onClick;
    public ImageSliderAdapter(Context context, List<String> urls, OnClick onClick) {
        this.context = context;
        this.urls = urls;
        this.onClick = onClick;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_details_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        String url = urls.get(position);
        Glide.with(viewHolder.itemView)
                .load(url)
                .fitCenter()
                .into(viewHolder.image);

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.click();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return urls.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView image;
        View root;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            root = itemView;
        }
    }
    public interface OnClick {
        void click();
    }
}