package com.example.bookstorebackend.controller;

import com.example.bookstorebackend.dao.BookDao;
import com.example.bookstorebackend.ds.Book;
import com.example.bookstorebackend.ds.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookDao bookDao;


    @GetMapping("/creation")
    @Transactional
    public void init(){

        Book book1 = new Book("A pale view of Hill","Ishiguro",32.5,"Moron",
                LocalDate.of(1978,2,12),"Novel","https://source.unsplash.com/random/400x300/?book");

        Book book2 = new Book("Under the GreenWood Tree","Thomas Hardy",22.5,"Sun"
                ,LocalDate.of(1798,3,22),"Novel","https://source.unsplash.com/random/400x300/?nature");

        Book book3 = new Book("Sons and Lovers","DH Lawrence",22.5,"Sun",
                LocalDate.of(1964,3,4),"Novel","https://source.unsplash.com/random/400x300/?girl");

        Book book4 = new Book("Women in Love","DH Lawrence",23.5,"GreenBean",
                LocalDate.of(1970,3,23),"Novel","https://source.unsplash.com/random/400x300/?women");

        Book book5 = new Book("A Tale of Two Cities","Charles Dickens",22.4,"GreenBean",
                LocalDate.of(1690,3,22),"Novel","https://source.unsplash.com/random/400x300/?desert");

        bookDao.save(book1);
        bookDao.save(book2);
        bookDao.save(book3);
        bookDao.save(book4);
        bookDao.save(book5);
    }



    @GetMapping("/books")
    public Books listAllBooks(){
        return new Books(bookDao.findAll());
    }

    public record BookDto(int id,
                          String title,
                          String authorName,
                          String publisher,
                          LocalDate yearPublished,
                          String genre,
                          String imageUrl){

    }


    @GetMapping("/books/book/{id}")
    public ResponseEntity findBookById(@PathVariable("id") int id){
        Optional<Book> bookOptional = bookDao.findById(id);

        if (bookOptional.isPresent()){
            Book book = bookOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            new BookDto(book.getId(),
                                    book.getTitle(),
                                    book.getAuthorName(),
                                    book.getPublisher(),
                                    book.getYearPublished(),
                                    book.getGenre(),
                                    book.getImageUrl())
                    );

        }else {
            throw new EntityNotFoundException(id + "Not Found");
        }
    }

}
