package com.example.ebay_hw3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebay_hw3.adapters.SimilarProductsAdapter;
import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.SimilarProductsResponse;
import com.example.ebay_hw3.viewmodel.SimilarProductsViewModel;

import java.util.ArrayList;

public class SimilarFragment extends Fragment {

    private SimilarProductsViewModel similarProductsViewModel;
    private Observer<ObjectModel> observerEvents;
    public SimilarProductsAdapter similarProductsAdapter;
    public RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    public SimilarProductsResponse similarProductsResponse;
    public ArrayList<SimilarProductsResponse.Item> items;
    private static final String ARG_PARAM1 = "itemId";
    private String itemId;

    private Spinner sortCriteriaSpinner;
    private Spinner orderSpinner;

    private boolean isAscendingOrder = true;
    public SimilarFragment(String itemId) {
        // Required empty public constructor
        this.itemId = itemId;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_similar, container, false);

        sortCriteriaSpinner = view.findViewById(R.id.sortCriteriaSpinner);
        orderSpinner = view.findViewById(R.id.orderSpinner);

        // Set up item selection listeners
        setUpSortCriteriaSpinner();
        setUpOrderSpinner();

        // Rest of your onCreateView method...
        similarProductsViewModel = new ViewModelProvider(requireActivity()).get(SimilarProductsViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = new ArrayList<>();
        recyclerView = view.findViewById(R.id.similarList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        similarProductsAdapter = new SimilarProductsAdapter(items);
        relativeLayout = view.findViewById(R.id.progressLoadingSimilar);
        recyclerView.setAdapter(similarProductsAdapter);
        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                //Log.d("khushmody", ((SearchResultsResponse) objectModel.getObj()).findItemsAdvancedResponse.get(0).searchResult.get(0).count.toString());
                similarProductsResponse = (SimilarProductsResponse) objectModel.getObj();
                relativeLayout.setVisibility(View.GONE);

                // Extract the data you need from searchResponse

                //String count = searchResultsResponse.findItemsAdvancedResponse.get(0).searchResult.get(0).count;
                items.clear();
                items.addAll(similarProductsResponse.getSimilarItemsResponse.itemRecommendations.item);
                similarProductsAdapter.notifyDataSetChanged();
                Log.d("khushmodySimilar", "Items: " + items.size());

                // Create an Intent

                // Put the extracted data into the Intent

                // Start the SearchResultsActivity with the Intent
            } else {
                relativeLayout.setVisibility(View.GONE);
                Log.d("khushmodySimilar", objectModel.getMessage());
            }
        };
        similarProductsViewModel.getSimilarProducts(itemId).observe(getViewLifecycleOwner(), observerEvents);
    }

    // Inside your SimilarFragment class

    private void setUpSortCriteriaSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_criteria_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortCriteriaSpinner.setAdapter(adapter);

        sortCriteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update the sort criteria based on the selected item
                updateSortCriteria();

                // Enable or disable the orderSpinner based on the selected item
                if (position == 0) { // Assuming 0 is the position of the default item
                    orderSpinner.setEnabled(false);
                } else {
                    orderSpinner.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

// Rest of your code...

    private void setUpOrderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_order_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(adapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update the sorting order based on the selected item
                updateSortOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void updateSortCriteria() {
        // Implement logic to update sorting criteria
        // You can retrieve the selected item from sortCriteriaSpinner.getSelectedItem()
        String selectedItem = sortCriteriaSpinner.getSelectedItem().toString();
        if(selectedItem.equals("Name")){
            if(isAscendingOrder){
                items.sort((item1, item2) -> item1.title.compareTo(item2.title));
            }
            else{
                items.sort((item1, item2) -> item2.title.compareTo(item1.title));
            }
        } else if (selectedItem.equals("Price")) {
            if(isAscendingOrder){
                items.sort((item1,item2) -> Float.valueOf(item1.buyItNowPrice.__value__).compareTo(Float.valueOf(item2.buyItNowPrice.__value__)));
            }
            else{
                items.sort((item1,item2) -> Float.valueOf(item2.buyItNowPrice.__value__).compareTo(Float.valueOf(item1.buyItNowPrice.__value__)));
            }
        } else if (selectedItem.equals("Days")) {
            if(isAscendingOrder){
                items.sort((item1,item2) -> Float.valueOf(item1.timeLeft.substring(item1.timeLeft.indexOf("P") + 1, item1.timeLeft.indexOf("D"))).compareTo(Float.valueOf(item2.timeLeft.substring(item2.timeLeft.indexOf("P") + 1, item2.timeLeft.indexOf("D")))));
            }
            else{
                items.sort((item1,item2) -> Float.valueOf(item2.timeLeft.substring(item2.timeLeft.indexOf("P") + 1, item2.timeLeft.indexOf("D"))).compareTo(Float.valueOf(item1.timeLeft.substring(item1.timeLeft.indexOf("P") + 1, item1.timeLeft.indexOf("D")))));
            }
        }
        similarProductsAdapter.notifyDataSetChanged();
    }

    private void updateSortOrder() {
        // Implement logic to update sorting order
        // You can retrieve the selected item from orderSpinner.getSelectedItem()
        String selectedItem = orderSpinner.getSelectedItem().toString();
        if(selectedItem.equals("Ascending")){
            isAscendingOrder = true;
        }
        else{
            isAscendingOrder = false;
        }
        updateSortCriteria();
    }
}