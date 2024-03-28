/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.service;

import com.security.product.product_scan.constants.MessagesConstant;
import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Users;
import com.security.product.product_scan.pojo.RequestResponsePojo;
import com.security.product.product_scan.repository.UserRepo;
import com.security.product.product_scan.utilities.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MongoTemplate mongoTemplate;

    public AuthService(UserRepo userRepo, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MongoTemplate mongoTemplate) {
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mongoTemplate = mongoTemplate;
    }

    public RequestResponsePojo signUp(RequestResponsePojo registrationRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        try {
            if (!isUserExits(registrationRequest.getEmail())) {
                Users user = new Users();
                user.setName(registrationRequest.getName());
                user.setEmail(registrationRequest.getEmail());
                user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                user.setRole(registrationRequest.getRole());
                Users userResult = userRepo.save(user);

                if (!ValidationUtils.isEmptyString(userResult.getEmail())) {
                    response.setUsers(userResult);
                    response.setMessage(MessagesConstant.USER_SAVE_SUCCESS_MESSAGE);
                    response.setStatusCode(200);
                }
            } else {
                response.setStatusCode(400);
                response.setMessage(MessagesConstant.USER_ALREADY_EXISTS_MESSAGE);
            }
        } catch (DuplicateKeyException ex) {
            response.setStatusCode(400);
            response.setMessage(MessagesConstant.DUPLICATE_EMAIL_ERROR);
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
    }

    public RequestResponsePojo signIn(RequestResponsePojo signInRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            Users user = userRepo.findByEmail(signInRequest.getEmail()).orElseThrow();
            logger.info("User is: {}", user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(jwt, user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime(MessagesConstant.TOKEN_EXPIRE_TIME_MESSAGE);
            response.setMessage(MessagesConstant.USER_SIGNIN_SUCCESS_MESSAGE);
        } catch (Exception ex) {
            response.setStatusCode(500);
            response.setError(ex.getMessage());
        }
        return response;
    }

    public RequestResponsePojo refreshToken(RequestResponsePojo refreshTokenRequest) {
        RequestResponsePojo response = new RequestResponsePojo();
        String userEmail = jwtUtils.getUsernameFromToken(refreshTokenRequest.getToken());
        Users users = userRepo.findByEmail(userEmail).orElseThrow();
        if(jwtUtils.isValidToken(refreshTokenRequest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime(MessagesConstant.TOKEN_EXPIRE_TIME_MESSAGE);
            response.setMessage(MessagesConstant.REFRESH_SUCCESS_MESSAGE);
        }
        response.setStatusCode(500);
        return response;
    }

    private boolean isUserExits(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.EMAIL).is(email));
        Users user = mongoTemplate.findOne(query, Users.class);
        if (user == null)
            return false;
        else return !ValidationUtils.isEmptyString(user.getEmail());
    }

}
