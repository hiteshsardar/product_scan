/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.service;

import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 1800000; // half an hour or 1800000 mili-seconds

    public JwtUtils() {
        secretKey = Jwts.SIG.HS512.key().build();
    }

    public String generateToken(Users userDetails) {
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claim(Constants.USER_ROLE, userDetails.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateOperationToken(Users userDetails, String id, String operationType) {
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claim(Constants.ID, id)
                .claim(Constants.USER_ROLE, userDetails.getRole())
                .claim(Constants.OPERATION_TYPE, operationType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String token, Users userDetails) {
        if (isValidToken(token, userDetails)) {
            return Jwts
                    .builder()
                    .subject(userDetails.getUsername())
                    .claim(Constants.USER_ROLE, userDetails.getRole())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(secretKey)
                    .compact();
        }
        else return "";
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return (String) claims.get(Constants.USER_ROLE);
    }

    public String getIdFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return (String) claims.get(Constants.ID);
    }

    public String getOperationTypeFroToken(String token) {
        Claims claims = getClaimFromToken(token);
        return (String) claims.get(Constants.OPERATION_TYPE);
    }

    private Claims getClaimFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        return getClaimFromToken(token)
                .getExpiration()
                .before(new Date());
    }
}
