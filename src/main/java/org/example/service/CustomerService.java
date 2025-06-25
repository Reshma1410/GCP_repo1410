package org.example.service;
import java.util.List;


import org.example.common.BaseResponse;
import org.example.model.Customer;

public interface CustomerService {

    List<Customer> getCustomers();
    BaseResponse   saveCustomer(Customer customer);
}
