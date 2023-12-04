package com.example.ebay_hw3.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ZipCodeResponse {
    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

    public ArrayList<PostalCode> postalCodes;
    public class PostalCode{
        public String adminCode2;
        public String adminCode1;
        public String adminName2;
        public double lng;
        public String countryCode;
        public String postalCode;
        public String adminName1;

        @SerializedName("iSO3166-2")
        public String iSO3166;
        public String placeName;
        public double lat;
    }


}
