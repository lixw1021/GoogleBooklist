package com.example.xianweili.googlebooklist;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianwei li on 7/26/2017.
 */

public class MainActivity extends AppCompatActivity {
    BookAdapter bookAdapter;
    ListView listView;

    String jsonString =
            "{\"kind\": \"books#volumes\",\r\n \"totalItems\": 3039,\r\n \"items\": [\r\n  {\r\n   \"kind\": \"books#volume\",\r\n   \"id\": \"_zQ5ygAACAAJ\",\r\n   \"etag\": \"LXh/f+f7an0\",\r\n   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/_zQ5ygAACAAJ\",\r\n   \"volumeInfo\": {\r\n    \"title\": \"The Busy Coder's Guide to Advanced Android Development\",\r\n    \"authors\": [\r\n     \"Mark L. Murphy\"\r\n    ],\r\n    \"publisher\": \"Commonsware, LLC\",\r\n    \"publishedDate\": \"2011\",\r\n    \"description\": \"There are many Android programming guides that give you the basics. This book goes beyond simple apps into many areas of Android development that you simply will not find in competing books. Whether you want to add home screen app widgets to your arsenal, or create more complex maps, integrate multimedia features like the camera, integrate tightly with other applications, or integrate scripting languages, this book has you covered. Moreover, this book has over 50 pages of Honeycomb-specific material, from dynamic fragments, to integrating navigation into the action bar, to creating list-based app widgets. It also has a chapter on using NFC, the wireless technology behind Google Wallet and related services. This book is one in CommonsWare's growing series of Android related titles, including \\\"The Busy Coder's Guide to Android Development,\\\" \\\"Android Programming Tutorials,\\\" and the upcoming \\\"Tuning Android Applications.\\\" Table of Contents WebView, Inside and Out Crafting Your Own Views More Fun With ListViews Creating Drawables Home Screen App Widgets Interactive Maps Creating Custom Dialogs and Preferences Advanced Fragments and the Action Bar Animating Widgets Using the Camera Playing Media Handling System Events Advanced Service Patterns Using System Settings and Services Content Provider Theory Content Provider Implementation Patterns The Contacts ContentProvider Searching with SearchManager Introspection and Integration Tapjacking Working with SMS More on the Manifest Device Configuration Push Notifications with C2DM NFC The Role of Scripting Languages The Scripting Layer for Android JVM Scripting Languages Reusable Components Testing Production\",\r\n    \"industryIdentifiers\": [\r\n     {\r\n      \"type\": \"ISBN_10\",\r\n      \"identifier\": \"098167805X\"\r\n     },\r\n     {\r\n      \"type\": \"ISBN_13\",\r\n      \"identifier\": \"9780981678054\"\r\n     }\r\n    ],\r\n    \"readingModes\": {\r\n     \"text\": false,\r\n     \"image\": false\r\n    },\r\n    \"pageCount\": 600,\r\n    \"printType\": \"BOOK\",\r\n    \"categories\": [\r\n     \"Computers\"\r\n    ],\r\n    \"maturityRating\": \"NOT_MATURE\",\r\n    \"allowAnonLogging\": false,\r\n    \"contentVersion\": \"preview-1.0.0\",\r\n    \"imageLinks\": {\r\n     \"smallThumbnail\": \"http://books.google.com/books/content?id=_zQ5ygAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api\",\r\n     \"thumbnail\": \"http://books.google.com/books/content?id=_zQ5ygAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\"\r\n    },\r\n    \"language\": \"en\",\r\n    \"previewLink\": \"http://books.google.com/books?id=_zQ5ygAACAAJ&dq=android&hl=&cd=1&source=gbs_api\",\r\n    \"infoLink\": \"http://books.google.com/books?id=_zQ5ygAACAAJ&dq=android&hl=&source=gbs_api\",\r\n    \"canonicalVolumeLink\": \"https://books.google.com/books/about/The_Busy_Coder_s_Guide_to_Advanced_Andro.html?hl=&id=_zQ5ygAACAAJ\"\r\n   },\r\n   \"saleInfo\": {\r\n    \"country\": \"US\",\r\n    \"saleability\": \"NOT_FOR_SALE\",\r\n    \"isEbook\": false\r\n   },\r\n   \"accessInfo\": {\r\n    \"country\": \"US\",\r\n    \"viewability\": \"NO_PAGES\",\r\n    \"embeddable\": false,\r\n    \"publicDomain\": false,\r\n    \"textToSpeechPermission\":\"ALLOWED\",\r\n\"epub\": {\r\n\"isAvailable\": false\r\n},\r\n\"pdf\":{\r\n\"isAvailable\": false\r\n    },\r\n    \"webReaderLink\": \"http://play.google.com/books/reader?id=_zQ5ygAACAAJ&hl=&printsec=frontcover&source=gbs_api\",\r\n    \"accessViewStatus\": \"NONE\",\r\n    \"quoteSharingAllowed\": false\r\n   },\r\n   \"searchInfo\": {\r\n    \"textSnippet\": \"There are many Android programming guides that give you the basics. This book goes beyond simple apps into many areas of Android development that you simply will not find in competing books.\"\r\n   }\r\n  }\r\n ]\r\n}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        fake data
        ArrayList<Book> books;
//        books = QueryUtils.extractBooks(jsonString);

        listView = (ListView) findViewById(R.id.list);
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);
        new BookAsynchTask().execute(jsonString);
    }

    private class BookAsynchTask extends AsyncTask<String, Void, List<Book>>{

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length <1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.extractBooks(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bookAdapter.clear();
            bookAdapter.addAll(books);
        }
    }
}
