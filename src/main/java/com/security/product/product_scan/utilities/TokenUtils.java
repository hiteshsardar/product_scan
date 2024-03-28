package com.security.product.product_scan.utilities;

import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Users;
import com.security.product.product_scan.service.JwtUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private final JwtUtils jwtUtils;

    public TokenUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public boolean isAdminUserRole(String token) {
        return jwtUtils.getRoleFromToken(token).equals(Constants.ADMIN);
    }

    public boolean isUserRole(String token) {
        return jwtUtils.getRoleFromToken(token).equals(Constants.USER);
    }

    public String getUserRole(String token) {
        return jwtUtils.getRoleFromToken(token);
    }

    public String getOperationToken(Users user, String id, String operationType) {
        return jwtUtils.generateOperationToken(user, id, operationType);
    }
    public String getIdFromToken(String token) {
        return jwtUtils.getIdFromToken(token);
    }

    public String getOperationTypeFroToken(String token) {
        return jwtUtils.getOperationTypeFroToken(token);
    }

}
