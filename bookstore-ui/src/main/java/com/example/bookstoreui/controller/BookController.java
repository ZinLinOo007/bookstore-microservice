package com.example.bookstoreui.controller;


import com.example.bookstoreui.ds.AccountInfo;
import com.example.bookstoreui.ds.Book;
import com.example.bookstoreui.ds.Cart;
import com.example.bookstoreui.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Set;


@Controller
@RequestMapping("/bookstore")
public class BookController {



    @Autowired
    private BookService bookService;

    private RestTemplate template = new RestTemplate();

    @Value("${payment.url}")
    private String paymentUrl;


    @Autowired
    private Cart cart;





    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return "success";
    }

    @GetMapping({"/","/home","/index"})
    public String index(Model model){
        model.addAttribute("books",bookService.findAllBooks());
        return "home";
    }

    @GetMapping("/book/{id}")
    public String detailBook(@PathVariable("id") int id, Model model){
        model.addAttribute("book",bookService.findBookById(id));
        return "book-details";
    }

    @ModelAttribute("cartSize")
    public int cartSize(){
        return  cart.cartSize();
    }

    @ModelAttribute("books")
    public Set<Book> getAllBooksFromCart(){
        return cart.getBookSet();
    }

    @GetMapping("/book/cart/{id}")
    public String addToCart(@PathVariable("id") int id){
        cart.addToCart(bookService.findBookById(id));
        return "redirect:/bookstore/book/" + id;
    }

    @GetMapping("/book/cart/view")
    public String viewCart(@ModelAttribute Book book){
        //model.addAttribute("books",cart.getBookSet());
        //model.addAttribute("book",new Book());

        return "card-view";
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBookFromCart(@PathVariable("id") int id){
        cart.removeBookFromCart(bookService.findBookById(id));
        return "redirect:/bookstore/book/cart/view";
    }

    @GetMapping("/book/cart/clear")
    public String clearCart(){
        cart.clearCart();
        return "redirect:/bookstore/book/cart/view" ;
    }

    @PostMapping("/book/checkout")
    public String handleCheckout(@ModelAttribute Book book){
         Set<Book> bookSet = cart.getBookSet();
         int i = 0;
         for (Book book1 : bookSet){
             book1.setItemCount(book.getQuantity().get(i));
             i++;
         }
         cart.setBookSet(bookSet);
         //cart.getBookSet().forEach(System.out::println);



//        ResponseEntity responseEntity=
//            template.postForEntity(paymentUrl + "payment/checkout/1",String.valueOf(totalPrice()),String.class);
//
//        System.out.println("========================="+ responseEntity.getStatusCode());
        return  "redirect:/bookstore/account-info";
    }

    @GetMapping("/account-info")
    public String paymentInfoForm(Model model){
        model.addAttribute("accountInfo",new AccountInfo());
        return "accountInfoForm";

    }

    @PostMapping("/account-info")
    public String processAccountInfo(AccountInfo accountInfo, BindingResult result){

        if (result.hasErrors()){
            return "accountInfoForm";
        }

        AccountInfo account = new AccountInfo();
        account.setAccountNumber(accountInfo.getAccountNumber());
        account.setName(accountInfo.getName());
        account.setTotalAmount(totalPrice());

        ResponseEntity response =
                template.postForEntity(paymentUrl + "payment/checkout",account, String.class);


        System.out.println("============================" + response.getStatusCode());

        return "redirect:/";

    }

    private double totalPrice() {
        double totalPrice = cart.getBookSet()
                        .stream()
                                .map(b-> b.getPrice()* b.getItemCount())
                                        .mapToDouble(d -> d)
                                                .sum();
        return totalPrice;
    }


}
