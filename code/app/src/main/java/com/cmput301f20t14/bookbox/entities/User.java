package com.cmput301f20t14.bookbox.entities;

import java.io.Serializable;
import android.util.Patterns;

import java.util.ArrayList;

/**
 * An entity class that encapsulates the user of the app.
 * @author Olivier Vadiavaloo
 * @author Alex Mazzuca
 * @version 2020.11.04
 */

public class User implements Serializable {
    public static final String USERS = "USERS";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";
    public static final String OWNED_BOOKS = "OWNED_BOOKS";
    public static final String REQUESTED_BOOKS = "REQUESTED_BOOKS";
    public static final String BORROWED_BOOKS = "BORROWED_BOOKS";
    public static final String IMAGE_URL = "IMAGE_URL";

    private String username;
    private String password;
    private String phone;
    private String email;
    private ArrayList<Book> ownedBooks;
    private ArrayList<Book> borrowedBooks;
    private String photoUrl;
    //TO ADD LATER:
    // private ArrayList<Request> requests;

    /**
     * Constructor of the User class
     * @param username      username of the user's BookBox account
     * @param password      password of the user's BookBox account
     * @param phone         user's phone number
     * @param email         email associated with the user's BookBox account
     * @param ownedBooks    ArrayList of books owned by user
     * @param borrowedBooks ArrayList of books borrowed by user
     */
    public User(String username, String password, String phone, String email,
                ArrayList<Book> ownedBooks, ArrayList<Book> borrowedBooks, String photoUrl) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.ownedBooks = ownedBooks;
        this.borrowedBooks = borrowedBooks;
        this.photoUrl = photoUrl;
    }

    /**
     * Constructor of the User class, without ownedBooks and borrowedBooks
     * @param username      username of the user's BookBox account
     * @param password      password of the user's BookBox account
     * @param phone         user's phone number
     * @param email         email associated with the user's BookBox account
     */
    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }


    /**
     * This method is called when there is a need to update the username, password
     * and phone number of the user. It is then unnecessary to call the setters
     * individually.
     * @author Olivier Vadiavaloo
     * @version 2020.10.27
     * @param username username for the user's BookBox account
     * @param password password for the user's BookBox account
     * @param phone    the phone number of the user
     */
    public void updateContactInfo(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    /**
     * This overloaded method is called when there is a need to update the username, password,
     * phone number AND the email of the user. It is then unnecessary to call the setters
     * individually.
     * @author Olivier Vadiavaloo
     * @version 2020.10.27
     * @param username username for the user's BookBox account
     * @param password password for the user's BookBox account
     * @param phone    the phone number of the user
     */
    public void updateContactInfo(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Checks if email has valid syntax
     * @param email user's registered email
     */
    public static boolean isEmailSyntaxValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Checks if phone number has valid syntax
     * @param phone user's registered phone
     */
    public static boolean  isPhoneSyntaxValid(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * Gets the username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     * @param username username for user's BookBox account
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * @param password password for user's BookBox account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number
     * @param phone phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * @param email email string
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the array list of books owned by the user
     * @return ownedBooks
     */
    public ArrayList<Book> getOwnedBooks() {
        return ownedBooks;
    }

    /**
     * Sets the arrya list of books owned by the user
     * @param ownedBooks ArrayList of owned books
     */
    public void setOwnedBooks(ArrayList<Book> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    /**
     * Returns the books borrowed BY the user
     * @return borrowedBooks
     */
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     * Sets the books borrowed BY the user
     * @param borrowedBooks ArrayList of borrowed books
     */
    public void setBorrowedBooks(ArrayList<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    /**
     * Sets the image associated with the user
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Gets the image associated with the book
     * @return An image for the book
     */
    public String getPhotoUrl() {
        return photoUrl;
    }
}
