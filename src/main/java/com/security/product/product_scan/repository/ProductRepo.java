package com.security.product.product_scan.repository;

import com.security.product.product_scan.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product, Integer> {
}
