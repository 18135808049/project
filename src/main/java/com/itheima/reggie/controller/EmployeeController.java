package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author cxw
 * @date 2022/5/11 11:18
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //将员工登录密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据员工用户名查员工信息
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        //员工信息不存在
        if (emp == null) {
            return R.error("没有改账户");
        }

        //密码输入错误
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //员工的状态被禁用
        if (emp.getStatus() == 0) {
            return R.error("员工信息已被禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }
}
