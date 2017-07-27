package com.example.xianweili.googlebooklist;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xianwei li on 7/27/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter (MainActivity mainActivity, ArrayList<Book> books) {
        super(mainActivity, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booklist_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView bookImageView = (ImageView) convertView.findViewById(R.id.book_image);
        Bitmap bitmap = currentBook.getImage();
        bookImageView.setImageBitmap(bitmap);

        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        String title = currentBook.getTitle();
        titleView.setText(title);

        TextView authorView = (TextView) convertView.findViewById(R.id.author_view);
        String author = currentBook.getAuthor();
        authorView.setText(author);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.book_description);
        String description = currentBook.getDescription();
        descriptionView.setText(description);

        return convertView;
    }

}
