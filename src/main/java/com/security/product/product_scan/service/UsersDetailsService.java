/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Users;
import com.security.product.product_scan.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public UsersDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow();
    }

    public List<ObjectNode> findAllUsers() {
        List<Users> users = userRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> jsonNode = new ArrayList<>();
        for (Users user: users) {
            ObjectNode node = mapper.createObjectNode();
            node.put(Constants.ID, user.getId());
            node.put(Constants.NAME, user.getName());
            node.put(Constants.EMAIL, user.getEmail());
            node.put(Constants.ROLE, user.getRole());
            jsonNode.add(node);
        }
        return jsonNode;
    }
}
