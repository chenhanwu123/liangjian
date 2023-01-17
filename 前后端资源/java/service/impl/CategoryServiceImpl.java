package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.CategoryMapper;
import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.entity.Category;
import com.example.raj_liangjian.entity.Dish;
import com.example.raj_liangjian.entity.Setmeal;
import com.example.raj_liangjian.service.CategoryService;
import com.example.raj_liangjian.service.DishService;
import com.example.raj_liangjian.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId, id);
        //执行查询
        int count = dishService.count(lqw);

        if (count>0){
            //有关联的菜品无法被删除，自定义个异常抛出
            throw new CustomException("当前分类下关联有菜品不能被删除");
        }

        //添加查询条件，根据分类id进行查询
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper= new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //执行查询
        int count1 = setmealService.count (setmealLambdaQueryWrapper);
        if (count1>0){
            //有关联的套餐无法被删除，自定义个异常抛出
            throw new CustomException("当前分类下关联有  套餐不能被删除");
        }

        //没有关联，执行删除
        super.removeById(id);
    }
}
