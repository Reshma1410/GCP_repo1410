package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.common.BaseResponse;
import org.example.model.Customer;
import org.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public List<Customer> getCustomer(){
        return customerService.getCustomers();
    }

    @PostMapping("/PostCustomer")
    public BaseResponse saveCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

}
