package com.example.bookstorebackend;

import com.example.bookstorebackend.controller.BookController;
import com.example.bookstorebackend.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;


@SpringBootApplication
public class BookstoreBackendApplication {





	public static void main(String[] args) {
		SpringApplication.run(BookstoreBackendApplication.class, args);
	}




}
