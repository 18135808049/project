package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

/**
 * @author cxw
 * @date 2022/5/18 16:06
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
