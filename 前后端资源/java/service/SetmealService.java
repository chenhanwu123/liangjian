package com.example.raj_liangjian.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.raj_liangjian.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void removeWithDish(List<Long> ids);
}
