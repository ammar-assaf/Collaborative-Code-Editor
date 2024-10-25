package com.example.backend.config;

import com.example.backend.entities.User;
import com.example.backend.entities.UserConnectedAccount;
import com.example.backend.enums.Role;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = authenticationToken.getAuthorizedClientRegistrationId();
        String providerId = authentication.getName();
        String email = authenticationToken.getPrincipal().getAttribute("email");

        Optional<UserConnectedAccount> connectedAccount = userService.findByProviderAndProviderId(provider, providerId);
        if (connectedAccount.isPresent()) {
            authenticateUser(connectedAccount.get().getUser(), response);
            return;
        }

        User existingUser = userService.findByEmail(email).orElse(null);
        if (existingUser != null) {
            existingUser = userService.addConnectedAccount(existingUser, provider, providerId);
            authenticateUser(existingUser, response);
        } else {
            User newUser = userService.createUserFromOauth2User(authenticationToken);
            authenticateUser(newUser, response);
        }
    }

    private void authenticateUser(User user, HttpServletResponse response) throws IOException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        switch (user.getRole()) {
            case USER -> response.sendRedirect("http://localhost:3000/home");
            case ADMIN -> response.sendRedirect("http://localhost:3000/admin");
            default -> throw new UnsupportedOperationException("You are not authorized ");
        }
    }
}
