package com.itheima.reggie.dto;

import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author cxw
 * @date 2022/5/18 21:06
 */
@Data
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
