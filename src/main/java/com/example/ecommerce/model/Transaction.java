package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User customer;
    @OneToOne
    private Order order;
    @ManyToOne
    private Seller seller;

    private LocalDateTime date = LocalDateTime.now();


}
