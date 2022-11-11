package com.example.bookstoreui.ds;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class TransportInfo {

    private String name;
    private String orderId;
    private Set<Book> books;
    private double total;

    public TransportInfo() {
    }

    public TransportInfo(String name, String orderId, Set<Book> books, double total) {
        this.name = name;
        this.orderId = orderId;
        this.books = books;
        this.total = total;
    }
}
