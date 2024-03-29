/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.constants;

public class MessagesConstant {
    private MessagesConstant(){}
    public static final String OPERATION_TYPE_NOT_FOUND_MESSAGE = "Operation type not found for the given token.";
    public static final String DUPLICATE_EMAIL_ERROR = "Email address is already registered with our system.";

    // Token Messages
    public static final String TOKEN_EXPIRE_TIME_MESSAGE = "30 min";
    public static final String REFRESH_SUCCESS_MESSAGE = "Successfully Refreshed the token.";
    public static final String INVALID_TOKEN = "Unauthorised user, please provide a valid token";

    //    User Messages
    public static final String INVALID_USER = "User is not valid to perform this request.";
    public static final String USER_SAVE_SUCCESS_MESSAGE = "User Details saved successfully.";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists by the provided email address.";
    public static final String USER_SIGNIN_SUCCESS_MESSAGE = "Successfully signed In.";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found for the given token.";
    public static final String USER_NOT_PERMITTED = "User has no valid permission to perform this request.";


    //    Product Messages
    public static final String PRODUCT_ID_NULL = "Product Id should not be null or empty and unique.";
    public static final String PRODUCT_NAME_NULL = "Product name should not be null or empty.";
    public static final String PRODUCT_BRAND_NULL = "Product brand details should not be null or empty.";
    public static final String PRODUCT_SIZE_NULL = "Product size details should not be null or empty.";
    public static final String PRODUCT_PRICE_NULL = "Product price should not be null or empty.";
    public static final String PRODUCT_QUANTITY_NULL = "Product quantity should not be null or empty.";
    public static final String PRODUCT_DESCRIPTION_NULL = "Product description should not be null or empty.";
    public static final String PRODUCT_DETAILS_SAVE_SUCCESS_MESSAGE = "Product Details saved successfully.";
    public static final String PRODUCT_DETAILS_SAVE_ERROR_MESSAGE = "Unable to save the product details.";
    public static final String PRODUCT_ID_GENERATE_SUCCESS_MESSAGE = "Token to delete the product has generated successfully.";
    public static final String PRODUCT_DELETED_SUCCESS_MESSAGE = "Product is deleted successfully.";
    public static final String PRODUCT_ID_OR_OP_TYPE_NOT_FOUND_MESSAGE = "Operation type or product Id Mismatch";
    public static final String PRODUCT_ID_DUPLICATE_ERROR = "Provided product id is already exists with our system.";
    public static final String PRODUCT_ID_NOT_FOUND_MESSAGE = "ProductID not found for the given token.";
}
