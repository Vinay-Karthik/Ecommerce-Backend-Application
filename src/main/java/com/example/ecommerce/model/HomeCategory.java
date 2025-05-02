package com.example.ecommerce.model;
import com.example.ecommerce.domain.HomeCategorySection;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class HomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String image;

    private String categoryId;

    private HomeCategorySection section;
}
