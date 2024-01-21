package ru.kikopark.backend.configs;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kikopark.backend.service.AuthenticationService;
import ru.kikopark.backend.utils.JwtService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    public JwtRequestFilter(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtService.getUsername(jwt);username = jwtService.getUsername(jwt);
                logger.info("Username extracted from JWT: {}", username);
                List<String> roles = jwtService.getRoles(jwt);
                logger.info("Roles extracted from JWT: {}", roles);
            } catch (ExpiredJwtException e) {
                logger.error("JWT token is expired");
                // Если access token истек, пробуем обновить его с помощью refresh token
                String refreshToken = request.getHeader("Refresh-Token");
                username = jwtService.getUsername(refreshToken);
                if (username != null) {
                    // Если удалось извлечь username из refresh token, генерируем новый access token
                    UserDetails userDetails = null;
                    String newAccessToken = null;
                    try {
                        userDetails = authenticationService.loadUserByUsername(username);
                        newAccessToken = jwtService.generateAccessToken(userDetails);
                        response.setHeader("Authorization", newAccessToken);
                    } catch (Exception exception) {
                        logger.error("Cannot find that user");
                    }
                }
            } catch (Exception e) {
                logger.error("Unexpected exception during token processing", e);
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtService.getRoles(jwt)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(token);
            logger.info("Authentication token set for user: {}", username);
        }
        filterChain.doFilter(request, response);
    }
}
