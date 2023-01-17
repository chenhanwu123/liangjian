package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.Category;
import com.example.raj_liangjian.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService CategoryService;

    /**
     * 添加菜品或套餐
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> AddCategory(@RequestBody Category category) {
        CategoryService.save(category);
        return R.success("新增成功");
    }

    /**
     * 分页条件查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        lqw.orderByAsc(Category::getSort);

        //执行分页查询
        CategoryService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }

    /**
     * 通过id执行删除分类
     *
     * @param id
     * @return
     */

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        log.info("执行删除{}", id);
        CategoryService.remove(id);
        return R.success("删除成功");
    }


    /**
     * 修改菜品或套餐
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {

        //执行修改
        CategoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 查询分类
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //添加条件
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType, category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //执行查询
        List<Category> list = CategoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
