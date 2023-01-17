package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.dto.SetmealDto;
import com.example.raj_liangjian.entity.Category;
import com.example.raj_liangjian.entity.Setmeal;
import com.example.raj_liangjian.entity.SetmealDish;
import com.example.raj_liangjian.service.CategoryService;
import com.example.raj_liangjian.service.SetmealDishService;
import com.example.raj_liangjian.service.SetmealService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {
        //添加分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //添加条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.like(name != null && !"".equals(name.trim()), Setmeal::getName, name);
        //添加排序条件，按时间
        lqw.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(setmealPage, lqw);
        //copy
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> l = records.stream().map((re) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(re, setmealDto);
            Long categoryId = re.getCategoryId();
            Category byId = categoryService.getById(categoryId);

            if (byId != null) {
                setmealDto.setCategoryName(byId.getName());
            }


            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(l);

        return R.success(setmealDtoPage);

    }

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @Transactional
    @PostMapping
    public R<String> AddSetmeal(@RequestBody SetmealDto setmealDto) {
        //保存套餐，同时要本次套餐和菜品的关联关系
        setmealService.save(setmealDto);
        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);

        }
        setmealDishService.saveBatch(setmealDishes);

        return R.success("ok");
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    /**
     * 通过id更改状态
     * @param status
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("status/{status}")
    public R<String> PutStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> lqw = new LambdaUpdateWrapper<>();
        lqw.in(Setmeal::getId, ids);
        lqw.set(ids!=null, Setmeal::getStatus, status);
        boolean update = setmealService.update(lqw);
        if (update){
            return R.success("修改状态成功");
        }
        return R.error("修改状态失败");
    }

    /**
     * 通过id回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable Long id){
        //通过id查询setmeal表
        Setmeal byId = setmealService.getById(id);
        //copy到SetmealDto
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId, setmealDto,"setmealDishes");
        //添加条件构造器
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        //添加条件
        lqw.eq(SetmealDish::getSetmealId, id);
        //执行查询
        List<SetmealDish> list = setmealDishService.list(lqw);
        //set套餐下菜品
        setmealDto.setSetmealDishes(list);

        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @Transactional
    @PutMapping
    public R<String> PutSetmeal(@RequestBody SetmealDto setmealDto){
        //修改setmeal表
        setmealService.updateById(setmealDto);
        //获取id
        Long id = setmealDto.getId();
        //先删除
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(lqw);
        //后添加
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }

        setmealDishService.saveBatch(setmealDishes);

        return R.success("套餐成功");
    }
    @GetMapping("/list")
    public R<List> listR(Long categoryId, int status){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId!=null,Setmeal::getCategoryId, categoryId);
        lqw.eq(Setmeal::getStatus, status);
        lqw.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lqw);
        return R.success(list);
    }
}
