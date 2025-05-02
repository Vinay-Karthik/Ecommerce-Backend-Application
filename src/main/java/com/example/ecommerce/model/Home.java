package com.example.ecommerce.model;

import lombok.Data;

import java.util.List;

@Data
public class Home {

    private List<HomeCategory> grid;

    private List<HomeCategory> shopByCategory;

    private List<HomeCategory> electricCategory;

    private List<HomeCategory> dealCategory;

    private  List<Deal> deals;
}
