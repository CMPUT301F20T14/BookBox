package com.cmput301f20t14.bookbox.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cmput301f20t14.bookbox.R;
import com.cmput301f20t14.bookbox.adapters.BookList;
import com.cmput301f20t14.bookbox.entities.Book;
import com.cmput301f20t14.bookbox.entities.Request;
import com.cmput301f20t14.bookbox.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * This fragment is one of the tabs of the tabbed activity: listsActivity
 * It contains a listview of the requests made by the user that haven't been
 * accepted yet.
 * @author Olivier Vadiavaloo
 * @see com.cmput301f20t14.bookbox.activities.ListsActivity
 */

public class OutRequestFragment extends Fragment {
    private BookList listAdapter;
    private ArrayList<Book> books;
    private ListView listView;
    private String username;
    private FirebaseFirestore database;

    public static OutRequestFragment newInstance(String usernameArg) {
        Bundle args = new Bundle();
        args.putString(User.USERNAME, usernameArg);

        OutRequestFragment fragment = new OutRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outrequest, container, false);

        // get username extra
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString(User.USERNAME);
        }

        // initialize database
        database = FirebaseFirestore.getInstance();

        // find listview
        listView = (ListView) view.findViewById(R.id.fragment_listview);

        // initialize adapter and books
        books = new ArrayList<>();
        listAdapter = new BookList(getContext(), books, true);

        setUpList();
        listView.setAdapter(listAdapter);

        return view;
    }

    /**
     * Set up the list of requested books
     */
    public void setUpList() {
        database
                .collection(Request.REQUESTS)
                .whereEqualTo(Request.BORROWER, username)
                .whereEqualTo(Request.IS_ACCEPTED, Boolean.valueOf(false).toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            books.clear();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                getRequestedBook(doc.getData().get(Request.BOOK).toString());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * Get the specified book and add it to the list
     * @param id The firestore id of the book
     */
    public void getRequestedBook(String id) {
        database
                .collection(Book.BOOKS)
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc != null && doc.exists()) {
                                String title = doc.getData().get(Book.TITLE).toString();
                                String author = doc.getData().get(Book.AUTHOR).toString();
                                String isbn = doc.getData().get(Book.ISBN).toString();
                                String owner = doc.getData().get(Book.OWNER).toString();
                                String statusString = doc.getData().get(Book.STATUS).toString();
                                int status = Integer.parseInt(statusString);
                                String lentTo = doc.getData().get(Book.LENT_TO).toString();
                                String imageUrl = doc.getData().get(Book.IMAGE_URL).toString();

                                Book book = new Book(isbn, title, author, owner, status, lentTo, imageUrl);
                                books.add(book);
                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
