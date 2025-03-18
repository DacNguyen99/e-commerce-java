package com.dacnguyen.ecommerce.security.user;

import com.dacnguyen.ecommerce.entity.User;
import com.dacnguyen.ecommerce.exception.NotFoundException;
import com.dacnguyen.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email not found!"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
