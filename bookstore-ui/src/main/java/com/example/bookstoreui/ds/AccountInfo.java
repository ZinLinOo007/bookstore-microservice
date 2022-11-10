package com.example.bookstoreui.ds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountInfo {

    public AccountInfo() {
    }

    private double totalAmount;
    private String accountNumber;
    private String name;
}
