package com.security.product.product_scan.repository;

import com.security.product.product_scan.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public interface UserRepo  extends MongoRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}
