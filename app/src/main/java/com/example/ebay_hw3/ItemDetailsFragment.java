package com.example.ebay_hw3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebay_hw3.adapters.ImageSliderAdapter;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.ItemDetailsResponse;
import com.example.ebay_hw3.viewmodel.ItemDetailsViewModel;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemDetailsFragment extends Fragment implements ImageSliderAdapter.OnClick {

    public static final String ARG_PARAM1 = "itemId";

    private ItemDetailsViewModel itemDetailsViewModel;
    private Observer<ObjectModel> observerEvents;

    public ProgressBar loader;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    private String itemId;
    private ListView listViewSpecifications;
    private TextView itemTitle;
    private TextView itemPriceShipping;
    private TextView itemPrice;
    private TextView itemBrand;

    private SliderView sliderView;
    private String shippingCost;
    public ItemDetailsFragment(String itemId, String shippingCost) {
        this.itemId = itemId;
        this.shippingCost = shippingCost;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        itemDetailsViewModel = new ViewModelProvider(requireActivity()).get(ItemDetailsViewModel.class);
        return inflater.inflate(R.layout.fragment_item_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewSpecifications = view.findViewById(R.id.ListViewSpecifications);
        listViewSpecifications.setScrollContainer(false);
        itemTitle = view.findViewById(R.id.itemTitle);
        itemPriceShipping = view.findViewById(R.id.itemPriceShipping);
        itemPrice = view.findViewById(R.id.itemPrice);
        itemBrand = view.findViewById(R.id.itemBrand);
        sliderView = view.findViewById(R.id.imageSlider);
        relativeLayout = view.findViewById(R.id.progressLoadingID);
        linearLayout = view.findViewById(R.id.linearLayoutID);

        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                ItemDetailsResponse.Item details = ((ItemDetailsResponse) objectModel.getObj()).item;
                itemTitle.setText(details.title);
                if(shippingCost.equals("0.0") || shippingCost.equals("0.00")){
                    shippingCost = "Free Shipping";
                }
                else{
                    shippingCost = "$"+shippingCost+" Shipping";
                }
                relativeLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                itemPriceShipping.setText("$" + details.currentPrice.value + " with "+ shippingCost);
                itemPrice.setText("" + details.currentPrice.value);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.activity_list_view_specifications, R.id.SimpleTextView, extractBrand(details));
                listViewSpecifications.setAdapter(arrayAdapter);
                // Set your brand text view here
                String brand = getBrand(details);
                itemBrand.setText(brand);
                sliderView.setSliderAdapter(new ImageSliderAdapter(getContext(), details.pictureURL, this));

            } else {
                relativeLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Log.d("khushmody", objectModel.getMessage());
                Toast.makeText(getContext(), objectModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        itemDetailsViewModel.getItemDetails(itemId).observe(getViewLifecycleOwner(), observerEvents);
    }

    private List<String> extractBrand(ItemDetailsResponse.Item details) {
        List<String> brand = new ArrayList<>();
        if (details != null && details.itemSpecifics != null && details.itemSpecifics.nameValueList != null) {
            for (ItemDetailsResponse.NameValueList nameValue : details.itemSpecifics.nameValueList) {
                if (nameValue.value != null && !nameValue.value.isEmpty()) {
                    brand.add("• " + nameValue.value.get(0)); // Assuming there is only one brand value
                }
            }
        }
        return brand;
    }

    private String getBrand(ItemDetailsResponse.Item details) {
        String brand="";
        if (details != null && details.itemSpecifics != null && details.itemSpecifics.nameValueList != null) {
            for (ItemDetailsResponse.NameValueList nameValue : details.itemSpecifics.nameValueList) {
                if (nameValue.name.equals("Brand") && !nameValue.value.isEmpty()) {
                    brand = nameValue.value.get(0); // Assuming there is only one brand value
                }
            }
        }
        return brand;
    }

    @Override
    public void click() {
        sliderView.slideToNextPosition();
    }
}
