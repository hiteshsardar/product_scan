/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.controller;

import com.security.product.product_scan.pojo.RequestResponsePojo;
import com.security.product.product_scan.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save_product")
    public ResponseEntity<Object> saveProduct(@RequestBody RequestResponsePojo productRequest) {
        RequestResponsePojo responsePojo = productService.saveProduct(productRequest);
        if(responsePojo.getStatusCode() == 200) {
            return ResponseEntity.ok(responsePojo);
        } else {
            return ResponseEntity.status(responsePojo.getStatusCode()).body(responsePojo);
        }
    }

    @PostMapping("/delete_product_request")
    public ResponseEntity<Object> deleteProductRequest(@RequestBody RequestResponsePojo productRequest) {
        RequestResponsePojo responsePojo = productService.deleteProductRequest(productRequest);
        if(responsePojo.getStatusCode() == 200) {
            return ResponseEntity.ok(responsePojo);
        } else {
            return ResponseEntity.status(responsePojo.getStatusCode()).body(responsePojo);
        }
    }

    @PostMapping("/delete_product")
    public ResponseEntity<Object> deleteProduct(@RequestBody RequestResponsePojo productRequest) {
        RequestResponsePojo responsePojo = productService.deleteProduct(productRequest);
        if(responsePojo.getStatusCode() == 200) {
            return ResponseEntity.ok(responsePojo);
        } else {
            return ResponseEntity.status(responsePojo.getStatusCode()).body(responsePojo);
        }
    }
}
