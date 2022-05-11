package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cxw
 * @date 2022/5/11 10:21
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
