package com.esquad.esquadbe.user.service;
import com.esquad.esquadbe.user.config.UserDetailsImpl;
import com.esquad.esquadbe.user.entity.User;
import com.esquad.esquadbe.user.repository.UserRepository;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUsername(username);
        if (userData == null) {
            throw new UsernameNotFoundException("아이디가 일치하지 않습니다. " + username);
        }
        return new UserDetailsImpl(userData);
    }
}
