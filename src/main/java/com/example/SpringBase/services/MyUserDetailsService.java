package com.example.SpringBase.services;

import com.example.SpringBase.models.User;
import com.example.SpringBase.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> userOptional = userRepository.findByEmail(email);

        userOptional.orElseThrow(() -> {
            throw new UsernameNotFoundException("Not found " + email);
        });

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles()
        );
    }
}
