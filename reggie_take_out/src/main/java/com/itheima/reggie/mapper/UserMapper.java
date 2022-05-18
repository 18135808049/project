package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cxw
 * @date 2022/5/18 8:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
