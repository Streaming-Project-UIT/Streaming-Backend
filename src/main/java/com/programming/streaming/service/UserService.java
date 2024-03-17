package com.programming.streaming.service;

import org.springframework.stereotype.Service;

import com.programming.streaming.repository.UserRepository;
import com.programming.streaming.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;



}