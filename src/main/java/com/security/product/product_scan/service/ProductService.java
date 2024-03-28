/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.service;

import com.security.product.product_scan.constants.MessagesConstant;
import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Product;
import com.security.product.product_scan.entity.Users;
import com.security.product.product_scan.pojo.JwtAuthTokenPojo;
import com.security.product.product_scan.pojo.RequestResponsePojo;
import com.security.product.product_scan.repository.ProductRepo;
import com.security.product.product_scan.repository.UserRepo;
import com.security.product.product_scan.utilities.TokenUtils;
import com.security.product.product_scan.utilities.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final UserRepo userRepo;
    private final TokenUtils tokenUtils;
    private final JwtAuthTokenPojo authToken;
    private final ProductRepo productRepo;
    private final JwtUtils jwtUtils;
    public ProductService(TokenUtils tokenUtils, JwtAuthTokenPojo authToken, ProductRepo productRepo, JwtUtils jwtUtils, UserRepo userRepo){
        this.tokenUtils = tokenUtils;
        this.authToken = authToken;
        this.productRepo = productRepo;
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    public RequestResponsePojo saveProduct(RequestResponsePojo productRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        List<String> validateRequest = validateProductDetails(productRequest);
        if (validateRequest.get(0).equals(Constants.FAILED)) {
            response.setStatusCode(400);
            response.setError(validateRequest.get(1));
            return response;
        }
        if (tokenUtils.isAdminUserRole(authToken.getAuthToken())) {
            try {
                Product productToSave = getProduct(productRequest);
                Product productResult = productRepo.save(productToSave);

                if (!ValidationUtils.isEmptyString(productResult.getId())) {
                    response.setProducts(List.of(productResult));
                    response.setMessage(MessagesConstant.PRODUCT_DETAILS_SAVE_SUCCESS_MESSAGE);
                    response.setStatusCode(200);
                } else {
                    response.setStatusCode(400);
                    response.setError(MessagesConstant.PRODUCT_DETAILS_SAVE_ERROR_MESSAGE);
                }
            } catch (DuplicateKeyException ex) {
                response.setStatusCode(400);
                response.setMessage(MessagesConstant.PRODUCT_ID_DUPLICATE_ERROR);
            }
        } else {
            response.setStatusCode(400);
            response.setError(MessagesConstant.USER_NOT_PERMITTED);
        }
        return response;
    }

    public RequestResponsePojo deleteProductRequest(RequestResponsePojo productRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        if (tokenUtils.isAdminUserRole(authToken.getAuthToken())) {
            Users userDetails = getUserDetails(authToken.getAuthToken());
            if (userDetails != null) {
                response.setToken(tokenUtils.getOperationToken(userDetails, productRequest.getId(), Constants.DELETE));
                response.setMessage(MessagesConstant.PRODUCT_ID_GENERATE_SUCCESS_MESSAGE);
                response.setStatusCode(200);
            } else {
                response.setStatusCode(400);
                response.setError(MessagesConstant.USER_NOT_FOUND_MESSAGE);
            }
        } else {
            response.setStatusCode(400);
            response.setError(MessagesConstant.USER_NOT_PERMITTED);
        }
        return response;
    }

    public RequestResponsePojo deleteProduct(RequestResponsePojo productRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        String token = authToken.getAuthToken();
        if (tokenUtils.isAdminUserRole(token)) {
            String productId = tokenUtils.getIdFromToken(token);
            String operationType = tokenUtils.getOperationTypeFroToken(token);
            if (!ValidationUtils.isEmptyString(productId) && !ValidationUtils.isEmptyString(operationType) && productId.equals(productRequest.getId()) && operationType.equals(Constants.DELETE)) {
                Product productToDelete = new Product();
                productToDelete.setId(productId);
                productRepo.delete(productToDelete);

                response.setMessage(MessagesConstant.PRODUCT_DELETED_SUCCESS_MESSAGE + productId);
                response.setStatusCode(200);
            } else {
                response.setStatusCode(400);
                response.setError(MessagesConstant.PRODUCT_ID_OR_OP_TYPE_NOT_FOUND_MESSAGE);
            }
        } else {
            response.setStatusCode(400);
            response.setError(MessagesConstant.USER_NOT_PERMITTED);
        }
        return response;
    }

    private Users getUserDetails(String token){
        final String userEmail = jwtUtils.getUsernameFromToken(token);
        if (!ValidationUtils.isEmptyString(userEmail)) {
            return userRepo.findByEmail(userEmail).orElseThrow();
        } else {
            logger.error("Unable to find user from the given token.");
        }
        return null;
    }

    private Product getProduct(RequestResponsePojo productRequest) {
        Product productToSave = new Product();
        productToSave.setProductId(productRequest.getProductId());
        productToSave.setProductName(productRequest.getProductName());
        productToSave.setProductBrand(productRequest.getProductBrand());
        productToSave.setProductSize(productRequest.getProductSize());
        productToSave.setProductPrice(productRequest.getProductPrice());
        productToSave.setProductQuantity(productRequest.getProductQuantity());
        productToSave.setProductDescription(productRequest.getProductDescription());
        return productToSave;
    }

    private List<String> validateProductDetails(RequestResponsePojo productRequest) {
        List<String> output = new ArrayList<>();
        if (ValidationUtils.isEmptyString(productRequest.getProductId())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_ID_NULL);
        } else if (ValidationUtils.isEmptyString(productRequest.getProductName())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_NAME_NULL);
        } else if (ValidationUtils.isEmptyString(productRequest.getProductBrand())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_BRAND_NULL);
        } else if (ValidationUtils.isEmptyString(productRequest.getProductSize())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_SIZE_NULL);
        } else if (!ValidationUtils.validatePrice(productRequest.getProductPrice())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_PRICE_NULL);
        } else if (!ValidationUtils.validateQuantity(productRequest.getProductQuantity())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_QUANTITY_NULL);
        } else if (ValidationUtils.isEmptyString(productRequest.getProductDescription())) {
            output.add(Constants.FAILED);
            output.add(MessagesConstant.PRODUCT_DESCRIPTION_NULL);
        } else {
            output.add(Constants.SUCCESS);
        }
        return output;
    }
}
