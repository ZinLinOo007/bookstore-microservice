package com.example.transportationapp.controller;

import com.example.transportationapp.dao.BookDao;
import com.example.transportationapp.dao.TransprotInfoDao;
import com.example.transportationapp.ds.Book;
import com.example.transportationapp.ds.BookDto;
import com.example.transportationapp.ds.TransportInfo;
import com.example.transportationapp.ds.TransportInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transport")
public class TransportController {

    @Autowired
    private TransprotInfoDao transprotInfoDao;

    @Autowired
    private BookDao bookDao;



    @Transactional
    @PostMapping(value = "/transport-create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTransportInfo(@RequestBody TransportInfoDto transportInfoDto){

        TransportInfo tp1 = new TransportInfo(transportInfoDto.getName(),
                transportInfoDto.getOrderId(),transportInfoDto.getTotal());

        for(BookDto bookDto : transportInfoDto.getBooks()){
            tp1.addBook(bookDao.save(toEntity(bookDto)));
        }

        transprotInfoDao.save(tp1);


        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    public Book toEntity(BookDto bookDto){
        return  new Book(
                bookDto.getTitle(),
                bookDto.getAuthorName(),
                bookDto.getPrice(),
                bookDto.getPublisher(),
                bookDto.getYearPublished(),
                bookDto.getGenre(),
                bookDto.getImageUrl(),
                bookDto.getItemCount()

        );
    }

}
