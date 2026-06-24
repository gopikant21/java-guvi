package org.example.securitydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping
    public String sayHello() {
        return "Hello World";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String helloUser(){
        return "Hello User, you and admin can only search products and place order!!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin(){
        return "Hello Admin, you can add, delete, update products, orders and customers!!";
    }

    @GetMapping("/loan-officer")
    @PreAuthorize("hasRole('LOAN_OFFICER')")
    public String loanOfficer(){
        return "Hi, loan-officer!!, you can process loan application!!";
    }


}
