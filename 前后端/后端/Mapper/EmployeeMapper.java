package com.example.raj_liangjian.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.raj_liangjian.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
