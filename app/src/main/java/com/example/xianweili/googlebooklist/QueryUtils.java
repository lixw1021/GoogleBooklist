package com.example.xianweili.googlebooklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.xianweili.googlebooklist.MainActivity.LOG_TAG;

/**
 * Created by xianwei li on 7/26/2017.
 */

public final class QueryUtils {

    private QueryUtils() {
    }

    //  Fetch booklist data from server
    public static List<Book> fetchBookDate(String urlString) {
        URL url = createUrl(urlString);
        String jsonResponse = makeHttpRequest(url);
        return extractBooks(jsonResponse);
    }

    @Nullable
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "problem with create URL", e);
            return null;
        }
        return url;
    }

    //  Make HTTP request and retrieve JSON string from server
    private static String makeHttpRequest(URL url) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                jsonResponse = getJsonFromStream(inputStream);
            } else {
                throw new IOException("HTTP error code: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "problem with makeHTTPrequest", e);
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static String getJsonFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();

        if (inputStream != null) {
            Reader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }

    //  Parse JSON string to get book list
    private static ArrayList<Book> extractBooks(String BooksJSONString) {
        if (BooksJSONString == null) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(BooksJSONString);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentBook = jsonArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String bookTitle = volumeInfo.getString("title");
                String previewLink = volumeInfo.getString("previewLink");

                String author = null;
                try {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    author = authors.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bitmap image = null;
                try {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    String imageLink = imageLinks.getString("smallThumbnail");
                    image = getBitmapFromUrl(imageLink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String description = null;
                try {
                    JSONObject searchInfo = currentBook.getJSONObject("searchInfo");
                    description = searchInfo.getString("textSnippet");
                } catch (JSONException e) {
                    e.printStackTrace();
                    description = "";
                }

                books.add(new Book(previewLink, image, bookTitle, author, description));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book volume JSON results", e);
        }
        return books;
    }

    private static Bitmap getBitmapFromUrl(String imageLink) {
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
