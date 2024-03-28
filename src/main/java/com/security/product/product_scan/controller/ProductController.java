package com.security.product.product_scan.controller;

import com.security.product.product_scan.pojo.RequestResponsePojo;
import com.security.product.product_scan.repository.ProductRepo;
import com.security.product.product_scan.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductRepo productRepo;
    private final ProductService productService;

    public ProductController(ProductRepo productRepo, ProductService productService) {
        this.productRepo = productRepo;
        this.productService = productService;
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productRepo.findAll());
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

    @GetMapping("/user/alone")
    public ResponseEntity<Object> userAlone() {
        return ResponseEntity.ok("User alone can access this API only");
    }

    @GetMapping("/admin/both")
    public ResponseEntity<Object> bothAdminAndUserAPI() {
        return ResponseEntity.ok("Both Admin and User alone can access this API only");
    }
}
