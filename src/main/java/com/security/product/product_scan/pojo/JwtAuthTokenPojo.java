package com.security.product.product_scan.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtAuthTokenPojo {
    private String authToken;
}
