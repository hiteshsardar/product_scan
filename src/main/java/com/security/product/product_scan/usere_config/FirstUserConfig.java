/***********************************************************************
 * Copyright (c) 2024
 * owned by Hitesh Sardar
 ***********************************************************************/

package com.security.product.product_scan.usere_config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.security.product.product_scan.constants.Constants;
import com.security.product.product_scan.entity.Users;
import com.security.product.product_scan.repository.UserRepo;
import com.security.product.product_scan.service.AuthService;
import com.security.product.product_scan.utilities.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.*;


@Component
public class FirstUserConfig implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(FirstUserConfig.class);
    private final UserRepo userRepo;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final String userDirectory = System.getProperty("user.dir") + "/conf/first_user.json";
    public FirstUserConfig(UserRepo userRepo, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        firstUserSetupDone();
    }

    private void firstUserSetupDone(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            Users user = mapper.readValue(new File(userDirectory), Users.class);
            if (!authService.isUserExits(user.getEmail())){
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                Users saveFirstUser = userRepo.save(user);
                if (!ValidationUtils.isEmptyString(saveFirstUser.getId())) {
                    updateFirstUserDetails(saveFirstUser);
                    logger.info("First time user setup has done successfully!! {}", user);
                }
            }
        } catch (IOException ex){
            logger.error("Unable to do the setup of first user. {}", ex.getMessage());
        }
    }

    private void updateFirstUserDetails(Users userDetails) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put(Constants.NAME, userDetails.getName());
        jsonNode.put(Constants.EMAIL, userDetails.getEmail());
        jsonNode.put(Constants.PASSWORD, userDetails.getPassword());
        jsonNode.put(Constants.ROLE, userDetails.getRole());
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userDirectory), jsonNode);
    }
}
