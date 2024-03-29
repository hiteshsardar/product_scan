/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.controller;

import com.security.product.product_scan.service.ProductService;
import com.security.product.product_scan.service.UsersDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicAPIController {
    private final ProductService productService;
    private final UsersDetailsService usersDetailsService;

    public PublicAPIController(ProductService productService, UsersDetailsService usersDetailsService) {
        this.productService = productService;
        this.usersDetailsService = usersDetailsService;
    }

    @GetMapping("/product/get-products")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/user/get-users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(usersDetailsService.findAllUsers());
    }
}
