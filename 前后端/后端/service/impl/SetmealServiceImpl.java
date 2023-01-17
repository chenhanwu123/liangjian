package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.SetmealMapper;
import com.example.raj_liangjian.common.CustomException;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.Setmeal;
import com.example.raj_liangjian.entity.SetmealDish;
import com.example.raj_liangjian.service.SetmealDishService;
import com.example.raj_liangjian.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，是否可以删除
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId, ids);
        lqw.eq(Setmeal::getStatus, 1);
        int count = this.count(lqw);
        if (count>0){
            throw new CustomException("套餐正在售卖中,无法被删除");
        }

        //如果可以被删除，先删除套餐表中的套餐数据
        this.removeByIds(ids);
        //删除关联表中的数据
        LambdaQueryWrapper<SetmealDish> RemoveSetmealDish = new LambdaQueryWrapper<>();
        RemoveSetmealDish.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(RemoveSetmealDish);
    }
}
