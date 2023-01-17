package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.OrdersMapper;
import com.example.raj_liangjian.common.BaseContext;
import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.entity.*;
import com.example.raj_liangjian.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    UserService userService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    OrderDetailService orderDetailService;

    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前id
        Long userID = BaseContext.getThreadLocal();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userID);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lqw);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            //购物车无数据
            throw new CustomException("购物车无数据");
        }
        long orderId = IdWorker.getId();//生成订单号
        AtomicInteger amount = new AtomicInteger(0);
        //遍历购物车获取金额
        List<OrderDetail> orderDetails =shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            Integer number = item.getNumber();//数量
            BigDecimal price = item.getAmount();//价格
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());




        //查询用户数据
        User user = userService.getById(userID);
        //查询地址数据
        AddressBook address = addressBookService.getById(orders.getAddressBookId());
        if (address == null) {
            throw new CustomException("用户地址信息有误不能下单");
        }

        //向订单插入数据，一条

        //设置属性
        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//金额
        orders.setUserId(userID);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(address.getConsignee());
        orders.setPhone(address.getPhone());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAddress((address.getProvinceName() == null ? "" : address.getProvinceName())
                + (address.getCityName() == null ? "" : address.getCityCode())
                + (address.getDistrictName() == null ? "" : address.getDistrictName())
                + (address.getDetail() == null ? "" : address.getDetail())
        );


        this.save(orders);
        //向明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(lqw);


    }
}
