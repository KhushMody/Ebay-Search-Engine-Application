package com.example.ebay_hw3.response;

import java.util.ArrayList;

public class PhotosResponse {
    public String kind;
    public Url url;
    public Queries queries;
    public Context context;
    public SearchInformation searchInformation;
    public ArrayList<Item> items;

    public class Context{
        public String title;
    }

    public class Image{
        public String contextLink;
        public int height;
        public int width;
        public int byteSize;
        public String thumbnailLink;
        public int thumbnailHeight;
        public int thumbnailWidth;
    }

    public class Item{
        public String kind;
        public String title;
        public String htmlTitle;
        public String link;
        public String displayLink;
        public String snippet;
        public String htmlSnippet;
        public String mime;
        public String fileFormat;
        public Image image;
    }

    public class NextPage{
        public String title;
        public String totalResults;
        public String searchTerms;
        public int count;
        public int startIndex;
        public String inputEncoding;
        public String outputEncoding;
        public String safe;
        public String cx;
        public String searchType;
        public String imgSize;
    }

    public class Queries{
        public ArrayList<Request> request;
        public ArrayList<NextPage> nextPage;
    }

    public class Request{
        public String title;
        public String totalResults;
        public String searchTerms;
        public int count;
        public int startIndex;
        public String inputEncoding;
        public String outputEncoding;
        public String safe;
        public String cx;
        public String searchType;
        public String imgSize;
    }

    public class SearchInformation{
        public double searchTime;
        public String formattedSearchTime;
        public String totalResults;
        public String formattedTotalResults;
    }

    public class Url{
        public String type;
        public String template;
    }


}
