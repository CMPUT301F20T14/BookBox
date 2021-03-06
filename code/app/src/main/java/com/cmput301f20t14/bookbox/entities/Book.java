package com.cmput301f20t14.bookbox.entities;

import java.io.Serializable;

/**
 * A class that contains all the information necessary to represent a book
 * @author Carter Sabadash
 * @author Olivier Vadiavaloo
 * @author Alex Mazzuca
 * @version 2020.11.04
 *
 * All Getters have been implemented, but not setters (will have to also ensure that
 *     data entered is in correct format
 * The behaviour of the getLentTo() must be defined when the book Status is AVAILABLE
 *
 * Move the Status enum to a public file?
 */
public class Book implements Serializable {

    private String isbn;
    private String title;
    private String author;
    private String owner;
    private int status;
    private String lentTo;
    private String photoUrl;


    public static final String ID = "ID";
    public static final String BOOKS = "BOOKS";
    public static final String ISBN = "ISBN";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String STATUS = "STATUS";
    public static final String LENT_TO = "LENT_TO";
    public static final String OWNER = "OWNER";
    public static final String IMAGE_URL = "IMAGE_URL";

    public static final int AVAILABLE = 66;
    public static final int REQUESTED = 67;
    public static final int ACCEPTED = 68;
    public static final int BORROWED = 69;

    /**
     * Constructs a book
     * @param isbn The isbn of the book
     * @param title The title of the book
     * @param author The author of the book
     * @param owner The User who owns the book
     * @param status The Status of the Book (Book.Status)
     * @param lentTo Who the book is lent to (null if no-one)
     * @param photoUrl The image association with the book
     */
    public Book(String isbn, String title, String author, String owner, int status,
                String lentTo, String photoUrl) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = status;
        this.lentTo = lentTo;
        this.photoUrl = photoUrl;
    }


    /**
     * Get the ISBN of the book
     * @return A String of the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Get the Title of the book
     * @return A String of the Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author of the book
     * @return A String of the Author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the owner of the book
     * @return A String of who owns the book
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the Status of the book
     * @return A Status of the book
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets the Status of the book in string format
     * @return A string literal representing the status
     *         the book. It can be one of "Accepted",
     *         "Borrowed", "Requested" or "Available"
     */
    public String getStatusString() {
        switch (this.status) {
            case Book.ACCEPTED:
                return "Accepted";
            case Book.AVAILABLE:
                return "Available";
            case Book.BORROWED:
                return "Borrowed";
            case Book.REQUESTED:
                return "Requested";
            default:
                return "";
        }
    }

    /**
     * static version of the getStatusString method
     * @param status integer constant representing
     *               a status
     * @return A string literal representing the status
     *         the book. It can be one of "Accepted",
     *         "Borrowed", "Requested" or "Available"
     */
    public static String getStatusString(int status) {
        switch (status) {
            case Book.ACCEPTED:
                return "Accepted";
            case Book.AVAILABLE:
                return "Available";
            case Book.BORROWED:
                return "Borrowed";
            case Book.REQUESTED:
                return "Requested";
            default:
                return "";
        }
    }

    /**
     * Gets who currently has the book
     * ** Need to define behaviour when the book is AVAILABLE
     * @return A string identifying who the book is lent to
     */
    public String getLentTo() {
        if (lentTo == null) { return "Not Borrowed"; }
        return lentTo;
    }

    /**
     * Gets the image associated with the book
     * @return An image for the book
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * Gets the image associated with the book
     * @return An image for the book
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the image associated with the book
     * @return An image for the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the author associated with the book
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the image associated with the book
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Sets the image associated with the book
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Sets the image associated with the book
     * @param lentTo
     */
    public void setLentTo(String lentTo) {
        this.lentTo = lentTo;
    }

    /**
     * Sets the image associated with the book
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
