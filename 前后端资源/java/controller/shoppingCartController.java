package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.raj_liangjian.common.BaseContext;
import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.Dish;
import com.example.raj_liangjian.entity.Setmeal;
import com.example.raj_liangjian.entity.ShoppingCart;
import com.example.raj_liangjian.service.DishService;
import com.example.raj_liangjian.service.SetmealService;
import com.example.raj_liangjian.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> listR() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getThreadLocal());
        lqw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(new ArrayList<>(list));
    }

    /**
     * 添加菜品
     *
     * @param shoppingCart
     * @return
     */
    @Transactional
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id
        Long userID = BaseContext.getThreadLocal();
        shoppingCart.setUserId(userID);

        //查询菜品或者是菜单是否在购物车中
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userID);

        //添加购物车的为菜品
        lqw.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        //添加购物车的为套餐
        lqw.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart one = shoppingCartService.getOne(lqw);

        if (one != null) {
            //如果已经存在，就在原来数量基础上加一
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }


        return R.success(one);
    }

    /**
     * 减数量
     *
     * @param shoppingCart
     * @return
     */
    @Transactional
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long userID = BaseContext.getThreadLocal();
        LambdaUpdateWrapper<ShoppingCart> lqw = new LambdaUpdateWrapper<>();
        //减少菜品
        lqw.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, userID);
        //减少套餐
        lqw.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId()).eq(ShoppingCart::getUserId, userID);

        ShoppingCart one = shoppingCartService.getOne(lqw);

        if (one == null) {
            return R.error("无");
        }

        //如果数量等于1则直接删除
        Integer number = one.getNumber();
        if (number > 1) {
            //数量大于1在原有的基础上减1
            one.setNumber(one.getNumber() - 1);
            shoppingCartService.updateById(one);
        } else {
            //数量等于1直接删除
            one.setNumber(null);
            shoppingCartService.removeById(one);
        }
        return R.success(one);
    }

    /**
     * 清空购物车
     * @return
     */
    @Transactional
    @DeleteMapping("/clean")
    public R<String> clean(){
        //获取用户id
        Long userID = BaseContext.getThreadLocal();
        //条件构造器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.eq(ShoppingCart::getUserId, userID);
        //执行删除
        shoppingCartService.remove(lqw);

        return R.success("清空购物车成功");

    }
}