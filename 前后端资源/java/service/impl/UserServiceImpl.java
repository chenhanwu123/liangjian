package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.UserMapper;
import com.example.raj_liangjian.entity.User;
import com.example.raj_liangjian.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
