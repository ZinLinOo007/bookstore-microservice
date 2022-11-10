package com.example.bookstoreui.service;


import com.example.bookstoreui.ds.Book;
import com.example.bookstoreui.ds.Books;
import com.example.bookstoreui.exception.NotfoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Service
public class BookService {

    private RestTemplate template = new RestTemplate();


    private List<Book> books = new LinkedList<>();

    @PostConstruct
    public void postConstruct(){
        showAllBooks();
    }


    public List<Book> findAllBooks(){
        return books;
    }

    public Book findBookById(int id){
        return books
                .stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(()-> new NotfoundException(id + "Not Found"));
    }



    @Value("${api.url}")
    private String backendUrl;



    public void showAllBooks(){
        this.books = template.getForEntity(backendUrl+"api/books",Books.class)
                .getBody()
                .getBooks();
    }


}
