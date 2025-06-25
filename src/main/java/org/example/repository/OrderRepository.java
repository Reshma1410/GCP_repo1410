package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Basic CRUD operations
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setDelete(false);
            entityManager.persist(order);
            return order;
        } else {
            return entityManager.merge(order);
        }
    }

    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }

    public Order deleteById(Long id) {
        Order order = findById(id);
        if (order != null) {
            order.setDelete(true);
            return save(order);
        }else return null;
    }

    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM Order o", Order.class)
                .getResultList();
    }

    // Custom complex queries (add your future methods here)
    public List<Order> findActiveOrders() {
        return entityManager.createQuery(
                        "SELECT o FROM Order o WHERE o.delete = false", Order.class)
                .getResultList();
    }

    public List<Order> findOrdersByCustomer(String customerName) {
        return entityManager.createQuery(
                        "SELECT o FROM Order o WHERE o.customer_name = :name", Order.class)
                .setParameter("name", customerName)
                .getResultList();
    }

    // Example of a complex query with array operation (PostgreSQL specific)
    public List<Order> findOrdersContainingItem(String item) {
        return entityManager.createQuery(
                        "SELECT o FROM Order o WHERE :item = ANY(o.items)", Order.class)
                .setParameter("item", item)
                .getResultList();
    }
}