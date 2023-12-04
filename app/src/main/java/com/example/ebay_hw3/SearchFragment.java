package com.example.ebay_hw3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ebay_hw3.network.ObjectModel;
import com.example.ebay_hw3.response.ZipCodeResponse;
import com.example.ebay_hw3.viewmodel.SearchResultsViewModel;
import com.example.ebay_hw3.viewmodel.ZipCodeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText editTextKeyword;
    private Spinner spinnerCategory;
    private String selectedCategory;
    private HashMap<String, String> categoryValuesMap;
    private String categoryValue;

    private CheckBox checkboxNew, checkboxUsed, checkboxUnspecified;
    List<String> selectedCheckboxTexts;
    List<String> shippingOptions;

    String milesFromValue;
    String zipCodeValue;

    private RadioButton radioButtonCurrentLocation;
    private RadioButton radioButtonZipCode;
    private RadioGroup radioGroupFrom;

    private SearchResultsViewModel searchResultsViewModel;
    private Observer<ObjectModel> observerEvents;
    boolean checkZip = false;

    private ZipCodeViewModel zipCodeViewModel;

    private AutoCompleteTextView editTextZipCode;

    //String api = ""

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchResultsViewModel = new ViewModelProvider(this).get(SearchResultsViewModel.class);

        // Initialize UI components and handle logic here...
        editTextKeyword = view.findViewById(R.id.editTextKeyword);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        checkboxNew = view.findViewById(R.id.checkboxNew);
        checkboxUsed = view.findViewById(R.id.checkboxUsed);
        checkboxUnspecified = view.findViewById(R.id.checkboxUnspecified);
        zipCodeViewModel = new ViewModelProvider(this).get(ZipCodeViewModel.class);
        initializeCategoryValuesMap();
        shippingOptions = new ArrayList<>();
        CheckBox checkboxLocalPickup = view.findViewById(R.id.checkboxLocalPickup);
        CheckBox checkboxFreeShipping = view.findViewById(R.id.checkboxFreeShipping);
        CheckBox checkboxEnableNearby = view.findViewById(R.id.checkboxEnableNearby);
        LinearLayout nearbyOptionsLayout = view.findViewById(R.id.nearbyOptionsLayout);
        TextView keywordValidation = view.findViewById(R.id.keywordValidation);
        TextView zipcodeValidation = view.findViewById(R.id.zipcodeValidation);
        EditText editTextMilesFrom = view.findViewById(R.id.editTextMilesFrom);
        radioGroupFrom = view.findViewById(R.id.radioGroupFrom);
        editTextZipCode = view.findViewById(R.id.editTextZipCode);

        // Default zip code value
        zipCodeValue = "90047";

        // Set the default zip code value to EditText
        //editTextZipCode.setText(zipCodeValue);

        //RadioGroup radioGroupFrom = view.findViewById(R.id.radioGroupFrom);
        radioButtonCurrentLocation = view.findViewById(R.id.radioButtonCurrentLocation);
        radioButtonZipCode = view.findViewById(R.id.radioButtonZipCode);

        radioButtonCurrentLocation.setChecked(true);

        radioGroupFrom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonCurrentLocation) {
                    // If radioButtonCurrentLocation is checked, set the default zip code
                    //editTextZipCode.setText(zipCodeValue);
                    editTextZipCode.setEnabled(false); // Disable the EditText

                    // Ensure radioButtonZipCode is unchecked
                    radioButtonZipCode.setChecked(false);
                } else if (checkedId == R.id.radioButtonZipCode) {
                    // If radioButtonZipCode is checked, enable the EditText for user input
                    checkZip = true;
                    editTextZipCode.setEnabled(true);
                    editTextZipCode.setFocusable(true);
                    editTextZipCode.setFocusableInTouchMode(true);

                    // Ensure radioButtonCurrentLocation is unchecked
                    radioButtonCurrentLocation.setChecked(false);
                }
            }
        });

        observerEvents = objectModel -> {
            if (objectModel.isStatus()) {
                ZipCodeResponse response = (ZipCodeResponse) objectModel.getObj();
                List<String> suggestions = new ArrayList<>();
                if (response.postalCodes.size() > 0 && response.postalCodes.get(0) != null) {
                    suggestions.add(response.postalCodes.get(0).postalCode);
                }

                if (response.postalCodes.size() > 1 && response.postalCodes.get(1) != null) {
                    suggestions.add(response.postalCodes.get(1).postalCode);
                }

                if (response.postalCodes.size() > 2 && response.postalCodes.get(2) != null) {
                    suggestions.add(response.postalCodes.get(2).postalCode);
                }

                if (response.postalCodes.size() > 3 && response.postalCodes.get(3) != null) {
                    suggestions.add(response.postalCodes.get(3).postalCode);
                }

                if (response.postalCodes.size() > 4 && response.postalCodes.get(4) != null) {
                    suggestions.add(response.postalCodes.get(4).postalCode);
                }

                updateDropdown(suggestions);

            } else {
                Log.d("khushmody", objectModel.getMessage());
                Toast.makeText(getContext(), objectModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
// Add a TextWatcher to monitor changes in the EditText
        editTextZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update the zipCodeValue when the user changes the text
                zipCodeValue = editable.toString();
                Log.d("YourActivity", "Zipcode value: " + zipCodeValue);

                // Call getZipCode method and observe the results
                zipCodeViewModel.getZipCode(zipCodeValue).observe(getViewLifecycleOwner(), observerEvents);
            }
        });


        // Get the value from the EditText
        milesFromValue = editTextMilesFrom.getText().toString();

        // If the EditText is empty, set the default value to 10
        if (milesFromValue.isEmpty()) {
            milesFromValue = "10";
        }
        Log.d("YourActivity", "miles value: " + milesFromValue);
        //String selectCategory;

        //add the shipping options to shippingOptions array
        if (checkboxLocalPickup.isChecked()) {
            shippingOptions.add("LocalPickupOnly");
        }

        if (checkboxFreeShipping.isChecked()) {
            shippingOptions.add("FreeShippingOnly");
            Log.d("YourActivity", "Selected Checkbox shipping Texts: " + shippingOptions.toString());
        }

        // Set an OnCheckedChangeListener to the checkbox
        checkboxEnableNearby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the visibility of nearbyOptionsLayout based on the checkbox state
                nearbyOptionsLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.category_arrays,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        // Set an OnItemSelectedListener to the spinner
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedCategory = parentView.getItemAtPosition(position).toString();
                categoryValue = getCategoryValue(selectedCategory);
                Log.d("YourActivity", "Selected Category: " + selectedCategory + ", Value: " + categoryValue);
                //Log.d("YourActivity", "Selected Category: " + selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if nothing is selected
            }
        });
        Button buttonClear = view.findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset all values to default
                editTextKeyword.setText("");
                spinnerCategory.setSelection(0);
                checkboxNew.setChecked(false);
                checkboxUsed.setChecked(false);
                checkboxUnspecified.setChecked(false);
                editTextMilesFrom.setText("");
                radioButtonCurrentLocation.setChecked(true);
                editTextZipCode.setText("");
                editTextZipCode.setEnabled(false);
                checkboxEnableNearby.setChecked(false);
                checkboxFreeShipping.setChecked(false);
                checkboxLocalPickup.setChecked(false);
                // Ensure radioButtonZipCode is unchecked
                radioButtonZipCode.setChecked(false);
                nearbyOptionsLayout.setVisibility(View.GONE);
                keywordValidation.setVisibility(View.GONE);
                zipcodeValidation.setVisibility(View.GONE);
            }
        });

        Button buttonGetKeywordValue = view.findViewById(R.id.buttonSearch);
        buttonGetKeywordValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move the code to get the value inside the onClick method
                if(checkZip){
                    zipCodeValue = editTextZipCode.getText().toString();
                }
                Log.d("YourActivity", "Zipcode value: " + zipCodeValue);

                selectedCheckboxTexts = getSelectedCheckboxTexts();
                Log.d("YourActivity", "Selected Checkbox Texts: " + selectedCheckboxTexts.toString());
                String keywordValue = editTextKeyword.getText().toString();
                Log.d("YourActivity", "Keyword value: " + keywordValue);
                if(keywordValue.trim().isEmpty() || zipCodeValue.trim().isEmpty()){
                    if(keywordValue.trim().isEmpty()){
                        keywordValidation.setVisibility(View.VISIBLE);
                    }
                    else{
                        keywordValidation.setVisibility(View.GONE);
                    }
                    if(zipCodeValue.trim().isEmpty()){
                        zipcodeValidation.setVisibility(View.VISIBLE);
                    }
                    else{
                        zipcodeValidation.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String result = String.join(", ", stringList);
                //String condition = String.join(", ", selectedCheckboxTexts);
                //String shipping = String.join(", ", shippingOptions);
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);

                // Put data into the Intent
                intent.putExtra("keyword", keywordValue);
                intent.putExtra("category", categoryValue);
                intent.putExtra("condition", String.join(", ", selectedCheckboxTexts));
                intent.putExtra("shipping", String.join(", ", shippingOptions));
                intent.putExtra("milesFrom", milesFromValue);
                intent.putExtra("zipCode", zipCodeValue.isEmpty() ? "90047" : zipCodeValue);
                //searchResultsViewModel.getSearch(keywordValue, categoryValue, condition, shipping, milesFromValue, zipCodeValue.isEmpty()?"90047":zipCodeValue).observe(getViewLifecycleOwner(), observerEvents);
                startActivity(intent);
            }
        });


        return view;
    }
    private void updateDropdown(List<String> suggestions) {
        // Update your dropdown with the new suggestions
        // This could be a dropdown menu, autocomplete text view, or any other UI component
        editTextZipCode.setDropDownBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.color.white));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, suggestions);
        editTextZipCode.setThreshold(2);
        editTextZipCode.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initializeCategoryValuesMap() {
        categoryValuesMap = new HashMap<>();
        categoryValuesMap.put("All", "");
        categoryValuesMap.put("Art", "550");
        categoryValuesMap.put("Baby", "2984");
        categoryValuesMap.put("Books", "267");
        categoryValuesMap.put("Clothing, Shoes and Accessories", "11450");
        categoryValuesMap.put("Computer, Tablets and Networks", "58058");
        categoryValuesMap.put("Health and Beauty", "26395");
        categoryValuesMap.put("Music", "11233");
        categoryValuesMap.put("Video, Games and Consoles", "1249");
        // Add more mappings for other categories
    }

    // Get the value for a given category
    private String getCategoryValue(String category) {
        // Default to 0 if the category is not found in the map
        return categoryValuesMap.getOrDefault(category, "");
    }

    private List<String> getSelectedCheckboxTexts() {
        List<String> selectedCheckboxTexts = new ArrayList<>();

        if (checkboxNew.isChecked()) {
            selectedCheckboxTexts.add(checkboxNew.getText().toString());
        }

        if (checkboxUsed.isChecked()) {
            selectedCheckboxTexts.add(checkboxUsed.getText().toString());
        }

        if (checkboxUnspecified.isChecked()) {
            selectedCheckboxTexts.add(checkboxUnspecified.getText().toString());
        }

        return selectedCheckboxTexts;
    }

}
