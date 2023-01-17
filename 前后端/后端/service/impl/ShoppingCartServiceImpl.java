package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.ShoppingCartMapper;
import com.example.raj_liangjian.entity.ShoppingCart;
import com.example.raj_liangjian.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
