package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.OrderDetailMapper;
import com.example.raj_liangjian.entity.OrderDetail;
import com.example.raj_liangjian.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
