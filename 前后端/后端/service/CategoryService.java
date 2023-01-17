package com.example.raj_liangjian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.raj_liangjian.entity.Category;

public interface CategoryService extends IService<Category> {


    public void remove(Long id);
}
