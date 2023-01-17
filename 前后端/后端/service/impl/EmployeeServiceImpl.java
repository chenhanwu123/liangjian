package com.example.raj_liangjian.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.raj_liangjian.Mapper.EmployeeMapper;
import com.example.raj_liangjian.entity.Employee;
import com.example.raj_liangjian.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
