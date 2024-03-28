package com.security.product.product_scan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.product.product_scan.pojo.JwtAuthTokenPojo;
import com.security.product.product_scan.service.JwtUtils;
import com.security.product.product_scan.service.UsersDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsersDetailsService usersDetailsService;
    private final JwtAuthTokenPojo authToken;

    public JWTAuthFilter(JwtUtils jwtUtils, UsersDetailsService usersDetailsService, JwtAuthTokenPojo authToken) {
        this.jwtUtils = jwtUtils;
        this.usersDetailsService = usersDetailsService;
        this.authToken = authToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwtToken = authHeader.substring(7);
            final String userEmail = jwtUtils.getUsernameFromToken(jwtToken);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = usersDetailsService.loadUserByUsername(userEmail);
                if(jwtUtils.isValidToken(jwtToken, userDetails)) {
                    authToken.setAuthToken(jwtToken);
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                } else {
                    logger.error("Invalid token.");
                }
            } else {
                logger.error("Invalid auth header. User Details not found");
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("statusCode", 400);
            errorDetails.put("error", ex.getMessage());

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
            logger.error(ex.getMessage());
        }
    }
}
