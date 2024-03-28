/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    @Indexed(unique = true)
    private String productId;
    private String productName;
    private String productBrand;
    private String productSize;
    private Double productPrice;
    private Integer productQuantity;
    private String productDescription;
}
