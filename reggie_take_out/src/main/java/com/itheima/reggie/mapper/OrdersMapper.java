package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cxw
 * @date 2022/5/18 16:05
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
