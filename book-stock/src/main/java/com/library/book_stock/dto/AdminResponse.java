package com.library.book_stock.dto;

import com.library.book_stock.model.Admin;

public class AdminResponse {
    private String message;
    private Admin admin;

    public AdminResponse(String message, Admin admin) {
        this.message = message;
        this.admin = admin;
    }

    public String getMessage() {
        return message;
    }

    public Admin getAdmin() {
        return admin;
    }
}
