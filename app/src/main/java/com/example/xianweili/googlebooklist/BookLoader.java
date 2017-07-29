package com.example.xianweili.googlebooklist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by xianwei li on 7/28/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("LOADER123", "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (url == null) {
            return null;
        }
        Log.i("LOADER123", "loadInBackground");
        List<Book> books = QueryUtils.fetchBookDate(url);
        return books;
    }
}
