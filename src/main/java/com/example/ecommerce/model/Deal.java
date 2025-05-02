package com.example.ecommerce.model;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer discount;
    @OneToOne
    private HomeCategory category;
}
