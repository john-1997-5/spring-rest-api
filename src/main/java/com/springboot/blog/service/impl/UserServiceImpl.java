package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.SignUpException;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    @Override
    public void registerUser(Long roleId, SignUpDto signUpDto) {
        boolean usernameExists = userRepository.existsByUsername(signUpDto.getUsername());
        boolean emailExists = userRepository.existsByEmail(signUpDto.getEmail());

        if (usernameExists) {
            throw new SignUpException(HttpStatus.BAD_REQUEST, "Username already taken!");
        }

        if (emailExists) {
            throw new SignUpException(HttpStatus.BAD_REQUEST, "Email already taken!");
        }

        // plain text pass to encoded pass
        String encodedPwd = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(encodedPwd);
        // dto to entity mapping
        User userToRegister = mapper.map(signUpDto, User.class);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("Role not found with id: " + roleId));
        userToRegister.setRoles(Collections.singleton(role));

        userRepository.save(userToRegister);
    }
}
