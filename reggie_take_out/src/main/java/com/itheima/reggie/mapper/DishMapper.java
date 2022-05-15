package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cxw
 * @date 2022/5/13 11:18
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
