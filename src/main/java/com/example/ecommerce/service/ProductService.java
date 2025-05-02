package com.example.ecommerce.service;

import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest request, Seller seller);

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> searchProduct(String query) throws ProductException;

    public Page<Product> getAllProducts(String category, String brand, String color, String size, Integer minPrice,
                                        Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber);

    List<Product> getProductsBySellerId(Long sellerId);


}
