package com.example.xianweili.googlebooklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianwei li on 7/26/2017.
 */

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    BookAdapter bookAdapter;
    ListView listView;

    String url = "https://www.googleapis.com/books/v1/volumes?q={ansys}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        listView = (ListView) findViewById(R.id.list);
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);
        new BookAsynchTask().execute(url);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = bookAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getPreviewUrl());
                Intent website = new Intent(Intent.ACTION_VIEW,bookUri);
                startActivity(website);
            }
        });
    }

    private class BookAsynchTask extends AsyncTask<String, Void, List<Book>>{

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length <1 || urls[0] == null) {
                return null;
            }
            return QueryUtils.fetchBookDate(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bookAdapter.clear();
            bookAdapter.addAll(books);
        }
    }
}
