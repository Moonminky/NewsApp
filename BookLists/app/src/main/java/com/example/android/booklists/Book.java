package com.example.android.booklists;

/**
 * Class for storing book information
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;

    /**
     * Constructs a new Book with initial values for title, author, average rating and published date
     *
     * @param title         is the title of the book
     * @param author        is the author of the book
     * @param publishedDate is the published date of the book
     */
    public Book(String title, String author, String publishedDate) {
        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
    }


    // getter-methods

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }


    public String getmPublishedDate() {
        return mPublishedDate;
    }


}

