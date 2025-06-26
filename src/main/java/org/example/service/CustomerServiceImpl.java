package org.example.service;


import org.example.common.BaseResponse;
import org.example.constants.ConstantCode;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getCustomers(){
        try{
            List<Customer>  customers = customerRepository.getAllCustomer();
            return customers;
        } catch (Exception e){
            System.out.println("Order failed");
            return null;
        }

    }

    @Override
    public BaseResponse saveCustomer(Customer customer){
        try{
            BaseResponse response = new BaseResponse();
            Customer savedCustomer = customerRepository.saveCustomer(customer);

            if(savedCustomer.getCustomer_id() != null){
                response.setCode(ConstantCode.SUCCESS);
                response.setMessage("customer saved Successfully");
                response.setData(Map.of("customerDetials",savedCustomer));
                return response;
            }

            response.setCode(ConstantCode.FAIL);
            response.setMessage("customer save failed");
            response.setData(Map.of("CustomerDetials",savedCustomer));
            return response;
        } catch (Exception e) {

            System.out.println("customers failed to save");
            return new BaseResponse(
                    ConstantCode.FAIL,
                    "Error saving customer: " + e.getMessage(),
                    null
            );
        }
    }
}
