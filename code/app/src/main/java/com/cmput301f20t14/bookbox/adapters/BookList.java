package com.cmput301f20t14.bookbox.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cmput301f20t14.bookbox.R;
import com.cmput301f20t14.bookbox.entities.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This is the adapter used to represent a book in
 * a listview with each row having the layout described in
 * owned_book_content.xml
 * @author Olivier Vadiavaloo
 * @author Alex Mazzuca
 * @version 2020.11.22
 */
public class BookList extends ArrayAdapter<Book> {
    private ArrayList<Book> books;
    private Context context;
    private StorageReference storageReference;
    private Boolean showStatus;

    private static class ViewHolder {
        TextView owner;
        TextView author;
        TextView title;
        TextView isbn;
        TextView status;
        ImageView bookImageView;
    }

    public BookList(Context context, ArrayList<Book> books, Boolean showStatus) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
        this.showStatus = showStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        storageReference = FirebaseStorage.getInstance().getReference();

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.owned_book_content, parent, false);
            holder.owner = (TextView) view.findViewById(R.id.list_content_owner);
            holder.author = (TextView) view.findViewById(R.id.list_content_author);
            holder.title = (TextView) view.findViewById(R.id.list_content_title);
            holder.isbn = (TextView) view.findViewById(R.id.list_content_isbn);
            holder.status = (TextView) view.findViewById(R.id.list_content_status);
            holder.bookImageView = (ImageView) view.findViewById(R.id.list_content_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Book book = books.get(position);

        holder.owner.setText(book.getOwner());
        holder.author.setText(book.getAuthor());
        holder.title.setText(book.getTitle());
        holder.isbn.setText(book.getIsbn());

        CharSequence statusText = book.getStatusString();

        if (book.getStatus() == Book.BORROWED && !book.getLentTo().isEmpty()) {
            statusText = statusText + " (" + book.getLentTo() + ")";
        }

        holder.status.setText(statusText);

        if (!showStatus) {
            holder.status.setVisibility(View.GONE);
        }

        downloadImage(holder.bookImageView, book);
        return view;
    }

    /**
     * This will grab the image from Firebase and put it into the Image View
     * @author Alex Mazzuca
     * @version 2020.11.22
     */
    public void downloadImage(final ImageView imageView, Book book) {
        String imageUrl = book.getPhotoUrl();


        if (imageUrl != "") {

            Uri uri = Uri.parse(imageUrl);

            Picasso.get().load(uri).into(imageView);

        } else {
            Picasso.get().cancelRequest(imageView);
            imageView.setImageResource(R.drawable.ic_custom_image);
        }

    }
}
