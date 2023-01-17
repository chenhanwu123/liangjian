package com.example.raj_liangjian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.raj_liangjian.common.R;
import com.example.raj_liangjian.entity.Employee;
import com.example.raj_liangjian.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登入
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }*/
        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.将页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到返回登入失败
        if (emp == null) {
            return R.error("用户名或密码错误");
        }
        //4.密码比对如果不一致返回失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        //5.查询员工状态，如果以为禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //6.登入成功，将员工id存入Session并返回登入成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    /**
     * 退出账号
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //1.清理Session中的用户id
        request.getSession().removeAttribute("employee");
        //2.返回结果
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> AddEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工:{}", employee.toString());
        //把初始密码设为123456，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

       /* employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //获取当前用户id
        long empId = (long) request.getSession().getAttribute("employee");
        //设置更新人
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 通过id回显
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={}&pageSize={}&name={}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //分页条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //判断不为如果name不为null和“”添加此过滤条件
        queryWrapper.like(name != null && !"".equals(name.trim()), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改信息
     * @param httpServletRequest
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> Update(HttpServletRequest httpServletRequest,@RequestBody Employee employee){
        //获取当前登入的id
        long empId = (long)httpServletRequest.getSession().getAttribute("employee");
        //判断修改人是否为管理员
        if (empId!=1||employee.getId()==1){
            return R.error("检测到异常");
        }

        /*//set最后修改时间
        employee.setUpdateTime(LocalDateTime.now());
        //set修改人id
        employee.setUpdateUser(empId);*/
        //执行修改
        employeeService.updateById(employee);

        return R.success("ok");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private R<Employee> selectById(@PathVariable long id){

        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }

        return R.error("没有查询到员工信息");
    }
}
