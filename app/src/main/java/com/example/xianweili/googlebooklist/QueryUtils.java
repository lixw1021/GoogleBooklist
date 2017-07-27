package com.example.xianweili.googlebooklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianwei li on 7/26/2017.
 */

public final class QueryUtils {

    private QueryUtils() {
    }



    public static ArrayList<Book> extractBooks (String BooksJSONString) {
        if (BooksJSONString == null) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(BooksJSONString);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentBook = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String bookTitle = volumeInfo.getString("title");
                String previewLink = volumeInfo.getString("previewLink");

                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageLink = imageLinks.getString("smallThumbnail");
                Bitmap image = getBitmapFromUrl(imageLink);

                JSONObject searchInfo = currentBook.getJSONObject("searchInfo");
                String description = searchInfo.getString("textSnippet");

                books.add(new Book(previewLink, image, bookTitle, author, description));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book volume JSON results", e);
        }
        return books;
    }

    public static Bitmap getBitmapFromUrl(String imageLink) {
        try {
            URL url = new URL(imageLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem parsing the bitmap results", e);
            return null;
        }
    }
}
