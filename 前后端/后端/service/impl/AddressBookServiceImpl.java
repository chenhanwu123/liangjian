package com.example.raj_liangjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.AddressBookMapper;
import com.example.raj_liangjian.entity.AddressBook;
import com.example.raj_liangjian.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
