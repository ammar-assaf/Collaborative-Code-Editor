package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.entities.UserConnectedAccount;
import com.example.backend.enums.Role;
import com.example.backend.dto.PageDTO;
import com.example.backend.entities.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.ConnectedAccountRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectedAccountRepository connectedAccountRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws BadCredentialsException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Cannot find user with email %s".formatted(email)));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<UserConnectedAccount> findByProviderAndProviderId(String provider, String providerId){
        return connectedAccountRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public User createUserFromOauth2User(OAuth2AuthenticationToken authentication) {
        User user = new User(authentication.getPrincipal());
        String provider = authentication.getAuthorizedClientRegistrationId();
        String providerId = authentication.getName();
        UserConnectedAccount connectedAccount = new UserConnectedAccount();
        connectedAccount.setUser(user);
        connectedAccount.setProvider(provider);
        connectedAccount.setProviderId(providerId);
        user.addConnectedAccount(connectedAccount);
        user = userRepository.save(user);
        connectedAccountRepository.save(connectedAccount);
        return user;
    }
    @Transactional
    public User addConnectedAccount(User user,String provider, String providerId)
    {
        UserConnectedAccount newConnectedAccount = new UserConnectedAccount();
        newConnectedAccount.setUser(user);
        newConnectedAccount.setProvider(provider);
        newConnectedAccount.setProviderId(providerId);
        user.addConnectedAccount(newConnectedAccount);
        User existingUser = userRepository.save(user);
        connectedAccountRepository.save(newConnectedAccount);
        return existingUser;
    }

    public void assignDefaultRole(User user) {
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public PageDTO<UserDTO> getUsers(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be zero or greater");
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<User> users = userRepository.findAll(pageable);

        return new PageDTO<>(users.map(userMapper::toDto));
    }

}
