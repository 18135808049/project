package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrderDto;
import com.itheima.reggie.dto.Param;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private DishService dishService;

    @Autowired
    private ShoppingCartController shoppingCartController;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(number != null, Orders::getNumber, number)
                .between(beginTime != null && endTime != null,
                        Orders::getOrderTime,
                        beginTime, endTime);
        ordersService.page(ordersPage, lqw);
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

    @PutMapping
    public R<Integer> edit(@RequestBody Param param) {
//        System.out.println(param.toString());
        Orders orders = ordersService.getById(param.getId());
        orders.setStatus(param.getStatus());
        ordersService.updateById(orders);
        return R.success(param.getStatus());
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        Long currentId = BaseContext.getCurrentId();

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Orders::getUserId, currentId);
        ordersService.page(ordersPage, lqw);
        BeanUtils.copyProperties(ordersPage, orderDtoPage, "records");

        List<Orders> records1 = ordersPage.getRecords();

        List<OrderDto> records = records1.stream().map(item -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            String userName = item.getUserName();
            orderDto.setUserName(userName);
            String consignee = item.getConsignee();
            orderDto.setConsignee(consignee);
            String phone = item.getPhone();
            orderDto.setPhone(phone);
            String address = item.getAddress();
            orderDto.setAddress(address);
            LambdaQueryWrapper<OrderDetail> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(lqw1);
            orderDto.setOrderDetails(orderDetails);
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(records);

        return R.success(orderDtoPage);
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Map<String, Object> map) {
        String idObject = (String) map.get("id");
        Long id = Long.parseLong(idObject);

        Orders orders = ordersService.getById(id);

        Long userId = orders.getUserId();

        LambdaQueryWrapper<OrderDetail> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> list = orderDetailService.list(lqw);
        List<ShoppingCart> collect = list.stream().map(item -> {
            String dishFlavor = item.getDishFlavor();
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            String name = dish.getName();
            Integer number = item.getNumber();
            BigDecimal amount = item.getAmount();
            Long setmealId = item.getSetmealId();
            String image = item.getImage();

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setImage(image);
            shoppingCart.setDishId(dishId);
            shoppingCart.setSetmealId(setmealId);
            shoppingCart.setName(name);
            shoppingCart.setNumber(number);
            shoppingCart.setAmount(amount);
            shoppingCart.setDishFlavor(dishFlavor);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        for (ShoppingCart shoppingCart : collect) {
            Integer number = shoppingCart.getNumber();
            for (int i = 1; i <= number; i++) {
                shoppingCartController.add(shoppingCart);
            }
        }


        return R.success("");
    }

}
