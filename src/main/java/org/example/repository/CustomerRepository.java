package org.example.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.common.BaseResponse;
import org.example.model.Customer;
import org.example.model.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Customer> getAllCustomer(){
        return entityManager.createQuery("SELECT o FROM Customer o", Customer.class)
                .getResultList();
    }

    public Customer  saveCustomer(Customer  customer){
        if(customer.getCustomer_id() == null){
            entityManager.persist(customer);
            return customer;
        } else {
            return entityManager.merge(customer);
        }

    }
}
