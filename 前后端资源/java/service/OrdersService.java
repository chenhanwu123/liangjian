package com.example.raj_liangjian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.raj_liangjian.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
