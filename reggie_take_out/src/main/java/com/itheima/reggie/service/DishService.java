package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * @author cxw
 * @date 2022/5/13 11:25
 */
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
}
