package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cxw
 * @date 2022/5/18 14:39
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        if(dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else {
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);

        if(cartServiceOne!=null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .orderByAsc(ShoppingCart::getCreateTime);
        shoppingCartService.remove(lqw);
        return R.success("清空成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        if(dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else {
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);

        if(cartServiceOne.getNumber()>=2){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number-1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number-1);
            LambdaQueryWrapper<ShoppingCart> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                    .eq(ShoppingCart::getNumber,1);
            shoppingCartService.remove(lqw1);
        }
        return R.success(cartServiceOne);
    }
}
