package com.engine.normalMapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
