package org.example.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customer_name;
    @Column(name = "order_date", nullable = false)
    private LocalDate order_date;
    @Column(name = "total_amount", nullable = false)
    private Double total_amount;
    @Column(name = "delete")
    private Boolean delete;
    @Column(name = "items", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> items;
}
