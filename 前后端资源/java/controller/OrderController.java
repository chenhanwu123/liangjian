package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.dto.OrdersDto;
import com.example.raj_liangjian.entity.OrderDetail;
import com.example.raj_liangjian.entity.Orders;
import com.example.raj_liangjian.entity.User;
import com.example.raj_liangjian.service.OrderDetailService;
import com.example.raj_liangjian.service.OrdersService;
import com.example.raj_liangjian.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("已下单");
    }

    /**
     * 分页条件查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,Long number, String beginTime,String endTime){
        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(number!=null, Orders::getNumber,number);
        lqw.gt(beginTime!=null, Orders::getOrderTime,beginTime);
        lqw.lt(endTime!=null, Orders::getOrderTime,endTime);
        Page<Orders> page2 = ordersService.page(page1,lqw);

        return R.success(page2);
    }

    /**
     * 修改订单状态
     * @return
     */
    @PutMapping
    public R<String> putStatus(@RequestBody  Orders orders){
        boolean b = ordersService.updateById(orders);
        if (b){
            return R.success("修改成功");
        }else {
            return R.error("修改失败");
        }
    }
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        Page<Orders> userPage = new Page<>(page,pageSize);
        Page<OrdersDto> OrdersDtoPage = new Page<>();


        //添加条件按时间
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Orders::getOrderTime);

        //执行查询
        ordersService.page(userPage,lqw);
        //copy
        BeanUtils.copyProperties(userPage, OrdersDtoPage,"records");

        List<Orders> records = userPage.getRecords();
        List<OrdersDto> page1 = records.stream().map((l->{
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(l, ordersDto);
            //获取user
            Long userId = l.getUserId();
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());
            ordersDto.setAddress(l.getAddress());
            ordersDto.setConsignee(l.getConsignee());
            Long OrderId = l.getId();
            //查询明晰
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, OrderId);
            List<OrderDetail> list = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        })).collect(Collectors.toList());

        OrdersDtoPage.setRecords(page1);
        return R.success(OrdersDtoPage);
    }

    @PostMapping("again")
    public R<String> again(Long id){
        return R.success("ok");
    }
}
