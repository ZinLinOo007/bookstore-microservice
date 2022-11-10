package com.example.payment.controller;

import com.example.payment.dao.AccountDao;
import com.example.payment.ds.Account;
import com.example.payment.ds.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping("/payment")
public class AccountController {

    @Autowired
    private AccountDao accountDao;
    // curl -v -X POST -H "Content-Type: application/json" -d  '{"name":"Zin","totalAmount":25.5,"accountNumber":"Zin18939"}' http://localhost:9000/payment/checkout

    @Transactional
    @PostMapping(value = "/checkout",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkout( @RequestBody AccountInfo accountInfo){

            Optional<Account> accountOptional = accountDao.findAccountByNameAndAccountNumber(accountInfo.getName(),
                    accountInfo.getAccountNumber());
           if (!accountOptional.isPresent()){
               return ResponseEntity.notFound().build();
           }

            Account ownerAccount = accountDao.findById(7).get();
            Account userAccount = accountOptional.get();

            if (accountInfo.getTotalAmount() > userAccount.getAmount()){
                return  ResponseEntity
                        .badRequest()
                        .body("Insufficient Amount!");

            }

            else {
                transferAmount(accountInfo.getTotalAmount(), ownerAccount, userAccount);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .build();
            }

    }

    private void transferAmount(Double totalAmount, Account ownerAccount, Account userAccount) {
        userAccount.setAmount(userAccount.getAmount()- (totalAmount));
        ownerAccount.setAmount(ownerAccount.getAmount() + (totalAmount));
    }

    public double toDouble(String amount){
        return Double.parseDouble(amount);
    }
    //curl -X POST -H "Content-Type:application/json" -d '{"name":"Zin Lin Oo","amount":1000,"accountType":"SAVING"}' localhost:9000/payment/register
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestBody Account account){
        account.setAccountNumber(generateId(account.getName()));
        return ResponseEntity.ok(accountDao.save(account));
    }

    public String generateId(String name){
        Random random = new Random();

        String n1 = "";

        if (name.contains("")){
            String[] names = name.split("");

            for (String s: names){
                n1 += s;
            }
        }

        else n1 = name;


        return name +( random.nextInt(10000)+ 10000);
    }

}
