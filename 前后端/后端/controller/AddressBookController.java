package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.raj_liangjian.common.BaseContext;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.AddressBook;
import com.example.raj_liangjian.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /**
     * 新增
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getThreadLocal());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping ("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info(addressBook.toString());
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getThreadLocal());
        wrapper.set(AddressBook::getIsDefault, 0);
        //、SQL:update address_book set is_default = 0 where user_id =？
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBook.setUpdateUser(BaseContext.getThreadLocal());
        log.info(addressBook.toString());


        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到对象");
        }
    }

    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, BaseContext.getThreadLocal());
        lqw.eq(AddressBook::getIsDefault, 1);

        AddressBook Default = addressBookService.getOne(lqw);

        if (Default == null) {
            return R.error("没有找到对象");
        } else {
            return R.success(Default);
        }
    }

    /**
     * 查询指定用户的全部地址
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getThreadLocal());
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);


        return R.success(addressBookService.list(queryWrapper));
    }
    @PutMapping
    public R<String> putAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("地址修改成功");
    }

    @DeleteMapping
    public R<String> deleteAddressBook(Long[] ids){
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.in(ids!=null, AddressBook::getId,ids);
        addressBookService.remove(lqw);
        return R.success("删除地址成功");
    }
}
