package com.example.studentmanagement.security;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userByEmail = userRepository.findUserByEmail(username);
        if (userByEmail.isEmpty()) {
            throw new UsernameNotFoundException("User By This Email, Doest Exist");
        }
        return new SpringUser(userByEmail.get());
    }
}
