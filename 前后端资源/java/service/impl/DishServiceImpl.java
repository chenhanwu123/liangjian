package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.DishMapper;
import com.example.raj_liangjian.dto.DishDto;
import com.example.raj_liangjian.entity.Dish;
import com.example.raj_liangjian.entity.DishFlavor;
import com.example.raj_liangjian.service.DishFlavorService;
import com.example.raj_liangjian.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Value("Raj.path")
    private String path;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    DishMapper dishMapper;


    /**
     * 新增菜品
     * @param dishDto
     */
    @Transactional
    public void AddDish(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        Long DishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(DishId);
        }
        //保存菜品的口味信息到菜品口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    /**
     * 删除图片
     */
    @Override
    public void removeImgFile(Long[] ids) {
        log.info("进入删除图片");
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId, ids);
        List<Dish> list = this.list(lqw);

        for (Dish dish : list) {
            File file = new File(path+dish.getImage());
            boolean delete = file.delete();
            System.out.println(delete);

        }


    }

    /**
     * 通过id修改状态
     * @param status
     * @param ids
     * @return
     */
    @Transactional
    public boolean updateStatus(int status, Long[] ids) {
        LambdaUpdateWrapper<Dish> lqw = new LambdaUpdateWrapper<>();
        lqw.in(Dish::getId,ids);
        lqw.set(Dish::getStatus, status);
        //sql:update dish set status= 1/0 where id in (????)
        boolean update = this.update(lqw);

        return update;
    }

}
