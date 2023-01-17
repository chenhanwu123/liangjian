package com.example.raj_liangjian.dto;


import com.example.raj_liangjian.entity.OrderDetail;
import com.example.raj_liangjian.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {
    //用户名
    private String userName;
    //电话
    private String phone;
    //地址
    private String address;
    //收货人
    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
