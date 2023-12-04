package com.example.ebay_hw3;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.ItemDetailsResponse;
import com.example.ebay_hw3.response.SearchResultsResponse;
import com.example.ebay_hw3.viewmodel.ItemDetailsViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wssholmes.stark.circular_score.CircularScoreView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //public static final String ARG_PARAM1 = "itemId";
    public static final String ARG_PARAM1 = "itemId";

    public static final String ARG_PARAM4 = "shippingCost";
    public static final String ARG_PARAM2 = "shippingInfo";

    public static final String ARG_PARAM3 = "sellingStatus";

    private ItemDetailsViewModel itemDetailsViewModel;
    private Observer<ObjectModel> observerEvents;

    //public ArrayList<SearchResultsResponse.SellingStatus> ARG_PARAM3 = "sellingStatus";

    // TODO: Rename and change types of parameters
    public CircularScoreView circularScoreView;
    public String tempShippingCost;
    private String itemId;
    private String mParam2;

    public String shippingCost;
    public ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo;
    public ArrayList<SearchResultsResponse.SellingStatus> sellingStatus;

    private TextView storeName;
    private TextView feedbackScore;
    private TextView popularity;
    private TextView feedbackStar;
    private TextView shippingCost1;
    private TextView globalShipping;
    private TextView handlingTime;
    private TextView returnPolicy;
    private TextView returnsWithin;
    private TextView refundMode;
    private TextView shippedBy;
    public String storeURL;

    public ShippingFragment(String itemId, String shippingCost, ArrayList<SearchResultsResponse.ShippingInfo> shippingInfo, ArrayList<SearchResultsResponse.SellingStatus> sellingStatus) {
        // Required empty public constructor
        this.itemId = itemId;
        this.shippingInfo = shippingInfo;
        this.sellingStatus = sellingStatus;
        this.shippingCost = shippingCost;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingFragment.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getString(ARG_PARAM1);

            String shippingInfoJson = getArguments().getString(ARG_PARAM2);
            shippingInfo = new Gson().fromJson(shippingInfoJson, new TypeToken<ArrayList<SearchResultsResponse.ShippingInfo>>() {}.getType());

            String sellingStatusJson = getArguments().getString(ARG_PARAM3);

            sellingStatus = new Gson().fromJson(sellingStatusJson, new TypeToken<ArrayList<SearchResultsResponse.SellingStatus>>() {}.getType());
            if (shippingInfo != null && !shippingInfo.isEmpty()) {
                SearchResultsResponse.ShippingInfo firstShippingInfo = shippingInfo.get(0);
                if (firstShippingInfo != null && firstShippingInfo.shippingServiceCost != null && !firstShippingInfo.shippingServiceCost.isEmpty()) {
                    Toast.makeText(requireContext(), "First Shipping Cost: " + firstShippingInfo.shippingServiceCost.get(0).__value__, Toast.LENGTH_SHORT).show();
                }
            }
            if(shippingCost != null){
                tempShippingCost = shippingCost;
                if(shippingCost.equals("0.0") || shippingCost.equals("0.00")){
                    tempShippingCost = "Free";
                }
                Toast.makeText(requireContext(), "shipping cost = "+shippingCost, Toast.LENGTH_SHORT).show();
            }

            // Example: Display a Toast with the first sellingStatus value
            if (sellingStatus != null && !sellingStatus.isEmpty()) {
                SearchResultsResponse.SellingStatus firstSellingStatus = sellingStatus.get(0);
                if (firstSellingStatus != null && firstSellingStatus.currentPrice != null && !firstSellingStatus.currentPrice.isEmpty()) {
                    Toast.makeText(requireContext(), "First Selling Price: " + firstSellingStatus.currentPrice.get(0).__value__, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemDetailsViewModel = new ViewModelProvider(requireActivity()).get(ItemDetailsViewModel.class);
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                ItemDetailsResponse.Item details = ((ItemDetailsResponse) objectModel.getObj()).item;
                //itemTitle.setText(details.title);
                feedbackScore.setText(String.valueOf(details.seller.feedbackScore));
//                popularity.setText(String.valueOf(details.seller.positiveFeedbackPercent));
                circularScoreView.setScore((int)details.seller.positiveFeedbackPercent);
                handlingTime.setText(String.valueOf(details.handlingTime));
                storeName.setText(details.storefront != null ? details.storefront.storeName : "");
                storeURL = details.storefront != null ? details.storefront.storeURL : "";
                storeName.setMovementMethod(LinkMovementMethod.getInstance());
                storeName.setClickable(true);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        // Handle the click event, open the store URL in a browser, for example
                        if (!storeURL.isEmpty()) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeURL));
                            startActivity(browserIntent);
                        }
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        // Customize the appearance of the clickable text if needed
                        ds.setUnderlineText(true); // Underline the text to indicate it's clickable
                        ds.setColor(ContextCompat.getColor(requireContext(), R.color.purple));
                    }
                };
                SpannableString spannableString = new SpannableString(storeName.getText());
                spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                storeName.setText(spannableString);
                shippingCost1.setText(tempShippingCost);
                globalShipping.setText(details.globalShipping ? "Yes" : "No");
                returnPolicy.setText(details.returnPolicy != null ? details.returnPolicy.returnsAccepted : "");
                returnsWithin.setText(details.returnPolicy != null ? details.returnPolicy.returnsWithin : "");
                refundMode.setText(details.returnPolicy != null ? details.returnPolicy.refund : "");
                shippedBy.setText(details.returnPolicy != null ? details.returnPolicy.shippingCostPaidBy : "");
                ImageView feedbackStar = view.findViewById(R.id.feedbackStar);
                int feedbackScoreValue = details.seller.feedbackScore;
                Drawable starDrawable;

                if (feedbackScoreValue >= 50 && feedbackScoreValue <= 99) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), PorterDuff.Mode.SRC_IN);
                } else if (feedbackScoreValue >= 5000 && feedbackScoreValue <= 9999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_IN);
                }else if (feedbackScoreValue >= 500 && feedbackScoreValue <= 999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 1000 && feedbackScoreValue <= 4999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 500 && feedbackScoreValue <= 999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 100 && feedbackScoreValue <= 499) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.turquoise), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 10 && feedbackScoreValue <= 49) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 90000 && feedbackScoreValue <= 500000) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 100000 && feedbackScoreValue <= 499999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 50000 && feedbackScoreValue <= 99999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 25000 && feedbackScoreValue <= 49999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.turquoise), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 10000 && feedbackScoreValue <= 24999) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow), PorterDuff.Mode.SRC_IN);
                }
                else if (feedbackScoreValue >= 1000000) {
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.silver), PorterDuff.Mode.SRC_IN);
                }
                else {
                    // Default star for unknown range
                    starDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.star_circle_outline);
                    starDrawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN);
                }
                feedbackStar.setImageDrawable(starDrawable);


            } else {
                Log.d("khushmody", objectModel.getMessage());
                Toast.makeText(getContext(), objectModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        shippingCost1 = view.findViewById(R.id.shippingCost1);
        storeName = view.findViewById(R.id.storeName);
        feedbackScore = view.findViewById(R.id.feedbackScore);
        circularScoreView = (CircularScoreView)view.findViewById(R.id.popularity);
        globalShipping = view.findViewById(R.id.globalShipping);
        handlingTime = view.findViewById(R.id.handlingTime);
        returnPolicy = view.findViewById(R.id.returnPolicy);
        refundMode = view.findViewById(R.id.refundMode);
        returnsWithin = view.findViewById(R.id.returnsWithin);
        shippedBy = view.findViewById(R.id.shippedBy);
        itemDetailsViewModel.getItemDetails(itemId).observe(getViewLifecycleOwner(), observerEvents);
    }


}