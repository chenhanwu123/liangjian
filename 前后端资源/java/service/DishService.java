package com.example.raj_liangjian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.raj_liangjian.dto.DishDto;
import com.example.raj_liangjian.entity.Dish;

public interface DishService extends IService<Dish> {

    public void AddDish(DishDto dishDto);

    void removeImgFile(Long[] ids);

    boolean updateStatus(int status, Long[] ids);
}
