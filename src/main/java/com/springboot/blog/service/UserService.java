package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.SignUpDto;

public interface UserService {
    public void registerUser(Long roleId, SignUpDto signUpDto);
}
