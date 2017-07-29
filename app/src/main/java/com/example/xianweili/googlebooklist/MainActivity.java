package com.example.xianweili.googlebooklist;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianwei li on 7/26/2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    Button searchButton;
    EditText searchText;
    BookAdapter bookAdapter;
    ListView listView;
    String inputString;
    View emptyView;
    View noDataView;
    String createSearchUrl;

    StringBuilder url = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        listView = (ListView) findViewById(R.id.list);
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(bookAdapter);

        emptyView = findViewById(R.id.empty_list);
//        noDataView = findViewById(R.id.no_data_view);
        listView.setEmptyView(emptyView);


        searchButton = (Button) findViewById(R.id.button);
        searchText = (EditText) findViewById(R.id.search_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputString = searchText.getText().toString();
                createSearchUrl = createSearchUrl(inputString);
                getLoaderManager().initLoader(BOOK_LOADER_ID, null,MainActivity.this);
            }
        });

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

    // Create search URL
    private String createSearchUrl(String inputString) {
        String[] queryStrings= inputString.split(" ");
        url.append(queryStrings[0]);
        for( int i = 1; i < queryStrings.length; i++){
            url.append("+").append(queryStrings[i]);
        }
        return url.append("&maxResults=10").toString();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, createSearchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        bookAdapter.clear();
        if(books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }
}
