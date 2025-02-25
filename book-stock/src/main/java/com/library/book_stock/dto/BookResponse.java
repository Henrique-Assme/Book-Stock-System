package com.library.book_stock.dto;

import com.library.book_stock.model.Book;

public class BookResponse {
    private String message;
    private Book book;

    public BookResponse(String message, Book book) {
        this.message = message;
        this.book = book;
    }
    
    public String getMessage() {
        return message;
    }

    public Book getBook() {
        return book;
    }
}
