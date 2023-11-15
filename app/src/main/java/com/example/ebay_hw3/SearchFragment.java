package com.example.ebay_hw3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize UI components and handle logic here...
        editTextKeyword = view.findViewById(R.id.editTextKeyword);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        checkboxNew = view.findViewById(R.id.checkboxNew);
        checkboxUsed = view.findViewById(R.id.checkboxUsed);
        checkboxUnspecified = view.findViewById(R.id.checkboxUnspecified);
        initializeCategoryValuesMap();
        shippingOptions = new ArrayList<>();
        CheckBox checkboxLocalPickup = view.findViewById(R.id.checkboxLocalPickup);
        CheckBox checkboxFreeShipping = view.findViewById(R.id.checkboxFreeShipping);
        CheckBox checkboxEnableNearby = view.findViewById(R.id.checkboxEnableNearby);
        LinearLayout nearbyOptionsLayout = view.findViewById(R.id.nearbyOptionsLayout);
        EditText editTextMilesFrom = view.findViewById(R.id.editTextMilesFrom);
        radioGroupFrom = view.findViewById(R.id.radioGroupFrom);
        EditText editTextZipCode = view.findViewById(R.id.editTextZipCode);

        // Default zip code value
        zipCodeValue = "900047";

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
                    editTextZipCode.setText(zipCodeValue);
                    editTextZipCode.setEnabled(false); // Disable the EditText

                    // Ensure radioButtonZipCode is unchecked
                    radioButtonZipCode.setChecked(false);
                } else if (checkedId == R.id.radioButtonZipCode) {
                    // If radioButtonZipCode is checked, enable the EditText for user input
                    editTextZipCode.setEnabled(true);

                    // Ensure radioButtonCurrentLocation is unchecked
                    radioButtonCurrentLocation.setChecked(false);
                }
            }
        });

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

        Button buttonGetKeywordValue = view.findViewById(R.id.buttonSearch);
        buttonGetKeywordValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move the code to get the value inside the onClick method
                zipCodeValue = editTextZipCode.getText().toString();
                Log.d("YourActivity", "Zipcode value: " + zipCodeValue);

                selectedCheckboxTexts = getSelectedCheckboxTexts();
                Log.d("YourActivity", "Selected Checkbox Texts: " + selectedCheckboxTexts.toString());
                String keywordValue = editTextKeyword.getText().toString();
                Log.d("YourActivity", "Keyword value: " + keywordValue);
            }
        });

        return view;
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
