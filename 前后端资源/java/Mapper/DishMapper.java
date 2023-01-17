package com.example.raj_liangjian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.raj_liangjian.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {


}
