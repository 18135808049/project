package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.OrdersService;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cxw
 * @date 2022/5/18 16:09
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime,String endTime){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(number!=null,Orders::getNumber,number)
                .between(beginTime!=null&&endTime!=null,
                        Orders::getOrderTime,
                        beginTime,endTime);
        ordersService.page(ordersPage,lqw);
        List<Orders> collect = ordersPage.getRecords().stream().map(item -> {
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            String name = user.getName();
            item.setUserName(name);
            return item;
        }).collect(Collectors.toList());
        ordersPage.setRecords(collect);
        return R.success(ordersPage);
    }
}
