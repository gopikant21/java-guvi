package org.example.jwtdemo.service;

import jakarta.annotation.PostConstruct;
import org.example.jwtdemo.model.JpaUser;
import org.example.jwtdemo.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private JpaUserRepository jpaUserRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JpaUser jpaUser = jpaUserRepo.findByUsername(username);
        return User.builder()
                .username(jpaUser.getUsername())
                .password(jpaUser.getPassword())
                .roles(jpaUser.getRole())
                .build();
    }

    @PostConstruct
    public void init() {
        JpaUser jpaUser =new JpaUser();
        jpaUser.setUsername("admin");
        jpaUser.setPassword(passwordEncoder.encode("admin"));
        jpaUser.setRole("ADMIN");
        JpaUser jpaUser2 =new JpaUser();
        jpaUser2.setUsername("user");
        jpaUser2.setPassword(passwordEncoder.encode("user"));
        jpaUser2.setRole("USER");
        jpaUserRepo.save(jpaUser);
        jpaUserRepo.save(jpaUser2);
    }
}
